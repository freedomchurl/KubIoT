<template>
  <div id="top">
    <div id="menuname">
      <span id="title">이상 장치 알림 리스트</span>
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
    <div>
      <table class="table table-hover">
        <thead>
          <tr>
            <th scope="col">장치 ID</th>
            <th scope="col">발생 시간</th>
            <th scope="col">해결</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(data,index) in pushset" v-bind:key="index">
            <td id="push-did">{{data.dName}}</td>
            <td>{{data.time}}</td>
            <td id="check-field">
              <input
                type="checkbox"
                id="checkbox-push"
                v-on:change="checkSet(index)"
                v-model="data.ischecked"
              />
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
//import router from '../../router/index.js'
//const routespath = ['/list','/analytic','/group','/admin'];

export default {
  props: ["propsdata"],
  data() {
    return {
      testDataset: [
        { deviceid: "지민아", memo: "철오빠가 사랑해", location: "" },
        { deviceid: "지민아", memo: "많이많이 사랑해", location: "" },
        { deviceid: "지민아", memo: "진짜진짜 사랑해", location: "" },
        { deviceid: "지민아", memo: "오래오래 사랑해", location: "" },
      ],
      test_pushset: [
        {
          dID: 1,
          dName: "장치01",
          time: "2020년",
          ischecked: false,
          pushID: 1,
        },
      ],
      pushset: [], // dID(장치 key), dName(장치 ID , NAME) , time(발생 시간), ischecked ( true, 해결됨, false 해결 안됨), pushID(푸시key)
      totalDevice: "",
      totalGroup: "",
      abnormalDevice: "",
    };
  },
  methods: {
    checkSet(index) {
        var vm = this;
      console.log(index + " " + "push selected");
      var inputcheck = this.pushset[index].ischecked == true ? 1 : 0; // 
      axios
        .get("http://" + IP.IP + ":7878/push/message/checkpush",{
          params: { pushID: vm.pushset[index].pushID, ischecked:inputcheck },
          timeout: 3000, // 1초 이내에 응답이 없으면 에러 처리
        })
        .then((res) => {
          console.log(res.data);

          //   vm.totalGroup = res.data.payload.gnum;
          if (res.data.status == true) {
            //vm.abnormalDevice = res.data.payload.pushnum;
            console.log("Checked changed successfully");
          }
        });
    },
    selectDevice(index) {
      console.log(index + " " + "device selected");
    },
    clicklist() {
      console.log("Click List");
      //EventBus.$emit("click-sidemenu",input,this.menulist[input]); // index를 넘겨준다.
    },
    clickcard() {
      console.log("Click Card");
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
    });

    axios
      .get("http://" + IP.IP + ":7878/push/message/getpushlist")
      .then((res) => {
        console.log(res.data);

        //   vm.totalGroup = res.data.payload.gnum;
        if (res.data.status == true && res.data.payload != null) {
          for (var i = 0; i < res.data.payload.length; i++) {
            var tmp = res.data.payload[i];
            if (tmp.ischecked == 0) tmp.ischecked == false;
            // 미해결
            else tmp.ischecked == true; // 해결

            vm.pushset.push(tmp); // 추가한다.
          }
        }
      });

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

    axios
      .get("http://" + IP.IP + ":7676/device/info", {
        params: { pageinfo: 1 },
        timeout: 1000, // 1초 이내에 응답이 없으면 에러 처리
      })
      .then((res) => {
        console.log(res.data);

        this.dataset = res.data.payload;
      });

    // totalDevice, totalGroup, abnormalDevice 를 가져와야함
    }
  },
  mounted() {
    console.log("Mounted");
  },
  created() {
    this.LoadData();
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
table {
  width: 100%;
}
#check-field {
  width: 3rem;
  text-align: center;
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
#checkbox-push {
  margin-top: 5px;
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