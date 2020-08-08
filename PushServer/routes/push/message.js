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
router.get('/getpushlist',function(req,res){

		//var key = req.query.token;
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
					var exec = conn.query('select message from pushlist',function(err,result){
						conn.release();
						
						res.header("Access-Control-Allow-Headers","Authorization");
						res.header("Access-Control-Expose-Headers","*");
						if(err){
						res.send({status:false});
						}
						else{	
						console.log(result);
						console.log('------');
						
						//res.send({"value":["DEVICE 01 ERROR", "DEVICE 03 ERROR", "DEVICE 05 ERROR"]})
						if(result.length){
						console.log('Success');
						var resultarray = [];
						for(var i=0;i<result.length;i++)
						{
						    resultarray.push(result[i].message);
						}
						res.send({status:true,payload:resultarray});
						}
						else
						{
						console.log('Success');
						res.send({status:true,payload:null});
						}
						}

						});

		});

});

router.post('/addpushlist',function(req,res){
	var key = req.body.message;
	pool.getConection(function(err,conn){
		if(err){
		    if(conn){
			conn.release();
			}
			throw err;
		    }

		    var exec = conn.query('insert pushlist values(?)',key,function(err,result){
			    
			    conn.release();
			    
			    if(err){
			    res.send({status:false});
			    }
			    else
			    {
			    res.send({status:true});
			    }
			    
			    });
	
	
	});
});


module.exports = router;
