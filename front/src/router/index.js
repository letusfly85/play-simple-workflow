import Vue from 'vue'
import Router from 'vue-router'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import WorkflowEngine from '../components/WorkflowEngine'
import Item from '../components/Item'
import Order from '../components/Order'

Vue.use(Router)
Vue.use(BootstrapVue)

export default new Router({
  routes: [
    {
      path: '/workflow-engines',
      name: 'WorkflowEngine',
      component: WorkflowEngine
    },
    {
      path: '/items',
      name: 'Item',
      component: Item
    },
    {
      path: '/orders',
      name: 'Order',
      component: Order
    }
  ]
})
