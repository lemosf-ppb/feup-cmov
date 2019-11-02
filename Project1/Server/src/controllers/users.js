const { CreditCard, User } = require('../models');
const { auth } = require('../services/auth');

const signup = async (username, password, name, creditCard, publicKey) => {
  const user = await User.findOne({
    where: { username },
  });

  if (user) {
    throw new Error('User already exists');
  }

  const newUser = await User.create({
    name, username, password, publicKey,
  });

  const userCreditCard = await CreditCard.create({
    holder: creditCard.holder,
    number: creditCard.number,
    cvv: creditCard.cvv,
    validity: creditCard.validity,
  });

  await newUser.setCreditCard(userCreditCard);

  return newUser;
};

const login = async (username, password, publicKey) => {
  const user = await User.findOne({
    where: { username },
  });

  if (!user) {
    throw new Error('User not found');
  }

  const validate = await user.isValidPassword(password);
  if (!validate) {
    throw new Error('Wrong Password');
  }

  await user.update({ publicKey });

  return user;
};

const retrieve = async (userId) => {
  const user = await User.findByPk(userId,
    {
      include: [{
        model: CreditCard,
      }],
    });

  if (!user) {
    throw new Error('User not found');
  }

  delete user.dataValues.password;
  delete user.dataValues.updatedAt;
  delete user.dataValues.publicKey;

  return user;
};

const verifyAuth = async (object, userId, signature) => {
  const user = await User.findByPk(userId);
  if (!user) {
    throw new Error('User not found');
  }

  const signatureValid = auth.verifySignature(object, user.publicKey, signature);
  if (!signatureValid) {
    throw new Error('Signature invalid');
  }
};

module.exports = {
  signup,
  login,
  retrieve,
  verifyAuth,
};
