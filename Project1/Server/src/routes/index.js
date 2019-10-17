const router = require('express').Router();

/*+++++++++++++++++++++++++++++++++++++++++++++
 Routes
 ++++++++++++++++++++++++++++++++++++++++++++++*/

const passport = require('passport');
const auth = require('./auth');
const transactions = require('./transactions');
const users = require('./users');
const vouchers = require('./vouchers');


router.use('/', auth);
router.use('/', passport.authenticate('jwt', { session: false }));
router.use('/', transactions);
router.use('/', users);
router.use('/', vouchers);


module.exports = router;
