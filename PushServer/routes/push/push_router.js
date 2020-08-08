var express = require('express');
var router = express.Router();

var tokenAdd = require('./tokenadd.js');
var cors = require('cors');
var pushAll = require('./pushall.js');
var message = require('./message.js');


router.use(express.json());
router.use('/tokenadd',tokenAdd);
router.use('/pushall',pushAll);
router.use('/message',message);



module.exports = router;
