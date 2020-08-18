<template>
    <div id="app">
    <router-view v-on:loginsuccess="routelogin"></router-view>
    </div>
</template>


<script>
import {EventBus} from './utils/event-bus.js'
import axios from 'axios'
import IP from '../static/IP.json'

export default{
  methods:{
    routelogin(){
      console.log("Login Router!!");
      this.$router.push('/main');
    }
  },
  created(){
    console.log("Created Phaze");
    var vm = this;
    EventBus.$on("gotomain",function(){
            console.log("Event!!" + name);
            vm.$router.push('/main').catch(()=>{});
            // Redirect 가 안됨.
            //this.selectContent(index);
        });

    axios.get('http://' + IP.IP+ ':7676/project/projectinfo/get').then(res => { console.log(res.data) 
           // 현재, 프로젝트가 존재하는지 알아본다.
            
            if(res.data.status == true)
            {
              console.log("why?");
              vm.$router.push({name:'login'});
            }
            else{
              console.log("Cant");
              this.$router.push({name:'init'}) // project가 있는 경우
              //this.$router.push(this.$router.query.redirect || '/initProject')
            }
        })
    //this.$router.push(this.$route.query.redirect || '/initProject'); -> Redirect 하는 부분
    //this.$router.push(this.$route.query.redirect || '/loginProject')
  },
  components:{
    // 'InitHeader':InitialHeaderVue,
    // 'InitForm':InitialFormVue,
    //'InitFooter':InitialFooterVue,
    //'MainDashboard':MainDashboard
    }
}
</script>

<style>
  html, body{
  text-align:center;
  height: 100%;
  /* background-color:#F6F6F8; */
  }
  input{
    border-style:groove;
    width:200px;
  }
  button{
    border-style:groove;
  }
  .shadow{
    box-shadow:5px 10px 10px rgba(0,0,0,0.3)
  }
</style>

<!--npm run serve -->