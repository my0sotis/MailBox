<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>发送邮件</el-breadcrumb-item>
      <!-- <el-breadcrumb-item>活动列表</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
    </el-breadcrumb>

    <!-- 卡片区域 -->
    <el-card>
      <!-- <div slot="header" class="navigation">

      </div>-->
      <!-- 前三个按钮区域 -->
      <el-row :gutter="10" class="top_btns">
        <el-col :span="2">
          <el-button type="primary" round @click="send_btn">发送</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round @click="storeDraft_btn">草稿</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round @click="cancel_btn">取消</el-button>
        </el-col>
      </el-row>

      <!-- 收件人 主题 区域 -->
      <el-form :model="sendMail_form" :rules="rules"  class="senMail-container"  label-position="left"  label-width="0px">
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item  prop="recieve_m">
              <el-input type="text" prefix-icon="el-icon-message" placeholder="邮件地址" v-model="sendMail_form.reciever">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item  prop="mail_Title">
              <el-input type="text"  prefix-icon="el-icon-edit" placeholder="邮件主题内容" v-model="sendMail_form.theme"></el-input>
            </el-form-item>
            </el-col>
        </el-row>
        <!-- 邮件的正式内容 -->
        <el-form-item  prop="textarea">
          <el-input type="textarea" :rows="10" placeholder="请输入内容" v-model="sendMail_form.content"></el-input>
        </el-form-item>
        </el-form>

      <!-- 后两个按钮区域 -->
      <el-row :gutter="10" class="bottom_btns">
        <el-col :span="2">
          <el-button type="primary" round @click="send_btn">发送</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round @click="cancel_btn">取消</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>>

<script>
export default {
  data() {
    return {
      //修改了名称
      sendMail_form: {
        reciever: '', //收件人邮箱
        theme: '', //邮箱的主题
        content: '' //邮件的内容
      },
        //rule规则
        rules: {
          recieve_m:[
            { required: true,message: '请输入收件人地址', trigger: 'blur' },
          ],
          mail_Title:[
            { required: true, message: '请输入邮件主题', trigger: 'blur' },

          ],
        },
      replyNo : '',
    };
  },
  created() {
    //这部分是从查看邮件进行回复时跳转到的发送界面，回复邮件时自动把收件人和主题给补上，仅用于测试
    //正式版本在此使用axios从后台接收的邮件信息，传递的数据是replyNo，即邮件序号，返回的信息同测试版本一样
    if(this.$route.query != null){
      this.replyNo = this.$route.query["id"][0]
      this.sendMail_form["reciever"] = "876700669@qq.com"
      this.sendMail_form['theme'] = "回复："+"原邮件主题"
    }
    //此外还有转发界面也会带着邮件序号进行跳转，这部分需要单独处理一下，具体可以参考lookMail.vue中如何获取上一个界面的URL进行判断
    //所以不能仅仅通过query来判断是否补充相关内容，需要判断上一个界面上是否具有相关的转发或者回复功能来补充
    //上面代码考虑不周，需要改进
  },
  methods: {
    send_btn() {
      //发送邮件
    },
    storeDraft_btn() {
      //草稿箱
    },
    cancel_btn() {
      //取消
    }
  }
};
</script>>

<style scoped>
.el-row {
  margin-bottom: 10px;
}
.bottom_btns {
  margin-top: 10px;
}

.navigation {
  margin-top: 10px;
  align-items: center;
}
</style>>
