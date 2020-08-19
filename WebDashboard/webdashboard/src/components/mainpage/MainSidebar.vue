<template>
    <div v-bind:style="fullh">
        <img class="bus-image" v-bind:src="buspath"><span id="title">{{ProjectName}}</span>
        <hr id="hr-line">
        <ul>
            <li  v-on:mouseover = "changebgcolor(index)" v-on:mouseout = "originalcolor(index)" 
            v-on:click="menuselect(index)" v-for="(menu,index) in menuL" v-bind:key="index" v-bind:style="hoverbg[index]">
        
                <img :class="iconimage" v-bind:src=menu.path > <!--이미지는 다음과 같이 가져와야함-->
                <span id="content-text">{{menu.des}}</span>
            </li>
        </ul>
        <div id="footer">
            2020 Vaninside, 
            All rights reserved
        </div>
    </div>
</template>


<script>
import {EventBus} from '../../utils/event-bus'
//import {EventBus2} from '../../utils/event-bus'
import axios from 'axios'
import IP from '../../../static/IP'

var sidebar_basic = "rgb(42,63,84)";
var sidebar_hover = "#a3a3a3";

//const routespath = ['/list','/analytic','/group','/admin'];

export default {
    props:['id'],
    data(){
        return{
            ID:this.id,
            ProjectName:'',
            fullh:'heigth:100%',
            buspath:require('../../../static/img/bus.png'),
            menulist:
            ['장치리스트','수집 데이터 분석','장치 그룹관리','관리자 메뉴','PUSH 리스트'],
            menuL:
            [{des:'장치리스트',path:require('../../../static/img/list.png')},
            {des:'수집 데이터 분석',path:require('../../../static/img/graph.png')},
            {des:'장치 그룹관리',path:require('../../../static/img/group.png')},
            {des:'관리자 메뉴',path:require('../../../static/img/admin.png')},
            {des:'PUSH 리스트',path:require('../../../static/img/alert.png')}],
            iconimage:'imageclass',
            hoverbg:[
                {backgroundColor:sidebar_basic},
                {backgroundColor:sidebar_basic},
                {backgroundColor:sidebar_basic},
                {backgroundColor:sidebar_basic},{backgroundColor:sidebar_basic}
            ]

        }
    },
    methods:{
        menuselect(input){
            //this.$emit('menuselect',input); // index를 넘겨준다.
            //console.log('111123131');
            //EventBus2.$emit("content-name-show",this.menulist[input]);
            console.log("THIS EMIT : " + this.ID);
            EventBus.$emit("click-sidemenu",input,this.menulist[input],this.ID); // index를 넘겨준다.
            if(input == 0)
                EventBus.$emit("update-list");
            
        },
        changebgcolor(input){
            this.hoverbg[input].backgroundColor =sidebar_hover;
        },
        originalcolor(input){
            this.hoverbg[input].backgroundColor = sidebar_basic;
        }
    },
    created(){
        var vm = this;
        console.log(this.ID);
        console.log('Header Created');
        axios.get('http://' + IP.IP+ ':7676/project/projectinfo/get').then(res => { console.log(res.data)

            vm.ProjectName = res.data.payload.projectname;
       
        })
    },
}
</script>


<style scoped>
    .bus-image{
        display: inline-block;
        margin-top: 2rem;
        font-size: 3rem;
        height: 4rem;
        margin-left: 3rem;
        margin-bottom: 4rem;
    }
    html,body{
        position: absolute;
        height: 100%;
    }
    hr{
        border: solid 0.1px rgb(207, 206, 206);
        margin: 0 1px 0 0.1px; 
    }
    div{
        
        color:white;
        /* display: inline-block; */
        background-color: rgb(42,63,84);
        float:left;
        /* display:flex; */
        width:18rem;
        height: 200vh;
        text-align: left;
        /* 아래 두 줄은, 가로 스크롤바를 넣기 위한 방안 */
        white-space:nowrap;
        overflow-x:scroll;
        /* box-shadow: 1px 1px 1px 1px #051729; */
    
    }
    span#content-text{
        display: inline-block;
        text-align: left;
        font-size: 1.2rem;
        padding-top: 0rem;
    }
    #title{
        display: inline-block;
        margin-top: 10px;
        font-size: 3rem;
        height: 10px;
        margin-bottom: 1rem;
        color: rgb(224,224,224);
        text-align: center;
    }
    ul{
        margin-top:2rem;
        list-style:none;
        padding-left:0px;
    }
    .imageclass{
        width:50px;
        height:50px;
        margin-top: 0;
        padding-right: 0;
    }
    li{
        padding-top:0rem;
        padding-left:1rem;
        padding-bottom: 1rem;
    }
    #footer{
        padding: 0 0 0 0;
        text-align: center;
        margin-top: 10rem;
    }

</style>