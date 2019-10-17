const { User } = require('../models');

const {
  transactionsController,
} = require('../controllers');

const initializeUsers = async () => {
  await Promise.all([
    User.create({ email: 'john@template.com', password: 'john' }),
    User.create({ email: 'jane@template.com', password: 'jane' }),
    User.create({ email: 'test@template.com', password: 'test' }),
  ]);
};

const initializeTransactions = async () => {
  await Promise.all([
    transactionsController.create(),
    transactionsController.create(),
    transactionsController.create(),
  ]);
};


const initializeDatabase = async () => {
  // await initializeUsers();
  // await initializeTransactions();
};

module.exports = {
  initializeDatabase,
};
