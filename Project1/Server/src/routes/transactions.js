const router = require('express').Router();
const { usersController, transactionsController, vouchersController } = require('../controllers');
const { auth } = require('../services/auth');

router.post('/transactions', async (req, res) => {
  const {
    userId, productsList, useDiscounts, voucherId, signature,
  } = req.body;

  const payload = {
    userId, productsList, useDiscounts, voucherId,
  };

  try {
    await usersController.verifyAuth(JSON.stringify(payload), userId, signature);
    const transaction = await transactionsController.create(
      productsList,
      useDiscounts,
      userId,
      voucherId,
    );
    return res.status(200).send(transaction);
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

router.post('/transactions/user', async (req, res) => {
  const { userId, signature } = req.body;

  try {
    await usersController.verifyAuth(userId, userId, signature);
    const transactions = await transactionsController.retrieveByUser(userId);
    const unusedVouchers = await vouchersController.retrieveUnusedByUser(userId);

    const payload = { transactions, vouchers: unusedVouchers };
    const signedSupermarket = auth.createSignature(JSON.stringify(payload));

    return res.status(200).send({
      ...payload,
      signature: signedSupermarket,
    });
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
