const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;
const { Strategy, ExtractJwt } = require('passport-jwt');
const { User } = require('../../models');
const { JWT_SECRET } = require('../../config/configs');

passport.use('signup', new LocalStrategy({
  usernameField: 'username',
  passwordField: 'password',
  passReqToCallback: true,
}, async (req, username, password, done) => {
  try {
    const user = await User.findOne({
      where: { username },
    });

    if (user) {
      return done(null, false, { message: 'User already exists' });
    }
    const { name, creditCard, publicKey } = req.body;
    const newUser = await User.create({
      name, username, password, creditCard, publicKey,
    });

    return done(null, newUser);
  } catch (error) {
    return done(error);
  }
}));

passport.use('login', new LocalStrategy({
  usernameField: 'username',
  passwordField: 'password',
}, async (username, password, done) => {
  try {
    const user = await User.findOne({
      where: { username },
    });
    if (!user) {
      return done(null, false, { message: 'User not found' });
    }
    const validate = await user.isValidPassword(password);
    if (!validate) {
      return done(null, false, { message: 'Wrong Password' });
    }
    return done(null, user);
  } catch (error) {
    return done(error);
  }
}));

passport.use(new Strategy({
  secretOrKey: JWT_SECRET,
  jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
}, async (token, done) => {
  try {
    return done(null, token.user);
  } catch (error) {
    return done(error);
  }
}));
