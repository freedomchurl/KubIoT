<template>
  <div id="top">
    <!-- <DeviceModalVue/> -->
    <!-- <modals-container /> -->
    <div id="menuname">
      <span id="title">장치 그룹 관리 - 그룹 {{groupinfo.name}}</span>
    </div>
    <div id="current-state">
      <div id="total-device">
        <div id="state-name">그룹 내 장치 수</div>
        <div id="state-value">
          {{groupinfo.dNum}}
          <span id="amount">대</span>
        </div>
      </div>
      <!-- <div class="vl"></div>
      <div id="total-group">
        <div id="state-name">총 그룹 수</div>
        <div id="state-value">
          {{totalGroup}}
          <span id="amount">그룹</span>
        </div>
      </div> -->
      
    </div>
    <div>
        <div id="top-box"><div id="title-list">멤버 리스트</div>
        <div id="right-buttons">
        <button v-on:click="controlGroup" id="add-button" class="btn btn-primary">제어</button>
        <button v-on:click="addDevice" id="add-button" class="btn btn-primary">장치 추가</button></div></div>
      <table class="table table-hover">
        <thead>
          <tr>
            <th scope="col">장치 ID</th>
            <th scope="col">메모</th>
            <th scope="col">위치</th>
            <th scopr="col">프로토콜</th>
            <th scope="col">전송 타입</th>
            <th scope="col">등록 시간</th>
            <th scope="col">관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(data,index) in dataset" v-bind:key="index">
            <td>{{data.name}}</td>
            <td>{{data.memo}}</td>
            <td>{{data.location}}</td>
            <td>{{data.protocol}}</td>
            <td>{{data.type}}</td>
            <td>{{data.time}}</td>
            <td>
              <button v-on:click="deleteDevice(index)" class="btn btn-danger">제거</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    </div>
</template>

<script>
import { EventBus } from "../../utils/event-bus.js";
import axios from "axios";
import IP from "../../../static/IP.json";
import AddDeviceModal from "./Modal/AddDeviceModal.vue";
import GroupModal from './Modal/GroupModal.vue';

export default {
  components:{
    // 'DeviceModalVue':DeviceModal
  },
  props: ["gInfo"],
  data() {
    return {
        groupinfo:this.gInfo,
      testDataset: [
        { deviceid: "지민아", memo: "철오빠가 사랑해", location: "" },
        { deviceid: "지민아", memo: "많이많이 사랑해", location: "" },
        { deviceid: "지민아", memo: "진짜진짜 사랑해", location: "" },
        { deviceid: "지민아", memo: "오래오래 사랑해", location: "" },
      ],
      dataset: [],
      totalDevice: "",
      totalGroup: "",
      abnormalDevice: "",
    };
  },
  methods: {
    testFN3(){
      console.log("WOW!!!! HARD - finish");
    },
    testFN23(){
      console.log("WOW!!!! HARD");
      this.LoadData();
    },
    doc_del_rendar2() {
      
      this.$modal.show(
        GroupModal,
        {
          group_info: this.groupinfo,
          modal: this.$modal,
        },
        {
          name: "dynamic-modal",
          width: "800px",
          height: "600px",
          draggable: false,
        },{
          'closed':this.testFN3,
          'before-close':this.testFN23,
        }
      );
    },
    controlGroup(){
      this.doc_del_rendar2()
      // var vm = this;
      //     //JSON {deviceId, protocol, request} 모두 String 타입.
      //     axios.post("http://" + '192.168.0.29'+ ":8083/control", {
      //         groupId:vm.groupinfo.id , request:vm.control_data,
      //       })
      //       .then((res) => {
      //         console.log(res.status + 'aaa');
      //         if(res.status==200)
      //         {
      //             vm.$emit("close");
      //         }
      //       });
    },
    deleteDevice(index){
      var vm = this;
      axios
      .get("http://" + IP.IP + ":7676/device/info/deleteDeviceongroup",{
          params: { dID: vm.dataset[index].id , gID: vm.groupinfo.id},
          timeout: 10000, // 1초 이내에 응답이 없으면 에러 처리
        })
      .then((res) => {
        console.log(res.data);

        //   vm.totalGroup = res.data.payload.gnum;
        //if (res.data.status == true) {
          vm.LoadData(); // 재 로드.
        
      });
    },
    LoadData(){
      var vm = this;
    // axios
    //   .get("http://" + IP.IP + ":7878/push/message/getpushnum")
    //   .then((res) => {
    //     console.log(res.data);

    //     //   vm.totalGroup = res.data.payload.gnum;
    //     if (res.data.status == true) {
    //       vm.abnormalDevice = res.data.payload.pushnum;
    //     }
    //   });
    // axios.get("http://" + IP.IP + ":7676/device/info/devicenum").then((res) => {
    //   console.log(res.data);

    //   vm.totalDevice = res.data.payload.dnum;
    // });
    // axios.get("http://" + IP.IP + ":7676/device/info/groupnum").then((res) => {
    //   console.log(res.data);

    //   vm.totalGroup = res.data.payload.gnum;
    // });
    EventBus.$on("update-list", function () {
      console.log("aaaprint");
      axios
        .get("http://" + IP.IP + ":7676/device/info", {
          params: { pageinfo: 1 },
          timeout: 1000, // 1초 이내에 응답이 없으면 에러 처리
        })
        .then((res) => {
          console.log(res.data);

          this.dataset = res.data.payload;
        });
    });

    // axios
    //   .get("http://" + IP.IP + ":7676/device/info", {
    //     params: { pageinfo: 1 },
    //     timeout: 1000, // 1초 이내에 응답이 없으면 에러 처리
    //   })
    //   .then((res) => {
    //     console.log(res.data);

    //     this.dataset = res.data.payload;
    //   });

      axios
            .get("http://" + IP.IP + ":7676/device/info/dnumpergroup")
            .then((res) => {
              console.log(res.data);

              //vm.totalGroup = res.data.payload.gnum;

              if (res.data.payload == null) console.log("지민바보");

              if (res.data.payload != null) {
                for (let i = 0; i < res.data.payload.length; i++) {
                  //console.log(res.data.payload[i].gID-1);
                    if(vm.groupinfo.id == res.data.payload[i].gID)
                      vm.groupinfo.dNum = res.data.payload[i].dnum;
                  
                }
              }
            });

      axios.post("http://" + IP.IP + ":7676/device/info/getDeviceongroup", {
              gID:vm.groupinfo.id,
            })
            .then((res) => {
              console.log(res.data);
              
              if(res.data.status ==true)
              {
                vm.dataset = res.data.payload;
              }

            });
    // totalDevice, totalGroup, abnormalDevice 를 가져와야함
    },
    addDevice(){
        this.doc_del_rendar();
    },
    selectDevice(index) {
      console.log(index + " " + "device selected");
      this.doc_del_rendar(index);
    },
    testFN(){
      console.log("WOW!!!! HARD - finish");
    },
    testFN2(){
      console.log("WOW!!!! HARD");
      this.LoadData();
    },
    doc_del_rendar() {
      
      this.$modal.show(
        AddDeviceModal,
        {
          group_info: this.groupinfo,
          modal: this.$modal,
        },
        {
          name: "dynamic-modal",
          width: "800px",
          height: "600px",
          draggable: false,
        },{
          'closed':this.testFN,
          'before-close':this.testFN2,
        }
      );
    },
    clicklist() {
      console.log("Click List");
      //EventBus.$emit("click-sidemenu",input,this.menulist[input]); // index를 넘겨준다.
    },
    clickcard() {
      console.log("Click Card");
    },
  },
  mounted() {
    console.log("Mounted");
  },
  created() {
    this.LoadData();
  },
};


</script>

<style scoped>
#right-buttons{
  padding: 0.5rem 2rem 0.5rem 0rem;
  width:20%
}
#add-button{
    margin: 0rem 1rem 0rem 0rem;
    width: 50%;
}
#title-list{
    text-align: left;
    font-weight: bold;
    color:rgb(85, 107, 122);
    font-size: 1.5rem;
    margin:1rem 0rem 0.5rem 1rem ;
}
div#state-value {
  color: rgb(85, 107, 122);
  font-size: 3rem;
  font-weight: bold;
}
#state-name {
  color: rgb(85, 107, 122);
  text-align: left;
  font-weight: bold;
  margin-left: 1rem;
  margin-top: 1rem;
}
.vl {
  border-left: 1px solid rgb(70, 70, 70);
  height: 80%;
}
#top-box{
    display: flex;
    align-content: center;
    justify-content: space-between;
}
div {
  width: inherit;
  white-space: nowrap;
  overflow-x: scroll;
  padding: 0 0 0 0;
}
#total-device {
  flex: 1;
}
#total-group {
  flex: 1;
}
#abnormal-device {
  flex: 1;
}
#current-state {
  background-color: rgb(190, 190, 190);
  height: 120px;
  display: flex;
  flex-direction: row;
  align-items: center;
}
#title {
  margin-top: 25px;
  margin-left: 40px;
}
#menuname {
  /* float:inline-start; */
  display: flex;
  font-weight: bold;
  font-size: 1.8rem;
  /* width: inherit;
        height:inherit; */
  text-align: left;
  height: 90px;
  color: rgb(85, 107, 122);
  background-color: rgb(224, 224, 224);
  padding: 0rem 2rem 0rem 1rem;
}
span#flexicon {
  /* height: 3rem;; */
  display: flex;
  justify-content: flex-end;
  /* flex-flow: row-reverse; */
}

img#listbutton {
  width: 2rem;
}
img#listcard {
  width: 2rem;
}
img {
  margin: 0rem 1rem 1rem 0rem;
}
span#amount {
  margin-left: 1rem;
  font-size: 1.2rem;
  margin-bottom: 1rem;
  color: rgb(85, 107, 122);
}
table {
  margin: 30px 0px 0px 0px;
}
</style>