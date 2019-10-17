const router = require('express').Router();
const { vouchersController } = require('../controllers');

router.get('/vouchers/unused', async (req, res) => {
  try {
    const vouchersUnused = await vouchersController.retrieveUnusedByUser(req.user.id);
    res.status(200).send(vouchersUnused);
  } catch (error) {
    res.status(400).send(error.message);
  }
});

module.exports = router;
