// 필요라이브러리, Redis / MySQL
var pool = require('./setting/mysql_create')
var client = require('./setting/redis')

// 먼저 device의 개수와, group의 개수를 가져온다.
BackuptoObject = function(){
    // 먼저 device의 개수와, group의 개수를 가져온다.

    client.keys('*',function(err,result){
        console.log(result);
    });
    
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
