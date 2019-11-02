const router = require('express').Router();
const { usersController, transactionsController, vouchersController } = require('../controllers');
const { auth } = require('../services/auth');

router.post('/transactions', async (req, res) => {
  const {
    userId, productsList, useDiscounts, voucherId, signature,
  } = req.body;

  const user = await usersController.retrieve(userId);
  if (!user) {
    return res.status(400).send('User not found');
  }

  const payload = {
    userId, productsList, useDiscounts, voucherId,
  };
  const verifyAuth = auth.verifySignature(JSON.stringify(payload), user.publicKey, signature);
  if (!verifyAuth) {
    return res.status(400).send('Signature invalid');
  }

  try {
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

  const user = await usersController.retrieve(userId);
  if (!user) {
    return res.status(400).send('User not found');
  }

  const verifyAuth = auth.verifySignature(userId, user.publicKey, signature);
  if (!verifyAuth) {
    return res.status(400).send('Signature invalid');
  }

  try {
    const transactions = await transactionsController.retrieveByUser(userId);
    const unusedVouchers = await vouchersController.retrieveUnusedByUser(userId);
    return res.status(200).send({ transactions, vouchers: unusedVouchers });
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
