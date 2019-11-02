const router = require('express').Router();
const { usersController, vouchersController } = require('../controllers');
const { auth } = require('../services/auth');


router.post('/vouchers/unused/user', async (req, res) => {
  const { userId, signature } = req.body;

  try {
    await usersController.verifyAuth(userId, userId, signature);
    const vouchersUnused = await vouchersController.retrieveUnusedByUser(userId);

    const payload = { vouchers: vouchersUnused };
    const signedSupermarket = auth.createSignature(JSON.stringify(payload));
    return res.status(200).send({ ...payload, signature: signedSupermarket });
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
