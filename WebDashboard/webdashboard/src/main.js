import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import router from './router/index.js'

Vue.config.productionTip = false

Vue.use(VueRouter)
// new VueRouter({
//   routes:[

//   ]
// // });
// var route = new VueRouter({
//   routes:,
//     {
//       path:'/',
//       component:App
//     }//
//   ]
// });

new Vue({
  render: h => h(App),
  router:router
}).$mount('#app')

//onsole.log('Problem');
