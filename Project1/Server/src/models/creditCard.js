module.exports = (sequelize, DataTypes) => {
  const CreditCard = sequelize.define('CreditCard', {
    holder: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    number: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    cvv: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    validity: {
      type: DataTypes.STRING,
      allowNull: false,
    },
  });

  CreditCard.associate = (models) => {
    CreditCard.belongsTo(models.User, {
      onDelete: 'CASCADE',
      onUpdate: 'CASCADE',
    });
  };

  return CreditCard;
};
