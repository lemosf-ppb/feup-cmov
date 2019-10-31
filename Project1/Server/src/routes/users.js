const router = require('express').Router();
const { usersController } = require('../controllers');
const { auth } = require('../services/auth');

router.post('/users/user', async (req, res) => {
  const { userId, signature } = req.body;
  try {
    const user = await usersController.retrieve(userId);
    if (!user) {
      return res.status(400).send('User not found');
    }

    const verifyAuth = auth.verifySignature(userId, user.publicKey, signature);
    if (!verifyAuth) {
      return res.status(400).send('Signature invalid');
    }

    return res.status(200).send(user);
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
