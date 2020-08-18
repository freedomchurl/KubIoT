<template>
    <div v-bind:style="fullh">
        <ul>
            <li  v-on:mouseover = "changebgcolor(index)" v-on:mouseout = "originalcolor(index)" 
            v-on:click="menuselect(index)" v-for="(menu,index) in menuL" v-bind:key="index" v-bind:style="hoverbg[index]">
                <img :class="iconimage" v-bind:src=menu.path > <!--이미지는 다음과 같이 가져와야함-->
                {{menu.des}}
            </li>
        </ul>
    </div>
</template>


<script>
import {EventBus} from '../../utils/event-bus'
//import {EventBus2} from '../../utils/event-bus'


var sidebar_basic = "#ebeced";
var sidebar_hover = "#a3a3a3";

//const routespath = ['/list','/analytic','/group','/admin'];

export default {
    data(){
        return{
            //fullh:'heigth:100%',
            menulist:
            ['장치리스트','수집 데이터 분석','장치 그룹관리','관리자 메뉴'],
            menuL:
            [{des:'장치리스트',path:require('../../../static/img/smartwatch.png')},
            {des:'수집 데이터 분석',path:require('../../../static/img/graph.png')},
            {des:'장치 그룹관리',path:require('../../../static/img/group.png')},
            {des:'관리자 메뉴',path:require('../../../static/img/admin.png')}],
            iconimage:'imageclass',
            hoverbg:[
                {backgroundColor:sidebar_basic},
                {backgroundColor:sidebar_basic},
                {backgroundColor:sidebar_basic},
                {backgroundColor:sidebar_basic}
            ]

        }
    },
    methods:{
        menuselect(input){
            //this.$emit('menuselect',input); // index를 넘겨준다.
            //console.log('111123131');
            //EventBus2.$emit("content-name-show",this.menulist[input]);
            EventBus.$emit("click-sidemenu",input,this.menulist[input]); // index를 넘겨준다.
            if(input == 0)
                EventBus.$emit("update-list");
            
        },
        changebgcolor(input){
            this.hoverbg[input].backgroundColor =sidebar_hover;
        },
        originalcolor(input){
            this.hoverbg[input].backgroundColor = sidebar_basic;
        }
    }
}
</script>


<style scoped>
    div{
        color:black;
        /* display: inline-block; */
        background-color: #ebeced;
        float:left;
        /* display:flex; */
        width:13rem;
        height: 200vh;
        text-align: left;
        /* 아래 두 줄은, 가로 스크롤바를 넣기 위한 방안 */
        white-space:nowrap;
        overflow-x:scroll;
        box-shadow: 5px 5px 5px 5px #e2e2e2;
    
    }
    ul{
        list-style:none;
        padding-left:0px;
    }
    .imageclass{
        width:20%;
        height: 20%;
        padding-right: 1rem;
    }
    li{
        padding-top:1rem;
        padding-left:1rem;
        padding-bottom: 1rem;
    }

</style>