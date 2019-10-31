const {
  User, Transaction, TransactionItem, Voucher,
} = require('../models');

const VOUCHER_FACTOR = 100;

const create = async (productsList, useDiscounts, userId, voucherId) => {
  const transaction = await Transaction.create({ useDiscounts, UserId: userId, voucherId });
  const user = await User.findByPk(userId);
  if (!user) throw new Error('User not found');

  const totalPrice = await createTransactionItems(productsList, transaction.id);

  await newVoucher(user, totalPrice);

  const usedDiscount = await calculateUsedDiscount(transaction.useDiscounts, totalPrice, user);

  if (voucherId) await applyVoucher(totalPrice, voucherId, user, transaction);

  return transaction.update({ totalPrice: totalPrice - usedDiscount });
};

const retrieveByUser = async (userId) => Transaction.findAll({
  where: {
    UserId: userId,
  },
  include: [TransactionItem, Voucher],
});

const createTransactionItems = async (productsList, transactionId) => {
  let totalPrice = 0;
  for await (const product of productsList) {
    await TransactionItem.create({
      ...product, // (uuid, price, quantity)
      TransactionId: transactionId,
    });
    totalPrice += product.quantity * product.price;
  }
  return totalPrice;
};

const newVoucher = async (user, totalPrice) => {
  const newTotalValueSpent = user.totalValueSpent + totalPrice;
  const numberOfVouchers = Math.floor(newTotalValueSpent / VOUCHER_FACTOR) - Math.floor(user.totalValueSpent / VOUCHER_FACTOR);

  await user.update({ totalValueSpent: newTotalValueSpent });

  for (let i = 0; i < numberOfVouchers; i++) await Voucher.create({ UserId: user.id });
};

const calculateUsedDiscount = async (useDiscounts, totalPrice, user) => {
  let usedDiscount = 0;
  if (useDiscounts) {
    const priceToPay = Math.max(totalPrice - user.discountValueAvailable, 0);
    usedDiscount = totalPrice - priceToPay;

    await user.update({ discountValueAvailable: user.discountValueAvailable - usedDiscount });
  }

  return usedDiscount;
};

const applyVoucher = async (totalPrice, voucherId, user, transaction) => {
  const voucher = await Voucher.findByPk(voucherId);
  if (!voucher) throw new Error('Voucher not found');
  if (voucher.isUsed) return;

  const discountValue = totalPrice * (voucher.discount / 100.0);
  await user.update({ discountValueAvailable: user.discountValueAvailable + discountValue });

  await voucher.update({ isUsed: true, TransactionId: transaction.id });

  await transaction.update({ voucherId });
};

module.exports = {
  create,
  retrieveByUser,
};
