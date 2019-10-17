module.exports = (sequelize, DataTypes) => {
  const Transaction = sequelize.define('Transaction', {
    useDiscounts: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
    },
    totalPrice: {
      type: DataTypes.DECIMAL(10, 2),
      allowNull: false,
    },
  });

  Transaction.associate = (models) => {
    Transaction.hasMany(models.TransactionItem, {
      foreignKey: 'transactionId',
    });
    Transaction.belongsTo(models.User, {
      foreignKey: 'userId',
    });
    // TODO: Check this if allow null or not
    Transaction.hasOne(models.Voucher, {
      foreignKey: 'voucherId',
    });
  };

  Transaction.beforeCreate(async (transaction) => {
    // Update total Price
    // const book = await order.getBook();
    // order.totalPrice = order.quantity * book.price;
  });

  Transaction.afterCreate(async (transaction) => {
    // Create voucher? new multiple of 100.00€
  });

  return Transaction;
};
