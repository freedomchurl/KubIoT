<template>
    <div id="app">
    <!-- <InitHeader></InitHeader>
    <InitForm></InitForm> -->
    <!-- <InitFooter></InitFooter>-->
    <!-- <button>변경</button> -->
    <!-- <p>
    <router-link to="/a">Login</router-link>
    <router-link to="/b">Main</router-link> 
    </p>
    <router-view></router-view> -->
    <router-view :key="$route.fullPath"></router-view>
    </div>
</template>


<script>
// import InitialHeaderVue from './components/initpage/InitialHeader.vue'
// import InitialFormVue from './components/initpage/InitialForm.vue'
//import InitialFooterVue from './components/initpage/InitialFooter.vue'

//import MainDashboard from './components/mainpage/MainDashboard.vue'

import axios from 'axios'
export default{
  created(){
    console.log("Created Phaze");
    axios.get('http://49.50.174.246:7676/project/projectinfo/get').then(res => { console.log(res.data) 
           // 현재, 프로젝트가 존재하는지 알아본다.
            
            if(res.data.status == true)
            {
              //this.$data.projectname2 = 'aaa';
              console.log("why?");
              //var projectname = res.data.payload;
                //this.$router.push({path:this.$route.query.redirect || '/loginProject',params: {name: 'dog', age:4}}) // project가 있는 경우
              //this.$router.push({name:'login'}); // ,params: {projectname: res.data.payload.projectname}}) // project가 있는 경우
              //this.$router.push(this.$router.query.redirect || '/login',params: {projectname: res.data.payload.projectname}}); // 이 경우만, 새로고침에 문제가없다.
              //this.$router.push(this.$route.query.redirect || '/loginProject');
              //console.log(this.$router.query.redirect || '/loginProject')
              //this.$router.push({name:'login',query:{redirect:'loginProject'}})
              this.$router.push('/loginProject');
            }
            else{
              console.log("Cant");
              this.$router.push({name:'init'}) // project가 있는 경우
              //this.$router.push(this.$router.query.redirect || '/initProject')
            }
        })
    // this.$router.push(this.$route.query.redirect || '/initProject'); -> Redirect 하는 부분
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
  body{
  text-align:center;
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