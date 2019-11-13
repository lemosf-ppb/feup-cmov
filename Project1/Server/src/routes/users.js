const router = require('express').Router();
const { usersController } = require('../controllers');
const { auth } = require('../services/auth');

router.post('/users/user', async (req, res) => {
  const { userId, signature } = req.body;
  try {
    await usersController.verifyAuth(userId, userId, signature);
    const user = await usersController.retrieve(userId);

    const signedSupermarket = auth.createSignature(JSON.stringify(user));
    return res.status(200).send({ user, signature: signedSupermarket });
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
