const {
  User, Transaction, TransactionItem, Voucher,
} = require('../models');

const create = async (productsList, useDiscounts, userId, voucherId) => {
  const transaction = await Transaction.create({ useDiscounts, UserId: userId, voucherId });
  const user = await User.findByPk(userId);
  if (!user) throw new Error('User not found');

  const totalPrice = await createTransactionItems(productsList, transaction.id);

  const usedDiscount = await calculateUsedDiscount(useDiscounts, totalPrice, user);

  if (voucherId) await applyVoucher(totalPrice, voucherId, user, transaction);

  await newVoucher(user, totalPrice);

  return transaction.update({ totalPrice: totalPrice - usedDiscount });
};

const retrieveByUser = async (userId) => Transaction.findAll({
  where: {
    UserId: userId,
  },
  include: [{
    model: TransactionItem,
  }],
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
  // TODO: Check if it will be created a new voucher

  // Update user total value spent
  await user.update({ totalValueSpent: user.totalValueSpent + totalPrice });

  // TODO: Create new voucher
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

  const discountValue = totalPrice * (voucher.discount / 100.0);

  await user.update({ discountValueAvailable: user.discountValueAvailable + discountValue });

  await voucher.update({ isUsed: true });

  await transaction.update({ voucherId });
};

module.exports = {
  create,
  retrieveByUser,
};
