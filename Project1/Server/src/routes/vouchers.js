const router = require('express').Router();
const { usersController, vouchersController } = require('../controllers');
const { auth } = require('../services/auth');


router.post('/vouchers/unused/user', async (req, res) => {
  const { userId, signature } = req.body;

  const user = await usersController.retrieve(userId);
  if (!user) {
    return res.status(400).send('User not found');
  }

  console.log(userId);
  console.log(signature);
  const verifyAuth = auth.verifySignature(userId, user.publicKey, signature);
  if (!verifyAuth) {
    return res.status(400).send('Signature invalid');
  }

  try {
    const vouchersUnused = await vouchersController.retrieveUnusedByUser(userId);
    return res.status(200).send(vouchersUnused);
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
