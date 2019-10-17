const router = require('express').Router();
const { transactionsController } = require('../controllers');

router.post('/transactions', async (req, res) => {
  const {
    productsList, useDiscounts, voucherId,
  } = req.body;

  try {
    const transaction = await transactionsController.create(
      productsList,
      useDiscounts,
      req.user.id,
      voucherId,
    );
    res.status(201).send(transaction);
  } catch (error) {
    res.status(400).send(error.message);
  }
});

router.get('/transactions', async (req, res) => {
  const userId = req.user.id;

  try {
    const transactions = await transactionsController.retrieveByUser(userId);
    res.status(200).send(transactions);
  } catch (error) {
    res.status(400).send(error.message);
  }
});

module.exports = router;
