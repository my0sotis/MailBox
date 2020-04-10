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
<<<<<<< Updated upstream
            <el-input v-model="recieveMail_form.send_P">
              <template slot="prepend">发送:</template>
=======
            <el-input type="text" v-model="recieveMail_form.sender">
              <template slot="prepend">{{this.currentUrl.s_or_r}}:</template>
>>>>>>> Stashed changes
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
<<<<<<< Updated upstream
            <el-input v-model="recieveMail_form.title">
=======
            <el-input type="text" v-model="recieveMail_form.theme">
>>>>>>> Stashed changes
              <template slot="prepend">主题:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
<<<<<<< Updated upstream
            <el-input v-model="recieveMail_form.time">
=======
            <el-input type="text" v-model="recieveMail_form.date">
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    storeRubbish_btn() {    //放入垃圾箱


=======
    //获取当前界面的URL中的邮件序号
    sendChooseNo(){
      let chooseNo = this.$route.query["id"];//取得的query['id']是数组
      this.chooseNo = chooseNo[0];
      console.log('sendChoosNo',this.chooseNo);
      //测试部分，仅测试序号为1的邮件数据
      if(this.chooseNo == "1"){
        
        var url = 'http://localhost:8080/static/testLook.json';
        //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json对象
        this.$axios.get(url).then(
          res=>{
            this.recieveMail_form.sender = res.data["sender"]
            //this.recieveMail_form.sender.value=res.data["sender"]
            //console.log(res.data["sender"]);
            this.recieveMail_form.theme = res.data["theme"]
           // this.recieveMail_form.theme.value = res.data["theme"]

            this.recieveMail_form.date = res.data["date"]
            //this.recieveMail_form.date.value = res.data["date"]

            this.recieveMail_form.content = res.data["content"]
           // this.recieveMail_form.content.value = res.data["content"]
            
          }
        )
      }
      else{
        console.log("error!!!!")
      }
      //正式版本
      //交互内容：传递邮件序号到后台，后台返回一个json对象，不是数组
      /*
      this.$axios
        .post('/lookMail', {
          chooseNo: this.chooseNo,
        })
        .then(res => {
          if(res != null){
            //返回一个json对象
          }
          else{
            alert('查看邮件失败');
          }
        })
        .catch(function (error) {
          console.log(error);
        })*/
    },
    //回复
    reply() {
      //传递该邮件序号
      this.$router.push({path:'/sendMail',query:{
        id : this.chooseNo,
        judge:'0'
      }})
    },
    //删除该邮件
    clear_btn() {
      //测试部分
      alert('删除邮件成功');
      this.$router.replace(this.currentUrl['url'])
      //正式版本
      //交互内容：传递邮件序号到后台，后台返回一个json对象，不是数组
      /*
      this.$axios
        .post('/lookMail', {
          chooseNo: this.chooseNo,
        })
        .then(successResponse => {
          if(successResponse.data.code === 200){
            alert('清空邮件成功');
            this.$router.replace(this.currentUrl['url'])
          }
          else{
            alert('清空邮件失败');
          }
        })
        .catch(function (error) {
          console.log(error);
        })*/
>>>>>>> Stashed changes
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