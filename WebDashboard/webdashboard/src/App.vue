<template>
    <div id="app">
    <InitHeader></InitHeader>
    <InitForm></InitForm>
    <!-- <InitFooter></InitFooter>-->
    <button>변경</button>
    <p>
    <router-link to="/a">Login</router-link>
    <router-link to="/b">Main</router-link> 
    </p>
    <router-view></router-view>
    </div>
</template>


<script>
import InitialHeaderVue from './components/initpage/InitialHeader.vue'
import InitialFormVue from './components/initpage/InitialForm.vue'
//import InitialFooterVue from './components/initpage/InitialFooter.vue'

   


export default{
  components:{
    'InitHeader':InitialHeaderVue,
    'InitForm':InitialFormVue
    //'InitFooter':InitialFooterVue
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