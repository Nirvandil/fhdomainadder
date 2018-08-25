import Vue from 'vue'
import Router from 'vue-router'
import Adding from '../components/Adding'
import deleting from '../components/Deleting'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Adding',
      component: Adding,
      meta: {title: 'Добавление доменов'}
    },
    {
      path: '/deleting',
      name: 'deleting',
      component: deleting,
      meta: {title: 'Удаление доменов'}
    }
  ]
})
