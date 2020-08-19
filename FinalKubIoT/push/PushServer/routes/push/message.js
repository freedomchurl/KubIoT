var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var cors = require('cors');

var pool = mysql.createPool({
connectionLimit : 20,
host : '101.101.219.90',
user : 'root',
password : 'dlcjf2779!',
database : 'kubiot',
debug : true,
charset : 'utf8'
});


router.use(express.json());
router.get('/checkpush',function(req,res){
	var value = req.query.ischecked;
	var key = req.query.pushID;
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
			data = [value, key];
				var exec = conn.query('update push_data set ischecked=? where id=?',data,function(err,result){
					conn.release();
					
					res.header("Access-Control-Allow-Headers","Authorization");
					res.header("Access-Control-Expose-Headers","*");
					if(err){
					res.send({status:false});
					}
					else{	
					console.log(result);
					console.log('------');
					
					
					res.send({status:true});
					}

					});

	});

});
router.get('/getpushnum',function(req,res){

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
				var exec = conn.query('select count(*) as pushnum from push_data where ischecked=0',function(err,result){
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
					// var resultarray = [];
					// for(var i=0;i<result.length;i++)
					// {
					//     resultarray.push(result[i].message);
					// }
					res.send({status:true,payload:result[0]});
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
					var exec = conn.query('select device.id dID, device.name dName,push_data.time time, push_data.ischecked ischecked,push_data.id pushID from push_data inner join device where push_data.device=device.id',function(err,result){
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
						// var resultarray = [];
						// for(var i=0;i<result.length;i++)
						// {
						//     resultarray.push(result[i].message);
						// }
						res.send({status:true,payload:result});
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

router.get('/addpushlist',function(req,res){
	var key = req.query.deviceID;	
	// device info.
	pool.getConection(function(err,conn){
		if(err){
		    if(conn){
			conn.release();
			}
			throw err;
		    }

		    var exec = conn.query('insert into push_data(device) values(?)',key,function(err,result){
			    
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
