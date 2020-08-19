var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var cors = require('cors');

var pool = mysql.createPool({
connectionLimit : 20,
host : 'localhost',
user : 'root',
password : 'dlcjf2779!',
database : 'kubiot',
debug : true,
charset : 'utf8'
});


router.use(express.json());
router.get('/',function(req,res){

		var key = req.query.token;
		pool.getConnection(function(err,conn){
				if(err){
				if(conn){
				conn.release();
				}
				throw err;
				}
	//			key가 2라면, 2번째 페이지. 페이지당 20개니까,
				//(key-1)*20 ~ 20
				//var startindex = (key-1)*20;
					var exec = conn.query('insert into fcmtoken values(?)',key,function(err,result){
						conn.release();
						
						res.header("Access-Control-Allow-Headers","Authorization");
						res.header("Access-Control-Expose-Headers","*");
						if(err){
						res.send({status:true});
						}
						else{	
						console.log(result);
						console.log('------');
						if(result.length){
						console.log('Success');
						res.send({status:true});
						}
						else
						{
						console.log('Success');
						res.send({status:true});
						}
						}

						});

		});

});


module.exports = router;
