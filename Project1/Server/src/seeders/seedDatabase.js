const { User, CreditCard } = require('../models');

const initializeUser = async () => {
  const newUser = await User.create({
    id: '96b471c1-0372-41cc-a121-9a8e3dc74662',
    username: 'john@store.com',
    password: 'john',
    name: 'John Doe',
    publicKey: '-----BEGIN PUBLIC KEY-----\nMFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMx7tRTJd4w9lFrYB6RbmC/2EgW/Te8D\nIlLuy9YmSnnOWO4qvH8Hm+5+t0yeadQUekRbEJV9QDzBawDnvk01ItcCAwEAAQ==\n-----END PUBLIC KEY-----\n',
  });
  const userCreditCard = await CreditCard.create({
    number: '1234123412341234',
    validity: '05/19',
    cvv: '123',
    holder: 'John Doe',
  });
  await newUser.setCreditCard(userCreditCard);
};

const initializeDatabase = async () => {
  await initializeUser();
};

module.exports = {
  initializeDatabase,
};
