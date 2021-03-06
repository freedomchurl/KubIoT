var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var cors = require('cors');
var redis = require('redis');
const client = redis.createClient({port:6379,host:'101.101.219.90',password:'dlcjf2779!'})

var pool = mysql.createPool({
	connectionLimit: 20,
	host: 'localhost',
	user: 'root',
	password: 'dlcjf2779!',
	database: 'kubiot',
	debug: true,
	charset: 'utf8',
	multipleStatements: true,
});


router.use(express.json());

router.get('/getliveData', function (req, res) {
	var dID = req.query.dName;
	console.log(req);

	const multi = client.multi();
	multi.lrange(dID+":input:data",0,-1)
	multi.lrange(dID+":input:time",0,-1)

	multi.exec(function(err,result){
		if(err){
			res.send({status:false});
		}
		else{
			res.send({status:true,payload:result});
		}
	});
	
});


router.get('/control_device', function (req, res) {
	var min = req.query.min;
	var max =req.query.max;
	var dID = req.query.dName;
	console.log(req);
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [memo,id];
		data =[min, max,dID,min,max,dID];
		var exec = conn.query('insert into control_device(min,max,deviceid) values(?,?,?) on duplicate key update min=?,max=?,deviceid=?',data,function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				// console.log(result.length);
				// console.log(result);	
				// if(result.length == 1){
				// 	res.send({status:true});
				// }
				// else{
				// 	res.send({status:false});
				// }
				// update는 별다른 result가 없음
					res.send({status:true})
			}
		});

		

	});
});


router.get('/deleteDeviceongroup', function (req, res) {
	var dID = req.query.dID;
	var gID =req.query.gID;
	console.log(req);
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [memo,id];
		data =[dID, gID];
		var exec = conn.query('delete from groupregi where deviceid=? and groupid=?',data,function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				// console.log(result.length);
				// console.log(result);	
				// if(result.length == 1){
				// 	res.send({status:true});
				// }
				// else{
				// 	res.send({status:false});
				// }
				// update는 별다른 result가 없음
					res.send({status:true})
			}
		});

		

	});
});

router.post('/getDeviceongroup', function (req, res) {
	var gID = req.body.gID;
	console.log(req);
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [memo,id];
		
		var exec = conn.query('select device.id id,device.name name,device.time time,device.protocol protocol,device.type type,device.location location,device.memo memo from groupregi inner join device where device.id=groupregi.deviceid and groupregi.groupid=?',
			gID,function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				// console.log(result.length);
				// console.log(result);	
				// if(result.length == 1){
				// 	res.send({status:true});
				// }
				// else{
				// 	res.send({status:false});
				// }
				// update는 별다른 result가 없음
				if(result.length>=1)
					res.send({ status: true,payload:result }); // 성공했으면.
				else
					res.send({status:true,payload:null})
			}
		});

		

	});
});


router.post('/addDeviceongroup', function (req, res) {
	var gID = req.body.gID;
	var did = req.body.dID;
	console.log(req);
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [memo,id];
		var mult_query = '';
		for (var i = 0; i < did.length; i++) {
			mult_query += 'insert into groupregi(deviceid,groupid) values(' + did[i] + ',' + gID + ');'
		}
		var exec = conn.query(mult_query, function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				// console.log(result.length);
				// console.log(result);	
				// if(result.length == 1){
				// 	res.send({status:true});
				// }
				// else{
				// 	res.send({status:false});
				// }
				// update는 별다른 result가 없음
				res.send({ status: true }); // 성공했으면.
			}
		});

		

	});
});

router.post('/creategroup', function (req, res) {
	var name = req.body.gName;
	var did = req.body.dID;
	console.log(req);
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [memo,id];
		var exec = conn.query('insert into groupinfo(name) values(?); select last_insert_id() as gID;', name, function (err, result) {
			//conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				// console.log(result.length);
				// console.log(result);	
				// if(result.length == 1){
				// 	res.send({status:true});
				// }
				// else{
				// 	res.send({status:false});
				// }
				// update는 별다른 result가 없음
				//res.send({status:true}); // 성공했으면.
				console.log(result[1]);

				var gID = result[1][0].gID;
				var mult_query = '';
				for (var i = 0; i < did.length; i++) {
					mult_query += 'insert into groupregi(deviceid,groupid) values(' + did[i] + ',' + gID + ');'
				}
				var exec = conn.query(mult_query, function (err, result) {
					conn.release();
					res.header("Access-Control-Allow-Headers", "Authorization");
					res.header("Access-Control-Expose-Headers", "*");
					if (err) {
						res.send({ status: false });
					}
					else {
						// console.log(result.length);
						// console.log(result);	
						// if(result.length == 1){
						// 	res.send({status:true});
						// }
						// else{
						// 	res.send({status:false});
						// }
						// update는 별다른 result가 없음
						res.send({ status: true }); // 성공했으면.
					}
				});

			}
		});



	});
});


router.post('/memochange', function (req, res) {
	var id = req.body.deviceID;
	var memo = req.body.memo;
	console.log(req);
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//data = "id=" + id + " and " + "pass=" + pwd;
		data = [memo, id];
		var exec = conn.query('update device set memo=? where id=?', data, function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				// console.log(result.length);
				// console.log(result);	
				// if(result.length == 1){
				// 	res.send({status:true});
				// }
				// else{
				// 	res.send({status:false});
				// }
				// update는 별다른 result가 없음
				res.send({ status: true });
			}
		});



	});
});


router.get('/getgroupinfo', function (req, res) {
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//	data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select * from groupinfo',
			function (err, result) {
				conn.release();
				res.header("Access-Control-Allow-Headers", "Authorization");
				res.header("Access-Control-Expose-Headers", "*");
				if (err) {
					res.send({ status: false });
				}
				else {
					console.log(result.length);
					console.log(result);
					if (result.length >= 1) {
						for (let i = 0; i < result.length; i++) {
							result[i].dNum = 0;
						}
						res.send({ status: true, payload: result });
					}
					else {
						res.send({ status: true, payload: null });
					}
				}
			});



	});
});

router.get('/dnumpergroup', function (req, res) {
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//	data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select count(*) as dnum,groupinfo.name as gName,groupinfo.id gID from groupinfo inner join groupregi where groupinfo.id=groupregi.groupid group by groupinfo.id',
			function (err, result) {
				conn.release();
				res.header("Access-Control-Allow-Headers", "Authorization");
				res.header("Access-Control-Expose-Headers", "*");
				if (err) {
					res.send({ status: false });
				}
				else {
					console.log(result.length);
					console.log(result);
					if (result.length >= 1) {
						res.send({ status: true, payload: result });
					}
					else {
						res.send({ status: true, payload: null });
					}
				}
			});



	});
});


router.get('/devicenum', function (req, res) {
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//	data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select count(id) dnum from device', function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				console.log(result.length);
				console.log(result);
				if (result.length == 1) {
					res.send({ status: true, payload: result[0] });
				}
				else {
					res.send({ status: false, payload: null });
				}
			}
		});



	});
});

router.get('/groupnum', function (req, res) {
	//var id = req.body.adminid;
	//var pwd = req.body.adminpwd;

	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//data = {id:id,pass:pwd};
		//	data = "id=" + id + " and " + "pass=" + pwd;
		//	data = [id, pwd];
		// MySQL에서, 서버에 projectinfo가 있을 시, true를 return한다. 
		var exec = conn.query('select count(id) gnum from groupinfo', function (err, result) {
			conn.release();
			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false });
			}
			else {
				console.log(result.length);
				console.log(result);
				if (result.length == 1) {
					res.send({ status: true, payload: result[0] });
				}
				else {
					res.send({ status: false, payload: null });
				}
			}
		});



	});
});

router.get('/', function (req, res) {

	var key = req.query.pageinfo;
	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		//			key가 2라면, 2번째 페이지. 페이지당 20개니까,
		//(key-1)*20 ~ 20
		var startindex = (key - 1) * 20;
		var exec = conn.query('select * from device order by time desc limit ?,20', startindex, function (err, result) {
			conn.release();

			res.header("Access-Control-Allow-Headers", "Authorization");
			res.header("Access-Control-Expose-Headers", "*");
			if (err) {
				res.send({ status: false, payload: null });
			}
			else {
				if (result.length) {
					res.send({ status: true, payload: result });
				}
				else {
					res.send({ status: true, paylod: null });
				}
			}

		});

	});

});

// /device/info/devicenum get



module.exports = router;
