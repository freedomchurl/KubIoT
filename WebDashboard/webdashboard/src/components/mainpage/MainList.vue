<template>
<div id="top">
    <div id="menuname"><span id="title">장치 리스트</span></div>
    <div id="current-state">
        <div id="total-device">
            <div id="state-name">총 장치 수</div>
            <div id="state-value">{{totalDevice}}<span id="amount">대</span></div>
            </div><div class="vl"></div>
        <div id="total-group">
            <div id="state-name">총 그룹 수</div>
            <div id="state-value">{{totalGroup}}<span id="amount">그룹</span></div>
            </div><div class="vl"></div>
        <div id="abnormal-device">
            <div id="state-name">이상 감지 장치 수</div>
            <div id="state-value">{{abnormalDevice}}<span id="amount">대</span></div>
        </div>
    </div> 
    <div>
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
    <!-- <tr>
      <th scope="row">1</th>
      <td>Mark</td>
      <td>Otto</td>
      <td>@mdo</td>
      <td><button>ddd</button></td>
    </tr>
    <tr>
      <th scope="row">2</th>
      <td>Jacob</td>
      <td>Thornton</td>
      <td>@fat</td>
    </tr
    <tr>
      <th scope="row">3</th>
      <td colspan="2">Larry the Bird</td>
      <td>@twitter</td>
    </tr> -->
    <tr v-for="(data,index) in dataset" v-bind:key="index">
        <td>{{data.name}}</td>
        <td>{{data.memo}}</td>
        <td>{{data.location}}</td>
        <td>{{data.protocol}}</td>
        <td>{{data.type}}</td>
        <td>{{data.time}}</td>
        <td><button v-on:click="selectDevice(index)" class="btn btn-primary">관리</button></td>
    </tr>
  </tbody>
</table>
</div>
</div>
</template>

<script>
import {EventBus} from '../../utils/event-bus.js'
import axios from 'axios'
import IP from '../../../static/IP.json'
//import router from '../../router/index.js'
//const routespath = ['/list','/analytic','/group','/admin'];

export default {
    props:['propsdata'],
    data(){
        return{
            testDataset:[{deviceid:'지민아',memo:'철오빠가 사랑해',location:''},{deviceid:'지민아',memo:'많이많이 사랑해',location:''}
            ,{deviceid:'지민아',memo:'진짜진짜 사랑해',location:''},{deviceid:'지민아',memo:'오래오래 사랑해',location:''}],
            dataset:[],
            totalDevice:3,
            totalGroup:4,
            abnormalDevice:1,
        }
    },
    methods:{
        selectDevice(index){
            console.log(index + ' ' + 'device selected');
        },
        clicklist(){
            console.log('Click List');
            //EventBus.$emit("click-sidemenu",input,this.menulist[input]); // index를 넘겨준다.
        },
        clickcard(){
            console.log('Click Card');
        }
    },
    mounted(){
        console.log('Mounted');
    },
    created(){
        EventBus.$on("update-list",function(){
            console.log('aaaprint');
            axios.get('http://' + IP.IP+ ':7676/device/info', { params: { pageinfo: 1 }, 
        timeout: 1000 // 1초 이내에 응답이 없으면 에러 처리 
        }).then(res => { console.log(res.data) 
        
            this.dataset = res.data.payload;
        })
        })

        axios.get('http://' + IP.IP+ ':7676/device/info', { params: { pageinfo: 1 }, 
        timeout: 1000 // 1초 이내에 응답이 없으면 에러 처리 
        }).then(res => { console.log(res.data) 
        
            this.dataset = res.data.payload;
        })


        // totalDevice, totalGroup, abnormalDevice 를 가져와야함


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
}
</script>

<style scoped>
div#state-value{
    color: rgb(85,107,122);
    font-size: 3rem;
    font-weight: bold;
}
#state-name{
    color: rgb(85,107,122);
    text-align: left;
    font-weight: bold;
    margin-left: 1rem;
    margin-top:1rem;
}
.vl {
  border-left: 1px solid rgb(70, 70, 70);
  height: 80%;
}
div{
    width:inherit;
    white-space:nowrap;
    overflow-x:scroll; 
    padding: 0 0 0 0;
}
#total-device{
    flex: 1;
}
#total-group{
    flex: 1;
}
#abnormal-device{
flex: 1;
}
#current-state{
    background-color: rgb(190,190,190);
    height: 120px;
    display: flex; 
    flex-direction: row;
    align-items: center;
}
#title{
    margin-top: 25px;
    margin-left: 40px;
}
#menuname{
        /* float:inline-start; */
        display:flex;
        font-weight: bold;
        font-size:1.8rem;
        /* width: inherit;
        height:inherit; */
        text-align: left;
        height: 90px;
        color:rgb(85,107,122);
        background-color: rgb(224,224,224);
        padding:0rem 2rem 0rem 1rem; 
    }
span#flexicon{
        /* height: 3rem;; */
        display:flex;
        justify-content: flex-end;
        /* flex-flow: row-reverse; */
    }

    img#listbutton{
        width:2rem;
    }
    img#listcard{
        width:2rem;
    }
    img{
        margin: 0rem 1rem 1rem 0rem;
    }
span#amount{
    margin-left: 1rem;
    font-size: 1.2rem;
    margin-bottom: 1rem;
    color:rgb(85,107,122);
}
table{
    margin: 30px 0px 0px 0px;
}
</style>