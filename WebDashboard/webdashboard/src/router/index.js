import Vue from 'vue'
import VueRouter from 'vue-router'

// import InitialHeaderVue from '../components/initpage/InitialHeader.vue'
// import InitialFormVue from '../components/initpage/InitialForm.vue'

import InitialPage from '../components/initpage/InitialPage.vue'
import MainPage from '../components/mainpage/MainDashboard.vue'
import LoginPage from '../components/loginpage/loginPage.vue'

import MenuList from '../components/mainpage/MainList.vue'
import MenuManager from '../components/mainpage/MainManager.vue'
import MenuAnalytic from '../components/mainpage/MainAnalytic.vue'
import MenuGroup from '../components/mainpage/MainGroup.vue'
//import indexPage from '../App.vue'
import MenuPush from '../components/mainpage/MainPush.vue'

Vue.use(VueRouter)


const route = [
    // {
    //   path:'/a',
    //   //component:InitialHeaderVue
    // },
    // {
    //   path:'/b',
    //   //com`1ponent:InitialFormVue
    // },
    {
      path:'/',
      component:this
    },
    {
      path:'/loginProject',
      component:LoginPage,
      name:'login'
    },
    {
      path:'/main',
      component:MainPage,
      name:'main',
      props:true,
      children:[
        {
          path:'list',
          name:'list',
          component:MenuList
        },
        {
          path:'admin',
          component:MenuManager,
          name:'admin',
          props:true,
        },
        {
          path:'analytic',
          name:'analytic',
          component:MenuAnalytic
        },
        {
          path:'group',
          name:'group',
          component:MenuGroup
        },
        {
          path:'push',
          name:'push',
          component:MenuPush
        },
        
      ]
    },
    {
      path:'/initProject',
      component:InitialPage,
      name:'init'
    }
];


const router = new VueRouter({
  mode: 'history',
    routes:route
})

export default router