const { Voucher } = require('../models');

const retrieveUnusedByUser = async (userId) => Voucher.findAll({
  where: {
    UserId: userId,
    isUsed: false,
  },
});

module.exports = {
  retrieveUnusedByUser,
};
