import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import router from './router/index.js'
import VModal from 'vue-js-modal'

Vue.config.productionTip = false

Vue.use(VueRouter)
Vue.use(VModal,{dynamic:true}) // Modal 이용

new Vue({
  render: h => h(App),
  router:router
}).$mount('#app')

//onsole.log('Problem');
