<template>
<div>
    <div id="menuname">관리자 메뉴</div>
    <hr>
    <div class="row justify-content-center" id="adminbox">
    <div class="col-sm-4 col-sm-offset-3">
        <label class="sr-only" for="adminid">Admin newpassword</label>
        <input v-bind:class="textinput" type="password" id="adminpwd" v-model="newAdminPass" title="managerpass" placeholder="새 관리자 비밀번호"><br>
        <label class="sr-only" for="adminpwd">Admin Password</label>
        <input v-bind:class="textinput" type="password" id="adminpwd_confirm" v-model="newAdminPass_confirm" title="managerpass_confirm" placeholder="새 관리자 비밀번호 확인"><br>
        <button v-bind:class="primebtn" @click="changePwd">관리자 비밀번호 변경</button> <!--얘가 여기 말고 다른 곳에 있을 경우, props / emit으로 데이터를 주고받아야함 --> 
    </div>
    </div>
</div>
</template>

<script>
import {EventBus} from '../../utils/event-bus.js'
//import router from '../../router/index.js'
//const routespath = ['/list','/analytic','/group','/admin'];

export default {
    props:['propsdata'],
    data(){
        return{
            newAdminPass:'',
            newAdminPass_confirm:'',
            primebtn:'btn btn-primary disabled btn-block',
            textinput:'form-control'
        }
    },
    methods:{
        changePwd(){
            console.log('비밀번호 변경');
        }
    },
    mounted(){
        console.log('Mounted');
    },
    crearted(){
        EventBus.$on("update-list",function(){
            console.log('aaaprint');
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
    input{
        /* margin:0em 0em 0.5em 0em; */
        display:flex;
        align-items: center;
    }
    #adminbox{
        margin:5rem 5rem 5rem 5rem;
    }
</style>