import Vue from 'vue'
import Router from 'vue-router'
// 导入刚才编写的组件
import Login from '@/components/Login'
import Home from '@/components/Home'
import Welcome from '../components/Welcome'
//导航栏的五个界面
import SendMail from '../components/SendMail/sendMail'
import RecieveMail from '../components/RecieveMail/recieveMail'
import DraftMail from '../components/DraftMail/draftMail'
import Al_send from '../components/Al_send/al_send'
import RubbishMail from '../components/RubbishMail/rubbishMail'


Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    //跳转到登录界面
    { path: '/', redirect: '/login' },
    //登陆的界面
    { path: '/login', name: 'Login', component: Login },
    //登陆成功后的主界面(导航栏)
    {
      path: '/home',
      name: 'Home',
      component: Home,
      redirect:'/welcome',
      children:[
        { path: '/welcome', name: 'Welcome', component: Welcome },

        { path: '/sendMail', name: 'SendMail', component: SendMail },
        { path: '/recieveMail', name: 'RecieveMail', component: RecieveMail },
        { path: '/draftMail', name: 'DraftMail', component: DraftMail },
        { path: '/al_send', name: 'Al_send', component: Al_send },
        { path: '/rubbishMail', name: 'RubbishMail', component: RubbishMail },
      ]
    }
  ]
})
