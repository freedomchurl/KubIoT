<template>
  <div id="login-box">
    <div id="left-box">
      <h2 id="title">Welcome to KubIoT!</h2>
      <p id="metioned">Sign up and Make your own project</p>
    </div>
    <div id="main-box">
      <InitHeader></InitHeader>
      <InitForm></InitForm>
    </div>
  </div>
</template>

<script>
import InitialHeaderVue from "./InitialHeader.vue";
import InitialFormVue from "./InitialForm.vue";
import axios from 'axios'
import IP from '../../../static/IP.json'

export default {
  beforeCreate(){
        var vm = this;
        axios.get('http://' + IP.IP+ ':7676/project/projectinfo/get').then(res => { console.log(res.data) 
           // 현재, 프로젝트가 존재하는지 알아본다.
            
            if(res.data.status == true)
            {
              console.log("Login Success");
              vm.$router.push({name:'login'});
            }
            else{
              console.log("Sign up Project");
              this.$router.push({name:'init'}) // project가 있는 경우
              //this.$router.push(this.$router.query.redirect || '/initProject')
            }
        })
    },
  components: {
    InitHeader: InitialHeaderVue,
    InitForm: InitialFormVue,
  },
};
</script>

<style scoped>
html,body{
        position: absolute;
        height: 100%;
        
    }
    h2,p{
        color:wheat;     
    }
    h2{
        margin-top: 50%;
    }
    #left-box{
        width: 40%;
        height: 100%;
        background-color: rgb(42, 63, 84);
    }
    #main-box{
        width: 60%;
    }
    #login-box{
        position:absolute;
        display: flex;
        height: 100%;
        width: 100%;
        align-content: space-between;
    }
</style>