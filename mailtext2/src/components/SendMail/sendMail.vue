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
              <el-input type="text" prefix-icon="el-icon-message" placeholder="邮件地址" v-model="sendMail_form.recieve_m">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item  prop="mail_Title">
              <el-input type="text"  prefix-icon="el-icon-edit" placeholder="邮件主题内容" v-model="sendMail_form.mail_Title"></el-input>
            </el-form-item>
            </el-col>
        </el-row>
        <!-- 邮件的正式内容 -->
        <el-form-item  prop="textarea">
          <el-input type="textarea" :rows="10" placeholder="请输入内容" v-model="sendMail_form.textarea"></el-input>
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
      sendMail_form: {
        recieve_m: '', //收件人邮箱
        mail_Title: '', //邮箱的主题
        textarea: '' //邮件的内容
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
    };
  },
  created() {},
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