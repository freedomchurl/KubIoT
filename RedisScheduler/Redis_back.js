// 필요라이브러리, Redis / MySQL
var pool = require('./setting/mysql_create')
var client = require('./setting/redis')

// 먼저 device의 개수와, group의 개수를 가져온다.
BackuptoObject = function () {
    // 먼저 device의 개수와, group의 개수를 가져온다.

    client.keys('*', function (err, result) {
        console.log(result);
    });

    const AWS = require('aws-sdk');

    const endpoint = new AWS.Endpoint('https://kr.object.ncloudstorage.com');
    const region = 'kr-standard';
    const access_key = 'ltDXrFCuHyha8pmwqTcX';
    const secret_key = 'XFDdqJEASit4wHVFjCEEX6Yi2oD17ekIzCec2z41';

    const S3 = new AWS.S3({
        endpoint: endpoint,
        region: region,
        credentials: {
            accessKeyId: access_key,
            secretAccessKey: secret_key
        }
    });

    (async () => {

        let { Buckets } = await S3.listBuckets().promise();

        for (let bucket of Buckets) {
            console.log(bucket.Name);
        }

    })();
    // pool.getConnection(function(err,conn){
    //     if(err){
    //         if(conn){
    //             conn.release();
    //         }
    //         throw err;
    //     }
    //     else{
    //         var exec = conn.query('select *')
    //     }
    // })
}

module.exports = BackuptoObject;
