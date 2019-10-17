const router = require('express').Router();
const { usersController } = require('../controllers');

router.get('/users/me', async (req, res) => {
  const { userId } = req.body;

  try {
    const userInfo = await usersController.retrieve(userId);
    res.status(200).send(userInfo);
  } catch (error) {
    res.status(400).send(error.message);
  }
});

module.exports = router;
