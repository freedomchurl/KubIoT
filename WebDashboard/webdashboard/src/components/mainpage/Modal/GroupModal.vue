<template>
    <div id="scroll-box" class="container">
        <div id="title-box">그룹 제어 전송</div>
    
        <div id="device-control-box">
            <span>제어 쿼리 :</span>
            <textarea v-model="control_data"></textarea>
        </div>
        <input id="control-btn" class="btn btn-primary col-md-3" @click="send_control" type="button" value="제어 전송">
    
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

export default {
    
  data:function(){
      return {
          groupinfo:this.group_info,
          control_data:'',
        //   device:this.device_info,
        //   device_memo:this.device_info.memo,
        //   place_device:this.device_info.memo,
      }
  },props : [
      'group_info',
  ],methods : {
      send_control(){
          var vm = this;
          //JSON {deviceId, protocol, request} 모두 String 타입.
          console.log(vm.groupinfo.id + ' ' + vm.control_data);
          axios.post("http://" + IP.IP + ":8083/group-control", {
              groupId:vm.groupinfo.id , request:vm.control_data,
            })
            .then((res) => {
                console.log(res);
              console.log(res.status + 'aaa');
              if(res.status==200)
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
    height: 200px;
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
</style>