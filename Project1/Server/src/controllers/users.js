const { CreditCard, User } = require('../models');

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

  await userCreditCard.setUser(newUser);

  return newUser;
};

const login = async (username, password) => {
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

  return user;
};

const retrieve = async (userId) => User.findByPk(userId);

module.exports = {
  signup,
  login,
  retrieve,
};
