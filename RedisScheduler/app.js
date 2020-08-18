var Backup = require('./Redis_back.js')

const TIME = 30000; // 30분
var timeId = null;
function StartBack(){
    Backup();
    timeId = setInterval(Backup,TIME)
}

StartBack();