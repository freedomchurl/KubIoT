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
      path:'/loginProject',
      component:LoginPage
    },
    {
      path:'/main',
      component:MainPage,
      children:[
        {
          path:'list',
          component:MenuList
        },
        {
          path:'admin',
          component:MenuManager
        },
        {
          path:'analytic',
          component:MenuAnalytic
        },
        {
          path:'group',
          component:MenuGroup
        }
      ]
    },
    {
      path:'/initProject',
      component:InitialPage
    }
];


const router = new VueRouter({
    routes:route
})

export default router