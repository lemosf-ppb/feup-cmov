const bcrypt = require('bcrypt');

const SALT_WORK_FACTOR = 10;

module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('User', {
    uuid: {
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      primaryKey: true,
    },
    name: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    username: {
      type: DataTypes.STRING,
      unique: true,
      allowNull: false,
    },
    password: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    // TODO: Think if it will be necessary to extract credit card to an independent entity
    creditCard: {
      type: DataTypes.STRING,
      allowNull: false,
      validate: {
        isCreditCard: true,
      },
    },
    publicKey: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    totalValueAccumulated: {
      type: DataTypes.DECIMAL(10, 2),
      allowNull: false,
      defaultValue: 0,
    },
  },
  {});

  // eslint-disable-next-line
  User.prototype.isValidPassword = async function (password) {
    const compare = await bcrypt.compare(password, this.password);
    return compare;
  };

  // TODO: Check this
  User.associate = (models) => {
    User.hasMany(models.Transaction, {
      foreignKey: 'userId',
    });
    User.hasMany(models.Voucher, {
      foreignKey: 'userId',
    });
  };

  User.beforeCreate(async (user) => {
    /* eslint-disable */
    user.password = await bcrypt.hash(user.password, SALT_WORK_FACTOR);
  });

  return User;
};
