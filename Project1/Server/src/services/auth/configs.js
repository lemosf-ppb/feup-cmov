module.exports = {
  CRYPTO_ALGORITHM: 'RSA-SHA256',
  MODULES_LENGTH: 512,
  PUBLIC_KEY_ENCODING: {
    type: 'spki',
    format: 'pem',
  },
  PRIVATE_KEY_ENCODING: {
    type: 'pkcs8',
    format: 'pem',
  },
};
