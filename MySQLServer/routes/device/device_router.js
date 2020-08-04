var express = require('express');
var router = express.Router();

var deviceinfo = require('./info.js');



router.use(express.json());
router.use('/info',deviceinfo);


module.exports = router;
