<template>
  <div id="top">
    <div id="menuname">
      <span id="title">장치 그룹 관리</span>
    </div>
    <div id="current-state">
      <div id="total-device">
        <div id="state-name">총 장치 수</div>
        <div id="state-value">
          {{totalDevice}}
          <span id="amount">대</span>
        </div>
      </div>
      <div class="vl"></div>
      <div id="total-group">
        <div id="state-name">총 그룹 수</div>
        <div id="state-value">
          {{totalGroup}}
          <span id="amount">그룹</span>
        </div>
      </div>
      <div class="vl"></div>
      <div id="abnormal-device">
        <div id="state-name">이상 감지 장치 수</div>
        <div id="state-value">
          {{abnormalDevice}}
          <span id="amount">대</span>
        </div>
      </div>
    </div>
    <div class="row" id="adminbox">
      <div id="group-button">
        <!-- 그룹 관리 icon -->
        <span class="col-sm-2 col-sm-offset-5">
          <button v-bind:class="primebtn" @click="addGroup">그룹 추가</button>
        </span>
      </div>
    </div>

    <div id="group-card-box">
      <div
        class="group-box"
        v-on:click="groupselect(index)"
        v-for="(group,index) in groups"
        v-bind:key="index"
      >
        <div id="group-name">{{group.name}}</div>
        <div id="text-device-num">장치 수 : {{group.dNum}}</div>
      </div>
    </div>
  </div>
</template>

<script>
//import { EventBus } from "../../utils/event-bus.js";
//import router from '../../router/index.js'
//const routespath = ['/list','/analytic','/group','/admin'];
import axios from "axios";
import IP from "../../../static/IP";
import AddGroupModalVue from './Modal/AddGroupModal.vue';

export default {
  data() {
    return {
      totalDevice: "",
      totalGroup: "",
      abnormalDevice: "",
      primebtn: "btn btn-primary disabled btn-block",
      groups: [], //{gID:0,gName:'그룹 A',dNum:3},{gID:1,gName:'그룹 B',dNum:10},{gID:2,gName:'그룹 C',dNum:4},{gID:3,gName:'그룹 D',dNum:2}],
    };
  },
  props: ["propsdata"],
  methods: {
    addGroup() {
      console.log("그룹추가");
      this.doc_del_rendar()
    },
    LoadData(){
      var vm = this;

    axios
      .get("http://" + IP.IP + ":7878/push/message/getpushnum")
      .then((res) => {
        console.log(res.data);

        //   vm.totalGroup = res.data.payload.gnum;
        if (res.data.status == true) {
          vm.abnormalDevice = res.data.payload.pushnum;
        }
      });

    axios.get("http://" + IP.IP + ":7676/device/info/devicenum").then((res) => {
      console.log(res.data);

      vm.totalDevice = res.data.payload.dnum;
    });
    axios.get("http://" + IP.IP + ":7676/device/info/groupnum").then((res) => {
      console.log(res.data);

      vm.totalGroup = res.data.payload.gnum;

      axios
        .get("http://" + IP.IP + ":7676/device/info/getgroupinfo")
        .then((res) => {
          console.log(res.data);

          //vm.totalGroup = res.data.payload.gnum;

          if (res.data.payload == null) console.log("지민바보");

          vm.groups = res.data.payload;

          axios
            .get("http://" + IP.IP + ":7676/device/info/dnumpergroup")
            .then((res) => {
              console.log(res.data);

              //vm.totalGroup = res.data.payload.gnum;

              if (res.data.payload == null) console.log("지민바보");

              if (res.data.payload != null) {
                for (let i = 0; i < res.data.payload.length; i++) {
                  console.log(res.data.payload[i].gID-1);
                  for(let j = 0;j<vm.groups.length;j++){
                    console.log('j-th : ' + vm.groups[j].gID);
                    if(vm.groups[j].id == res.data.payload[i].gID)
                      vm.groups[j].dNum = res.data.payload[i].dnum;
                  }
                }
              }
            });
        });
    });
    },
    doc_del_rendar() {
      this.$modal.show(
        AddGroupModalVue,
        {
          //device_info: this.dataset[index],
          modal: this.$modal,
        },
        {
          name: "dynamic-modal",
          width: "800px",
          height: "600px",
          draggable: false,
        },
        {
          closed: this.testFN,
          "before-close": this.testFN2,
        }
      );
    },
    testFN(){
      console.log("WOW!!!! HARD - finish");
    },
    testFN2(){
      console.log("WOW!!!! HARD");
      this.LoadData();
    },
    groupselect(index) {
      console.log(index);
      //var vm= this;
      //EventBus.$emit("click-sidemenu",5,'','',vm.groups[index]);
      this.$router.push({name:'groupdetail',params:{gInfo: this.groups[index]}}).catch(()=>{});
      //router.push({name:routesname[index],params:{admin_id:ID}}).catch(()=>{});
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
    // 그룹별 device의 수를 가져오는 역할

    // EventBus.$on("update-list", function () {
    //   console.log("aaaprint");
    // });
  },
  // created(){
  //     console.log('Test here');
  //     var vm = this;
  //     EventBus.$on("click-sidemenu",function(index,name){
  //         console.log("Event!!2" + name);
  //         //this.currentindex = index;
  //         vm.menuname = name;
  //         //this.setName(name);
  //         //router.push('/main' + routespath[index]);
  //         router.push('/main' + routespath[index]).catch(()=>{});
  //         // Redirect 가 안됨.
  //         //this.selectContent(index);
  //     });
  // },
};
</script>

<style scoped>
#text-device-num {
  text-align: right;
  height: 30%;
  font-size: 1.3rem;
  font-weight: bold;
  padding-right: 30px;
  color: rgb(85, 107, 122);
}
#group-name {
  color: rgb(85, 107, 122);
  font-size: 2.5rem;
  font-weight: bold;
  text-align: left;
  height: 70%;
  padding-left: 30px;
  padding-top: 20px;
}
#group-card-box {
  margin: 2rem 2rem 2rem 2rem;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
}
.group-box {
  margin: 1rem 1rem 1rem 1rem;
  background-color: rgb(224, 224, 224);
  height: 200px;
  width: 450px;
  color: black;
  /* border: 1px solid rgb(85, 107, 122); */
  /* box-shadow: 5px 10px 2px rgb(85, 107, 122); */
}
#adminbox {
  display: flex;
  justify-content: flex-end;
  margin-right: 3rem;
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