import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

//
export default new Vuex.Store({
    state: {
      user: {
        username: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).username
      },
      // username:'',
      // password:'',
    },
    //localStorage本地存储
    mutations: {
      // "set_username":function(state,username){
      //   state.username=username
      //   console.log("mutations",state.username)
      // },
      // "set_password":function(state,password){
      //   state.password=password
      //   console.log("mutations",state.password)
      // },


      login (state, user) {
        state.user = user
        window.localStorage.setItem('user', JSON.stringify(user))
      }
    },
    // getters:{
    //   "get_username":function(state){
    //     console.log("getter",state.username)
    //     return state.username
    //   },
    //   "get_password":function(state){
    //     console.log("getter",state.password)
    //     return state.password
    //   },
    // },
    // actions:{
    //   "set_username":function(state,username){
    //     console.log("action",state.username)
    //     state.commit("set_username",username)
    //   },
    //   "set_password":function(state,password){
    //     console.log("action",state.password)
    //     state.commit("set_password",password)
    //   },
    // }
})
