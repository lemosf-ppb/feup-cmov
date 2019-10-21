const bcrypt = require('bcrypt');

const SALT_WORK_FACTOR = 10;

module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('User', {
    id: {
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
    publicKey: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    totalValueSpent: {
      type: DataTypes.DECIMAL(10, 2),
      allowNull: false,
      defaultValue: 0,
    },
    discountValueAvailable: {
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

  User.beforeCreate(async (user) => {
    /* eslint-disable */
    user.password = await bcrypt.hash(user.password, SALT_WORK_FACTOR);
  });

  return User;
};
