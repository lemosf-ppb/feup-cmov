const router = require('express').Router();
const { usersController } = require('../controllers');
const { auth } = require('../services/auth');

router.post('/signup', async (req, res) => {
  const {
    username, password, name, creditCard, publicKey,
  } = req.body;

  try {
    const newUser = await usersController.signup(
      username, password, name, creditCard, publicKey,
    );
    return res.status(200).send({
      message: 'Signup successful',
      userId: newUser.id,
      supermarketPublicKey: auth.getPublicKey(),
    });
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

router.post('/login', async (req, res) => {
  const { username, password } = req.body;

  try {
    const newUser = await usersController.login(username, password);
    return res.status(200).send({
      message: 'Login successful',
      userId: newUser.id,
      supermarketPublicKey: auth.getPublicKey(),
    });
  } catch (error) {
    return res.status(400).send(error.message);
  }
});

module.exports = router;
