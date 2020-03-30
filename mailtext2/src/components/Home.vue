<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <img src="../assets/font1.png" alt />
      </div>
      <el-button  round @click="quitLogin">返回登陆</el-button>
    </el-header>
    <!-- 页面的主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse? '64px' : '200px'">
        <!-- 扩展收起侧边栏按钮 -->
        <div class="toggle-btn" @click="toggle_click">|||</div>

        <el-menu  router default-active="activePath" :collapse-transition="false" :collapse="isCollapse" class="el-menu-vertical-demo">
          <!-- <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">{{ item.navItem }}</el-menu-item>
          -->
          <el-menu-item index="/sendMail" @click="'/'+saveNavState(sendMail)">
            <i class="el-icon-menu"></i>
            <span slot="title">发送邮件</span>
          </el-menu-item>
          <el-menu-item index="/recieveMail" @click="'/'+saveNavState(recieveMail)">
            <i class="el-icon-menu"></i>
            <span slot="title">收件箱</span>
          </el-menu-item>
          <el-menu-item index="/draftMail" @click="'/'+saveNavState(draftMail)">
            <i class="el-icon-menu"></i>
            <span slot="title">草稿箱</span>
          </el-menu-item>
          <el-menu-item index="/al_send" @click="'/'+saveNavState(recieal_sendveMail)">
            <i class="el-icon-menu"></i>
            <span slot="title">已发送</span>
          </el-menu-item>
          <el-menu-item index="/rubbishMail" @click="'/'+saveNavState(rubbishMail)">
            <i class="el-icon-menu"></i>
            <span slot="title">垃圾邮件</span>
          </el-menu-item>
          <el-menu-item index="/lookMail" @click="'/'+saveNavState(lookMail)">
            <i class="el-icon-menu"></i>
            <span slot="title">查看邮件内容(暂时)</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 右侧主题 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      //折叠侧边栏
      isCollapse:false,
      activePath:'',
    };
  },
  created() {
    this.activePath=window.sessionStorage.getItem('activePath')

    //获取左侧的菜单
    // this.getMenulList();
  },
  methods: {
    //返回登陆界面
    quitLogin() {
      window.sessionStorage.clear();
      this.$router.push("/login");
    },

    //侧边栏切放的按钮
    toggle_click() {
      this.isCollapse = !this.isCollapse
    },

    //保存激活的状态
    saveNavState(activePath){
      window.sessionStorage.setItem('activePath',activePath)
      this.activePath=activePath
    }

  }
};
</script>
<style scoped>
.home-container {
  height: 100%;
}
.el-header {
  /* background-color:rgb(243, 172, 40); */
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: rgb(252, 252, 252);
  font-size: 20px;
}
.el-aside {
  /* //background-color:rgb(243, 172, 40); */
}
.el-main {
  background-color: rgb(236, 238, 236);
}
.toggle-btn {
  background-color: aliceblue;
  font-size: 15px;
  line-height: 24px;
  color: black;
  text-align: center;
}
</style>
