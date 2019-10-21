const router = require('express').Router();
const { User } = require('../models');
const { auth } = require('../services/auth');

router.post('/signup', async (req, res) => {
  const {
    username, password, name, creditCard, publicKey,
  } = req.body;

  const user = await User.findOne({
    where: { username },
  });

  if (user) {
    return res.status(400).send('User already exists');
  }

  const newUser = await User.create({
    name, username, password, creditCard, publicKey,
  });

  return res.json({
    message: 'Login successful',
    userId: newUser.id,
    supermarketPublicKey: auth.getPublicKey(),
  });
});

router.post('/login', async (req, res) => {
  const { username, password } = req.body;

  const user = await User.findOne({
    where: { username },
  });

  if (!user) {
    return res.status(400).send('User not found');
  }

  const validate = await user.isValidPassword(password);
  if (!validate) {
    return res.status(400).send('Wrong Password');
  }

  return res.json({
    message: 'Login successful',
    userId: user.id,
    supermarketPublicKey: auth.getPublicKey(),
    userPK: user.publicKey,
  });
});

module.exports = router;
