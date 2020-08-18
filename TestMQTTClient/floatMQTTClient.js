const cluster = require('cluster');
const mqtt = require('mqtt');
var cron = require('node-cron');
const fs = require('fs');
const util = require('util');
const readFile = util.promisify(fs.readFile);

const ID = "MQTTF01" // 디바이스 아이디
const TYPE = "float" // 디바이스 데이터 형식
const TOPIC = "topic" // 서버 토픽
var regi = "0"     // 등록 상태
var groupList = []




var client  = mqtt.connect('mqtt://101.101.219.90');
const options = {
    host: '101.101.219.90',
    port: 1883,
    protocol: 'mqtts',
    //username:"root",
    //password:"password",
};

client.subscribe('topic'+ID, 0) // 내 아이디 MQTT 구독

client.on("connect", () => {
      console.log("Connected To MQTT Broker")
      console.log("\nI am "+ID);
    });

client.on('message', (topic, message, packet) => {
  // 메시지를 받았을 때.
  message = message.toString("utf-8")
  message = JSON.parse(message)
  // 등록 정보 수정
  if(message.regi == "1"){
    console.log("register")
    regi = "1";
  }
  // 그룹 등록
  if(message.status == "1"){
    console.log("status 1")
    var groupName = message.name
    // 그룹 채널 가입
    console.log("subscirbe topic"+ID)
    client.subscribe('topic'+ID, 0)
    // 그룹 추가
    groupList.append(name);
  }
  // 제어 실행.
  control(message)
})

cron.schedule('*/2 * * * * *', function(){
  var data = Math.random()
  var msg = {"deviceId":ID, "type":TYPE,"regi":regi,"data":[data], "time":[new Date()]}
  var msg_json = JSON.stringify(msg);
  client.publish(TOPIC, msg_json.toString())
})


function control(message){
  // 제어 쿼리를 이용한 제어 행동
  console.log(message);
}
