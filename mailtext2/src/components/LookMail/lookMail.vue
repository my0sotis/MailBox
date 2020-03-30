<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/recieveMail' }">收件箱</el-breadcrumb-item>
      <el-breadcrumb-item>邮件</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- <div>
        <h3>邮件主题</h3>
      </div> -->

      <!-- 收件人 主题 区域 -->
      <el-form
        :model="recieveMail_form"
        class="recieveMai-container"
        label-position="left"
        label-width="0px"
      >
        <!-- 三个比较重要的信息 -->
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input v-model="recieveMail_form.send_P">
              <template slot="prepend">发送:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input v-model="recieveMail_form.title">
              <template slot="prepend">主题:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input v-model="recieveMail_form.time">
              <template slot="prepend">时间:</template>
            </el-input>
          </el-col>
        </el-row>

        <!-- 主要的内容 -->
        <el-input
          class="contentMail"
          type="textarea"
          :rows="10"
          placeholder="邮件内容"
          v-model="recieveMail_form.textarea"
        ></el-input>

        <!-- 最后的三个按钮 -->
        <el-row :gutter="10" class="bottom_btns">
          <el-col :span="2">
            <el-button type="primary" round @click="reWrite_btn">回复</el-button>
          </el-col>
          <el-col :span="3">
            <el-button round @click="storeRubbish_btn">放入垃圾箱</el-button>
          </el-col>
          <el-col :span="2">
            <el-button round @click="return_btn" class="R_btn">返回</el-button>
          </el-col>
        </el-row>
        
      </el-form>
    </el-card>
  </div>
</template>>

<script>
export default {
  data() {
    return {
      recieveMail_form: {
        send_P: "",
        title: "",
        time: "",
        textarea: ""
      }
    };
  },
  created() {},
  methods: {
    reWrite_btn() { //回复

    },
    storeRubbish_btn() {    //放入垃圾箱


    },
    return_btn() {      //返回收件箱
        this.$router.replace('/recieveMail')
    }
  }
};
</script>>

<style scoped>
.contentMail {
  margin-top: 10px;
}
.bottom_btns{
    margin-top: 10px;
}

</style>>