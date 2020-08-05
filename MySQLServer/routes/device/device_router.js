var express = require('express');
var router = express.Router();

var deviceinfo = require('./info.js');
var cors = require('cors');


router.use(express.json());
router.use('/info',deviceinfo);


module.exports = router;
