const { Voucher } = require('../models');

const retrieveUnusedByUser = async (userId) => Voucher.findAll({
  where: {
    userId,
    isUsed: false,
  },
});

module.exports = {
  retrieveUnusedByUser,
};
