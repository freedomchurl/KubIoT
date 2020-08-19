<template>
    <div id="scroll-box" class="container">
        <div id="title-box">장치 모니터링 및 범위 조절</div>
        <canvas id="mainCanvas"></canvas>
        
        <div id="device-control-box">
            <span>범위 조절 : </span>
            <input class="form-control" type="number" v-model="minvalue" id="min" placeholder="최솟값"><input class="form-control" v-model="maxvalue" type="number" placeholder="최댓값" id="max">
        </div>
        <input id="control-btn" class="btn btn-primary col-md-3" @click="send_control" type="button" value="범위 전송">
    
        <!-- <div class="input-group col-md-3">
            <span class = "input-group-addon">
                {{hot_table}}
            </span>
            <input type="text" v-model="del_password" class="form-control col-md-2">
        </div>
        <div class="row col-md-6"> 
            <input class="btn btn-default col-md-3" @click="del_data" type="button" value="삭제">
            <input class="btn btn-default col-md-3" @click="$emit('close')" type="button" value="취소"> -->
        <!-- </div> -->
    </div>
</template>
<script>
import axios from 'axios'
import IP from '../../../../static/IP'
import Chart from "chart.js";

let chartd_left = {
  type: "line",
  data: {
    labels:[1,2,3],
    datasets: [
      {
        label: "측정값",
        // data: [3, 4, 5, 4, 2],//, 1, 2, 3, 2, 1, 2, 3, 4, 2, 3],
        backgroudColor: 
          "rgba(54,73,93,.5)",
        borderColor: "#ff0000", 
        borderWidth: 0.5,
      }
    ],
  },
  options: {
    responsive: true,
    lineTension: 1,
    scales: {
      yAxes: [
        {
          ticks: {
            beginAtZero: false,
            padding: 25,
          },
        },
      ],
    },
  },
};

export default {
    
  data:function(){
      return {
          minvalue:'',
          maxvalue:'',
          deviceinfo:this.device_info,
          device:this.device_info,
          device_memo:this.device_info.memo,
          place_device:this.device_info.memo,
      }
  },props : [
      'device_info',
  ],
  created(){
      var vm = this;
          //JSON {deviceId, protocol, request} 모두 String 타입.
            //onsole.log(vm.device.name + ' ' + vm.device.protocol + ' ' + vm.control_data);
          axios.get("http://" + IP.IP+ ":7676/device/info/getliveData", {
          params: { dName:vm.deviceinfo.name},
          timeout: 10000, // 1초 이내에 응답이 없으면 에러 처리
        })
            .then((res) => {
                console.log(res);
              console.log(res.status + 'aaa');
              if(res.data.status==true)
              {
                  //vm.$emit("close");
                  console.log(res.data.payload);

                  //chartd_left.data.labels = Array.from(new Array(res.data.payload[0].length),(x,i)=>i+1)//[...Array(inputdata.length).keys()];
                chartd_left.data.labels = res.data.payload[1];
                chartd_left.data.datasets[0].data = res.data.payload[0];
                
                vm.createChart("mainCanvas", chartd_left);
              }
            });
      },
      methods : {
      createChart(charId, chartData) {
      const ctx = document.getElementById(charId);
      new Chart(ctx, {
        type: chartData.type,
        data: chartData.data,
        options: chartData.options,
      });
    },
      send_control(){
          var vm = this;
          //JSON {deviceId, protocol, request} 모두 String 타입.
            //onsole.log(vm.device.name + ' ' + vm.device.protocol + ' ' + vm.control_data);
          axios.get("http://" + IP.IP+ ":7676/device/info/control_device", {
          params: { min: vm.minvalue , max: vm.maxvalue,dName:vm.deviceinfo.name},
          timeout: 10000, // 1초 이내에 응답이 없으면 에러 처리
        })
            .then((res) => {
                console.log(res);
              console.log(res.status + 'aaa');
              if(res.data.status==true)
              {
                  vm.$emit("close");
              }
            });
      },
       modify_memo(){
            var vm = this;
            axios.post("http://" + IP.IP + ":7676/device/info/memochange", {
              deviceID:vm.device.id , memo:vm.device_memo,
            })
            .then((res) => {
              console.log(res.status + 'aaa');
              if(res.status==200)
              {
                  vm.$emit("close");
              }
            });

        },
      del_data(){
          this.$emit('close')
      }
  }
}
</script>
<style scoped>
#title-box{
    background-color: rgb(190,190,190);
    margin: 0;
    font-size: 1.2rem;
    font-weight: bold;
    padding:1rem 0rem 1rem 2rem;
    text-align: left;
    position: relative;;
    top:0px;
}
#min,#max{
    margin: 0rem 3rem 0rem 3rem;
    width: 30%;
}
#memobox{
    width: 100%;
    height: 200px;
    padding: 1rem 2rem 0 0;
    margin: 1rem 0rem 1rem 0rem;
    display:flex;
    align-items: center;
    
}
#device-control-box
{
    width: 100%;
    height: 40px;
    padding: 1rem 2rem 0 0;
    margin: 1rem 0rem 1rem 0rem;
    display:flex;
    align-items: center;
}
#control-btn{
    margin-bottom: 2rem;
}
span{
    width: 20%;
    margin-right: 1rem;
    font-size: 1.2rem;
    font-weight: bold;
}
textarea{
    width: 80%;
    height: 100%;
}
div#scroll-box{
    margin: 0 0 0 0;
    padding:0 0 0 0;
    /* padding: 1rem 2rem 0 0; */
    max-height: 600px;
    max-width: 800px;
    overflow-y:scroll;
    /* height: 600px; */
}

canvas{
  width: 100%;
  background-color: white;
  /* padding: 1rem 1rem 1rem 1rem; */
}

</style>