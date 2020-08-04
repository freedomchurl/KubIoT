var express = require('express');
var router = express.Router();
var mysql = require('mysql');


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

		var key = req.query.pageinfo;
		pool.getConnection(function(err,conn){
				if(err){
				if(conn){
				conn.release();
				}
				throw err;
				}
	//			key가 2라면, 2번째 페이지. 페이지당 20개니까,
				//(key-1)*20 ~ 20
				var startindex = (key-1)*20;
					var exec = conn.query('select * from device order by time desc limit ?,20',startindex,function(err,result){
						conn.release();
						if(err){
						res.send({status:false,payload:null});
						}
						else{	
						if(result.length){
						res.send({status:true,payload:result[0]});
						}
						else
						{
						res.send({status:true,paylod:null});
						}
						}

						});

		});

});


module.exports = router;
