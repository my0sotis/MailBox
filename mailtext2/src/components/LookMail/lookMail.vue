<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }" >首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: this.currentUrl.url }" >{{this.currentUrl.title}}</el-breadcrumb-item>
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
            <el-input v-model="recieveMail_form.sender">
              <template slot="prepend">{{this.currentUrl.s_or_r}}:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input v-model="recieveMail_form.subject">
              <template slot="prepend">主题:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input v-model="recieveMail_form.date" >
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
          v-html = "recieveMail_form.content"
        ></el-input>


        <!-- 最后的三个按钮 -->
        <el-row :gutter="10" class="bottom_btns">
          <el-col :span="2">
            <el-button type="primary" round @click="reply">回复</el-button>
          </el-col>
          <el-col :span="2">
            <el-button round @click="clear_btn">删除</el-button>
          </el-col>
          <el-col :span="2">
            <el-button round @click="returnTo" class="R_btn">返回</el-button>
          </el-col>
          <el-col :span="4">
            <!--移动邮件，其中disabled属性判断当前界面能否使用移动邮件功能-->
            <el-select v-model="value" placeholder = "移动至" @change="moveDestination"
                       :disabled = "{ page: this.currentUrl.url['title'] == ('草稿箱')}?false :true">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"></el-option>
            </el-select>
          </el-col>
          <el-col :span="13">
            <div id="enclosureSel">
              <el-select  id = "enclosure" v-model="documentName" :placeholder= "existed" @change="downloadDoc"
                          :disabled = "{ exist :this.recieveMail_form.enclosure.length == 0}?false:true">
                <el-option
                  v-for="item in documents"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                <span style="float: left">
                    <i class="el-icon-s-cooperation"></i>
                </span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.label }}</span>
                </el-option>
              </el-select>
            </div>

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
      //select组件需要根据当前界面的不同动态添加选项
      options: [],
      documents:[

      ],
      documentName:'',
      value: '',
      //邮件信息表单（可能是sender或者receiver，就用sender代表了）
      recieveMail_form: {
        sender: "",
        subject: "",
        date: "",
        content: "",
        enclosure:[]
      },
      chooseNo : "",
      //上一个界面的URL
      oldUrl : "",
      //当前界面URL，用于动态改变面包屑导航栏的内容
      currentUrl: {
        url : "",
        title :"",//当前界面名称
        s_or_r : "",
      },
    };
  },
  created() {

    this.sendChooseNo();
    this.loadEnclosure();
    //初始化面包屑
    this.$nextTick(()=> {
      //根据URL判断当前界面，初始化select和面包屑
      switch (this.currentUrl['url']){
        case '/rubbishMail':
          this.currentUrl['title'] = '垃圾箱';
          this.currentUrl['s_or_r'] = '发送';
          this.options.push(
            {
              value: '收件箱',
              label: '收件箱'
            }, {
              value: '已发送',
              label: '已发送'
            }
          );
          break;
        case '/al_send':
          this.currentUrl['title'] = '已发送';
          this.currentUrl['s_or_r'] = '收件';
          this.options.push(
            {
              value: '垃圾箱',
              label: '垃圾箱'
            }
          );
          break;
        case '/recieveMail':
          this.currentUrl['title'] = '收件箱';
          this.currentUrl['s_or_r'] = '发送';
          this.options.push(
            {
              value: '垃圾箱',
              label: '垃圾箱'
            }
          );
          break;
        case '/draftMail':
          this.currentUrl['title'] = '草稿箱';
          this.currentUrl['s_or_r'] = '收件';
          break;
      }

    });
  },
  //钩子函数，判断上一个界面的URL
  beforeRouteEnter (to, from, next){
    next(vm => {
      // 通过 `vm` 访问组件实例,将值传入oldUrl
      vm.currentUrl["url"] = from.path
    })
  },
  computed: {
    existed(){
      return this.recieveMail_form.enclosure.length ? '有附件':'无附件'

    }
  },
  methods: {
    loadEnclosure(){
      if(this.recieveMail_form.enclosure.length !=0){
        this.documents.push(
          {
            value: '全部下载',
            label: '全部下载'
          }
        );
      }
      for(var i =0;i<this.recieveMail_form.enclosure.length;i++){
        let spliteUrl = this.recieveMail_form.enclosure[i].split('/')
        let documentName=  spliteUrl[spliteUrl.length-1]
        this.documents.push(
          {
            value: documentName,
            label: documentName
          }
        );

      }
    },
    //处理附件
    downloadDoc(){
      if(this.documentName === '全部下载'){
        for(var i = 0;i<this.recieveMail_form.enclosure.length;i++){
          let a = document.createElement("a");
          a.href =this.recieveMail_form.enclosure[i];
          a.download = this.recieveMail_form.enclosure[i].split('/')[this.recieveMail_form.enclosure[i].split('/').length-1];
          a.style.display = "none";
          document.body.appendChild(a);
          a.click();  //下载
          console.log(a.href);
          URL.revokeObjectURL(a.href);    // 释放URL 对象
          document.body.removeChild(a);   // 删除 a 标签
        }
      }
      else {
        for(var i = 0;i<this.recieveMail_form.enclosure.length;i++){
          if(this.documentName === this.recieveMail_form.enclosure[i].split('/')[this.recieveMail_form.enclosure[i].split('/').length-1]){
            let a = document.createElement("a");
            a.href =this.recieveMail_form.enclosure[i];
            a.download = this.documentName;
            a.style.display = "none";
            document.body.appendChild(a);
            a.click();  //下载
            URL.revokeObjectURL(a.href);    // 释放URL 对象
            document.body.removeChild(a);   // 删除 a 标签
            break;
          }
        }
      }
    },
    //移动邮件到其他位置
    moveDestination(){
      if(this.value === '已发送'){
        alert("已将邮件移至已发送");
        this.$router.replace(this.currentUrl['url'])
      }
      else if(this.value === '收件箱'){
        alert(("已将邮件移至收件箱"));
        this.$router.replace(this.currentUrl['url'])
      }
      else{

      }
    },
    //获取当前界面的URL中的邮件序号
    sendChooseNo(){
      let chooseNo = this.$route.query["id"];//取得的query['id']是数组
      let type = this.$route.query["type"];  //-1代表列表 0代表已发送 1代表草稿 2代表垃圾箱
      var url = "";
      if(type === -1){
        url = "/transmit/"+chooseNo;
      }
      else{
        url = "/transmitPost/"+chooseNo+"&&"+type;
      }
      this.$axios.get(url).then(
        res=>{
          this.recieveMail_form.sender = res.data["briefInfo"]["senderEmail"];
          this.recieveMail_form.subject = res.data["briefInfo"]["subject"];
          this.recieveMail_form.date = res.data["briefInfo"]["datetime"];
          this.recieveMail_form.content = res.data["content"];

          for(let i = 0;i<res.data["attachments"].length;i++){
            this.recieveMail_form.enclosure.push(res.data["attachments"][i])
          }
          this.loadEnclosure();
        }
      )
    },
    //回复
    reply() {
      let chooseNo = this.$route.query["id"];
      //传递该邮件序号
      this.$router.push({path:'/sendMail',query:{
          id : chooseNo,
          judge: "0",
          type:this.$route.query["type"]
        }})
    },
    //删除该邮件
    clear_btn() {
      let chooseNo = this.$route.query["id"];
      let type = this.$route.query["type"];
      var url = "";
      if(type === -1){
        url = "/deleteMail/"+chooseNo;
      }
      else{
        url = "/deletePostMail/"+chooseNo+"&&"+type;
      }
      console.log(url);
      this.$axios
        .post(url)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            alert("已移动邮件至垃圾箱");
            this.$router.replace({ path: "/recieveMail" });
          } else {
            alert("删除失败");
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    //返回按钮
    returnTo() {
      this.$router.replace(this.currentUrl['url'])
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
  #enclosureSel{
    float:right;
  }
</style>>
