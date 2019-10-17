const router = require('express').Router();
const { usersController } = require('../controllers');

router.get('/users/me', async (req, res) => {
  try {
    const userInfo = await usersController.retrieve(req.user.id);
    res.status(200).send(userInfo);
  } catch (error) {
    res.status(400).send(error.message);
  }
});

module.exports = router;
