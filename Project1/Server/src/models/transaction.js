module.exports = (sequelize, DataTypes) => {
  const Transaction = sequelize.define('Transaction', {
    useDiscounts: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
    },
    totalPrice: {
      type: DataTypes.DECIMAL(10, 2),
      allowNull: false,
      defaultValue: 0,
    },
  });

  Transaction.associate = (models) => {
    Transaction.hasMany(models.TransactionItem);
    Transaction.belongsTo(models.User);
    // TODO: Check this if allow null or not
    Transaction.hasOne(models.Voucher, {
      foreignKey: 'TransactionId',
    });
  };

  return Transaction;
};
