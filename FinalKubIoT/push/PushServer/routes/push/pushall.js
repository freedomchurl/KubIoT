var express = require('express');
var router = express.Router();
var mysql = require('mysql');
var admin = require('firebase-admin');
var serviceAccount = require('./key.json');

admin.initializeApp({ credential: admin.credential.cert(serviceAccount) });

var pool = mysql.createPool({
	connectionLimit: 40,
	host: '101.101.219.90',
	user: 'root',
	password: 'dlcjf2779!',
	database: 'kubiot',
	debug: false,
	charset: "utf8"
});
router.use(express.json());

router.post('/', function (req, res) {
	console.log('Push');

	var message = req.body.message;
	var did = req.body.did;
	var payload = {
		data: { message }
	};

	pool.getConnection(function (err, conn) {
		if (err) {
			if (conn) {
				conn.release();
			}
			throw err;
		}
		console.log(did + ' ' + message);
		var exec = conn.query('insert into push_data(device) values(?)', did, function (err, result) {

			//conn.release();

			if (err) {
				res.send({ status: false });
			}
			else {
				//res.send({ status: true });
			


			var exec = conn.query('select  * from fcmtoken', function (err, result) {
				if (err) {
					res.send({ status: false });
				}
				else {
					var tokenList = [];

					for (var i = 0; i < result.length; i++) {
						if (result[i].fcmtoken != null)
							tokenList.push(result[i].fcmtoken);

					}

					admin.messaging().sendToDevice(tokenList, payload).then(function (response) {
						console.log('Successfully Sent message : ', response);
						res.send({ status: true });
					}).catch(function (err) {
						console.log('Error Sending Message :', err);
						res.send({ status: false });
					});

				}
			});
		}
		});



	});

});

module.exports = router;
