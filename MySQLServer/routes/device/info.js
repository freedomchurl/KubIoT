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
router.get('/getgroupinfo',function(req,res){
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function(err,conn){
		if(err){
			if(conn){
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
	//	data = "id=" + id + " and " + "pass=" + pwd;
	//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select * from groupinfo',
		function(err,result){
			conn.release();
			res.header("Access-Control-Allow-Headers","Authorization");
			res.header("Access-Control-Expose-Headers","*");
			if(err){
				res.send({status:false});
			}
			else{
				console.log(result.length);
				console.log(result);	
				if(result.length >= 1){
					res.send({status:true,payload:result});
				}
				else{
					res.send({status:true,payload:null});
				}
			}
		});

		

	});
});

router.get('/dnumpergroup',function(req,res){
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function(err,conn){
		if(err){
			if(conn){
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
	//	data = "id=" + id + " and " + "pass=" + pwd;
	//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select count(*) as dnum,groupinfo.name as gName,groupinfo.id gID from groupinfo inner join groupregi where groupinfo.id=groupregi.groupid group by groupinfo.id',
		function(err,result){
			conn.release();
			res.header("Access-Control-Allow-Headers","Authorization");
			res.header("Access-Control-Expose-Headers","*");
			if(err){
				res.send({status:false});
			}
			else{
				console.log(result.length);
				console.log(result);	
				if(result.length == 1){
					res.send({status:true,payload:result[0]});
				}
				else{
					res.send({status:true,payload:null});
				}
			}
		});

		

	});
});


router.get('/devicenum',function(req,res){
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function(err,conn){
		if(err){
			if(conn){
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
	//	data = "id=" + id + " and " + "pass=" + pwd;
	//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select count(id) dnum from device',function(err,result){
			conn.release();
			res.header("Access-Control-Allow-Headers","Authorization");
			res.header("Access-Control-Expose-Headers","*");
			if(err){
				res.send({status:false});
			}
			else{
				console.log(result.length);
				console.log(result);	
				if(result.length == 1){
					res.send({status:true,payload:result[0]});
				}
				else{
					res.send({status:false,payload:null});
				}
			}
		});

		

	});
});

router.get('/groupnum',function(req,res){
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function(err,conn){
		if(err){
			if(conn){
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
	//	data = "id=" + id + " and " + "pass=" + pwd;
	//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select count(id) gnum from groupinfo',function(err,result){
			conn.release();
			res.header("Access-Control-Allow-Headers","Authorization");
			res.header("Access-Control-Expose-Headers","*");
			if(err){
				res.send({status:false});
			}
			else{
				console.log(result.length);
				console.log(result);	
				if(result.length == 1){
					res.send({status:true,payload:result[0]});
				}
				else{
					res.send({status:false,payload:null});
				}
			}
		});

		

	});
});

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
						
						res.header("Access-Control-Allow-Headers","Authorization");
						res.header("Access-Control-Expose-Headers","*");
						if(err){
						res.send({status:false,payload:null});
						}
						else{	
						if(result.length){
						res.send({status:true,payload:result});
						}
						else
						{
						res.send({status:true,paylod:null});
						}
						}

						});

		});

});

// /device/info/devicenum get



module.exports = router;
