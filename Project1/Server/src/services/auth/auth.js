const crypto = require('crypto');
const fs = require('fs');
const path = require('path');
const {
  CRYPTO_ALGORITHM, MODULES_LENGTH, PUBLIC_KEY_ENCODING, PRIVATE_KEY_ENCODING,
} = require('./configs');

let PUBLIC_KEY = null;
let PRIVATE_KEY = null;

const generateKeys = () => crypto.generateKeyPairSync('rsa', {
  modulusLength: MODULES_LENGTH,
  publicKeyEncoding: PUBLIC_KEY_ENCODING,
  privateKeyEncoding: PRIVATE_KEY_ENCODING,
});

const createSignature = (object) => {
  const signerObject = crypto.createSign(CRYPTO_ALGORITHM);
  signerObject.update(object);
  const signature = signerObject.sign(PRIVATE_KEY, 'hex');
  return signature;
};

const verifySignature = (object, publicKey, signature) => {
  const verifierObject = crypto.createVerify(CRYPTO_ALGORITHM);
  verifierObject.update(object);
  const verified = verifierObject.verify(publicKey, signature, 'hex');
  return verified;
};

const createPublicKey = (publicKeyString) => crypto.createPublicKey(publicKeyString);

const createPrivateKey = (privateKeyString) => crypto.createPrivateKey(privateKeyString);

const getPublicKey = () => PUBLIC_KEY.export(PUBLIC_KEY_ENCODING);

const getPrivateKey = () => PRIVATE_KEY.export(PRIVATE_KEY_ENCODING);

const init = () => {
  const publicKeyFile = fs.readFileSync(path.join(path.resolve(), 'src/keys/supermarket_public.pem'));
  PUBLIC_KEY = createPublicKey(publicKeyFile);

  const privateKeyFile = fs.readFileSync(path.join(path.resolve(), 'src/keys/supermarket_private.pem'));
  PRIVATE_KEY = createPrivateKey(privateKeyFile);
};

module.exports = {
  init,
  createSignature,
  verifySignature,
  getPublicKey,
  getPrivateKey,
  createPublicKey,
  createPrivateKey,
};
