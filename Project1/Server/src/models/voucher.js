module.exports = (sequelize, DataTypes) => {
  const Voucher = sequelize.define('Voucher', {
    id: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true,
    },
    discount: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 15,
    },
    isUsed: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false,
    },
  });

  Voucher.associate = (models) => {
    Voucher.belongsTo(models.User);
    Voucher.belongsTo(models.Transaction);
  };

  return Voucher;
};
