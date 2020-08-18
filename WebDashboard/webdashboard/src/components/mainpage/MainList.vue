<template>
<div>
    <div id="menuname">장치 리스트</div>
    <hr>
    <span id="flexicon">
            <img v-on:click="clicklist" id="listbutton" src="../../../static/img/list.png">
            <img v-on:click="clickcard" id="listcard" src="../../../static/img/cardview.png">
        </span>
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
            dataset:[]
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
div{
    width:inherit;
    white-space:nowrap;
    overflow-x:scroll; 
}
#menuname{
        /* float:inline-start; */
        display:flex;
        font-weight: bold;
        font-size:1.8rem;
        /* width: inherit;
        height:inherit; */
        text-align: left;
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
    hr{
        /* display:flex; */
        /* align-content: left; */
        border-top:1px solid grey;
        width: 30vw;
        /* background: yellow; */
        margin:1.0rem 1.5rem 1.5rem 0.5rem;
        /* padding:0rem 1.5rem 0.5rem 1.5rem; */
    }
</style>