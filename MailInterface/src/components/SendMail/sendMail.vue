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
          <el-button type="primary" round @click="submit($event)">发送</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round @click="draft($event)">草稿</el-button>
        </el-col>
        <el-col :span="2">
          <el-button round @click="cancel_btn">取消</el-button>
        </el-col>
      </el-row>

      <!-- 收件人 主题 区域 -->
      <el-form
        :model="sendMail_form"
        :rules="rules"
        class="senMail-container"
        label-position="left"
        label-width="0px"
      >
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item prop="to">
              <el-input
                type="text"
                prefix-icon="el-icon-message"
                auto-complete="off"
                placeholder="邮件地址(多个收件人用';进行分割)"
                v-model="sendMail_form.to"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-form-item prop="subject">
              <el-input
                type="text"
                prefix-icon="el-icon-edit"
                auto-complete="off"
                placeholder="邮件主题内容"
                v-model="sendMail_form.subject"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 邮件的正式内容 -->
        <el-form-item prop="textarea">
          <el-input type="textarea" :rows="10" placeholder="请输入内容" v-model="sendMail_form.content"></el-input>
        </el-form-item>
      </el-form>

      <!-- <input type="file" @change="getFile($event)" multiple="multiple" />
      <button class="button button-primary button-pill button-small" @click="submit($event)">提交</button>-->

      <!-- 上传附件 -->
      <el-button
        icon="el-icon-document-copy"
        type="primary"
        round
        size="small"
        style="margin-left: 15px;"
        @click="checkFile"
      >选择文件</el-button>
      <span>{{fileName}}</span>
      <input
        type="file"
        id="fileinput"
        style="display: none;"
        @change="getFile($event)"
        multiple="multiple"
      />
      <!-- <div>
        <el-button
          class="submit_btn"
          icon="el-icon-upload2"
          type="primary"
          round
          size="small"
          style="margin-left: 15px;"
          @click="submit($event)"
        >提交</el-button>
      </div> -->

      <!-- 后两个按钮区域 -->
      <!--<el-row :gutter="10" class="bottom_btns">-->
        <!--<el-col :span="2">-->
          <!--<el-button type="primary" icon="el-icon-upload2"  round @click="submit($event)">发送</el-button>-->
        <!--</el-col>-->
        <!--<el-col :span="2">-->
          <!--<el-button round @click="cancel_btn">取消</el-button>-->
        <!--</el-col>-->
      <!--</el-row>-->
    </el-card>
  </div>
</template>>

<script>
export default {
  data() {
    return {
      fileName: "",
      //修改了名称
      sendMail_form: {
        username: "",
        password: "",
        cc: [""],
        bcc: [""],
        to: [""], //收件人邮箱
        subject: "", //邮箱的主题
        content: "",//邮件的内容
        attachments:[] //附件地址
      },
      //rule规则
      rules: {
        to: [
          { required: true, message: "请输入收件人地址", trigger: "blur" },
          { min: 10, max: 30, message: '长度不合理', trigger: 'blur' }
        ],
        subject: [
          { required: true, message: "请输入邮件主题", trigger: "blur" },
          { min: 3, max: 20, message: '长度不合理', trigger: 'blur' }
        ]
      },
      replyNo: "",
      tolist:[""],
      chooseNo: "", //邮件的编号
      //
      file: "",
      responseResult: []
    };
  },


  created() {
    console.log("loading.start");
    const rLoading = this.openLoading();
    if(this.$route.query["id"] == null){
      console.log("没有执行");
    }else{
      this.sendChooseNo();
    }
    rLoading.close();
    console.log("loading.close");
    //这部分是从查看邮件进行回复时跳转到的发送界面，回复邮件时自动把收件人和主题给补上，仅用于测试
    //正式版本在此使用axios从后台接收的邮件信息，传递的数据是replyNo，即邮件序号，返回的信息同测试版本一样
    // if (this.$route.query != null) {
    //   this.replyNo = this.$route.query["id"][0];
    //   this.sendMail_form["reciever"] = "876700669@qq.com";
    //   this.sendMail_form["theme"] = "回复：" + "原邮件主题";
    // }
    //此外还有转发界面也会带着邮件序号进行跳转，这部分需要单独处理一下，具体可以参考lookMail.vue中如何获取上一个界面的URL进行判断
    //所以不能仅仅通过query来判断是否补充相关内容，需要判断上一个界面上是否具有相关的转发或者回复功能来补充
    //上面代码考虑不周，需要改进

  },
  methods: {
    // handlefunc(){
    //   let str=this.sendMail_form.to
    //   console.log(str)
    //   let strlist=str.split(';')
    //   for(let i=0;strlist.length;i++){
    //     //this.sendMail_form.to
    //   }
    // },

    //读取本地json文件（用于测试，正式版本将从后台获取json文件)
    //获取当前界面的URL中的邮件序号
    sendChooseNo() {
      let chooseNo = this.$route.query["id"]; //取得的query['id']是数组
      let judge = this.$route.query["judge"];
      let type = this.$route.query["type"];
      this.chooseNo = chooseNo;
      var url = "";
      if(type === -1){
        url = "/transmit/"+this.chooseNo;
      }
      else{
        url = "/transmitPost/"+this.chooseNo+"&&"+type;
      }

      this.$axios.get(url).then(res => {
        if(res != null){
          if(judge === '1'){ //转发
            this.sendMail_form.subject = res.data["briefInfo"]["subject"];
            this.sendMail_form.content = res.data["content"];
            this.sendMail_form.attachments = res.data["attachments"]
          }
          else if(judge === "0"){ //回复
            console.log(res.data["briefInfo"]["senderEmail"]);
            this.sendMail_form.to = res.data["briefInfo"]["senderEmail"];
          }
        }
        else{
          alert('查看邮件失败');
        }
      });
    },
    // send_btn() {
    //   //首先把用户名密码输入进去
    //   this.sendMail_form.username = localStorage.getItem("login_username");
    //   this.sendMail_form.password = localStorage.getItem("login_password");
    //   // console.log('sendmail',this.sendMail_form.username)
    //   // console.log('sendmail',this.sendMail_form.password)

    //   //发送邮件
    // },
    draft: function(event) {
      this.sendMail_form.username = localStorage.getItem("login_username");
      this.sendMail_form.password = localStorage.getItem("login_password");

      //处理收件人信息
      let str=this.sendMail_form.to
      let strlist=str.split(';')
      for(let i=0;i<strlist.length;i++){
        this.tolist[i]=strlist[i];
        console.log(this.tolist[i])
      }

      //组织元素发生默认的行为
      event.preventDefault();
      let formData = new FormData();
      for (let i = 0; i < this.file.length; i++)
        formData.append("file", this.file[i]);
      this.$axios.post("/attachments", formData).catch(failResponse => {});

      this.$axios
        .post("/draft", {
          username: this.sendMail_form.username,
          password: this.sendMail_form.password,
          to: this.tolist,
          cc: this.sendMail_form.cc,
          bcc: this.sendMail_form.bcc,
          subject: this.sendMail_form.subject,
          content: this.sendMail_form.content,
          attachments: null
        })
        .then(successResponse => {
          if (successResponse.data.code === 250) {
            this.$router.replace({ path: "/recieveMail" });
          }
        })
        .catch(failResponse => {});
    },
    cancel_btn() {
      this.sendMail_form.username = "";
      this.sendMail_form.password = "";
      this.tolist = [];
      this.sendMail_form.subject = "";
      this.sendMail_form.content = "";
    },

    //选择文件的按钮
    checkFile() {
      document.querySelector("#fileinput").click();
      //this.fileName = document.querySelector("#fileinput").files[0].name;
    }, //input
    getFile: function(event) {
      this.file = event.target.files;
      this.fileName = this.file[0].name;
      //console.log("input"+this.file[0].name)
    }, //提交按钮
    submit: function(event) {
      this.sendMail_form.username = localStorage.getItem("login_username");
      this.sendMail_form.password = localStorage.getItem("login_password");


      //处理收件人信息
      let str=this.sendMail_form.to
      let strlist=str.split(';')
      for(let i=0;i<strlist.length;i++){
        this.tolist[i]=strlist[i];
        console.log(this.tolist[i])
      }

      //组织元素发生默认的行为
      event.preventDefault();
      let formData = new FormData();
      for (let i = 0; i < this.file.length; i++)
        formData.append("file", this.file[i]);

      this.$axios.post("/attachments", formData)
      .then(res => {
        console.log(res.data.length);
        console.log(res.data[0]);
        this.$axios
                .post("/mail", {
                  username: this.sendMail_form.username,
                  password: this.sendMail_form.password,
                  to: this.tolist,
                  cc: this.sendMail_form.cc,
                  bcc: this.sendMail_form.bcc,
                  subject: this.sendMail_form.subject,
                  content: this.sendMail_form.content,
                  attachments: res.data
                })
                .then(successResponse => {
                  if (successResponse.data.code === 250) {
                    this.$router.replace({ path: "/recieveMail" });
                  }
                })
                .catch(failResponse => {});
      })
      .catch(failResponse => {});
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
.submit_btn {
  margin-top: 10px;
}
</style>>
