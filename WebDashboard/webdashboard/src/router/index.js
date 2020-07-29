import Vue from 'vue'
import VueRouter from 'vue-router'

import InitialHeaderVue from '../components/initpage/InitialHeader.vue'
import InitialFormVue from '../components/initpage/InitialForm.vue'


Vue.use(VueRouter)


const route = [
    {
      path:'/a',
      component:InitialHeaderVue
    },
    {
      path:'/b',
      component:InitialFormVue
    }
];


const router = new VueRouter({
    routes:route
})

export default router