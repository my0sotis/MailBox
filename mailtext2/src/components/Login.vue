<template>
<body id="poster">
    <!-- 数据绑定：model  ref引用对象-->
    <el-form  :model="loginForm" :rules="rules" class="login-container" label-position="left"
             label-width="0px" ref="loginFormRef" >
      <h3 class="login_title">系统登录</h3>
      <el-form-item  prop="username">
        <el-input type="text" prefix-icon="el-icon-date" v-model="loginForm.username"
                  auto-complete="off" placeholder="账号" round></el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input type="password" prefix-icon="el-icon-goods" v-model="loginForm.password"
                  auto-complete="off" placeholder="密码" round></el-input>
      </el-form-item>

      <!-- 按钮区域 -->
      <el-form-item class="btns">
        <el-button type="primary" round v-on:click="login">登陆</el-button>
        <el-button type="info" round @click="resetloginForm">重置</el-button>
      </el-form-item>

      <!-- <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 100%;background: #505458;border: none" v-on:click="login">登录</el-button>
      </el-form-item> -->

    </el-form>
  </body>
</template>

// 登陆功能跳转都没做
<script>
import {mState} from 'vuex'
  export default {
    name: 'Login',
    data () {
      return {
        loginForm: {
          username: '',
          password: ''
        },
        //rule规则
        rules: {
          username:[
            { required: true, message: '请输入账号', trigger: 'blur' },
            { min: 10, max: 25, message: '长度不合理', trigger: 'blur' }
          ],
          password:[
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 3, max: 30, message: '长度不合理', trigger: 'blur' }
          ],
        },
        responseResult: []
      }
    },
    methods: {
      //登陆操作的函数
      login () {
        
        // //测试传输数据
        localStorage.setItem('login_username',this.loginForm.username)
        localStorage.setItem('login_password',this.loginForm.password)
        // this.$store.commit('set_username',this.loginForm.username)
        // console.log('login',this.loginForm.username)
        // this.bus.$emit("username",this.loginForm.username)
        // console.log(this.loginForm.username)
        // this.bus.$emit("password",this.loginForm.password)

        //测试 切换界面
        this.$router.push("/home"),

        this.$axios
          .post('/login', {
            username: this.loginForm.username,
            password: this.loginForm.password
          })
          .then(successResponse => {    
            //成功
            if (successResponse.data.code === 200) {
              this.$router.replace({path: '/index'})
            }else{
              //登陆失败
              this.$message.error('密码错误');
            }
          })
          .catch(failResponse => {  
          })
      },
      //重置表单函数
      resetloginForm(){
        this.$refs.loginFormRef.resetFields();
      }
    },
    // computed:mState({

    // })
  }
</script>

<style>
  #poster {
    background:url("../assets/LoginPhoto.jpg") no-repeat;
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
  }
  body{
    margin: 0px;
  }
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .btns {
    display: flex;
    justify-content: flex-end;
  }

</style>

