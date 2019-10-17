const { User } = require('../models');

const retrieve = async (userId) => User.findByPk(userId);

module.exports = {
  retrieve,
};
