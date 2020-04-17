<template>
  
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: this.currentUrl.url }">{{this.currentUrl.title}}</el-breadcrumb-item>
      <el-breadcrumb-item>邮件</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- <div>
        <h3>邮件主题</h3>
      </div>-->

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
            <el-input type="text" v-model="recieveMail_form.sender">
              <template slot="prepend">{{this.currentUrl.s_or_r}}:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input type="text" v-model="recieveMail_form.theme">
              <template slot="prepend">主题:</template>
            </el-input>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="10">
            <el-input type="text" v-model="recieveMail_form.date">
              <template slot="prepend">时间:</template>
            </el-input>
          </el-col>
        </el-row>

        <!-- 主要的内容 -->
        <!-- <el-input
          class="contentMail"
          type="textarea"
          :rows="10"
          placeholder="邮件内容"
          v-model="recieveMail_form.content"
        ></el-input>-->
        <!-- <div><iframe name = "child" id = "child" :src="www.baidu.com#asd=1" width="1920" height="880" frameborder="0" scrolling="no" style="position:related;top: 2.8px;left: 0px;"></iframe>
        </div>-->
        <div id="vue_app">
          <p id="a" v-html="rawHtml">Using mustaches: {{ rawHtml }}</p>
          <p>Using v-html directive:</p>
          <!-- <script type="text/javascript" src="router/index.js"></script> -->
        </div>

        <!-- 最后的三个按钮 -->
        <el-row :gutter="10" class="bottom_btns">
          <el-col :span="2">
            <el-button type="primary" round @click="reply">回复</el-button>
          </el-col>
          <el-col :span="2">
            <el-button round @click="clear_btn">清空</el-button>
          </el-col>
          <el-col :span="2">
            <el-button round @click="returnTo" class="R_btn">返回</el-button>
          </el-col>
          <el-col :span="4">
            <!--移动邮件，其中disabled属性判断当前界面能否使用移动邮件功能-->
            <el-select
              v-model="value"
              placeholder="移动至"
              @change="moveDestination"
              :disabled="{ page: this.currentUrl.url['title'] == ('草稿箱')}?false :true"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
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
      value: "",
      //邮件信息表单（可能是sender或者receiver，就用sender代表了）
      recieveMail_form: {
        sender: "",
        theme: "",
        date: "",
        content: ""
      },
      chooseNo: "",
      //上一个界面的URL
      oldUrl: "",
      //当前界面URL，用于动态改变面包屑导航栏的内容
      currentUrl: {
        url: "",
        title: "", //当前界面名称
        s_or_r: ""
      }
    };
  },
  created() {
    console.log("loading.start");
    const rLoading = this.openLoading();
    //加载数据

    this.sendChooseNo();
    //初始化面包屑
    this.$nextTick(() => {
      //根据URL判断当前界面，初始化select和面包屑
      switch (this.currentUrl["url"]) {
        case "/rubbishMail":
          this.currentUrl["title"] = "垃圾箱";
          this.currentUrl["s_or_r"] = "发送";
          this.options.push(
            {
              value: "收件箱",
              label: "收件箱"
            },
            {
              value: "已发送",
              label: "已发送"
            }
          );
          break;
        case "/al_send":
          this.currentUrl["title"] = "已发送";
          this.currentUrl["s_or_r"] = "收件";
          this.options.push({
            value: "垃圾箱",
            label: "垃圾箱"
          });
          break;
        case "/recieveMail":
          this.currentUrl["title"] = "收件箱";
          this.currentUrl["s_or_r"] = "发送";
          this.options.push({
            value: "垃圾箱",
            label: "垃圾箱"
          });
          break;
        case "/draftMail":
          this.currentUrl["title"] = "草稿箱";
          this.currentUrl["s_or_r"] = "收件";
          break;
      }
    });
    
    rLoading.close();
    console.log("loading.close");
    
    // this.app1 = new Vue({
    //   el: "#app",
    //   data: {
    //     htm: "<b style='color:red'>v-html</b>"
    //   }
    // });
  },
  //钩子函数，判断上一个界面的URL
  beforeRouteEnter(to, from, next) {
    next(vm => {
      // 通过 `vm` 访问组件实例,将值传入oldUrl
      vm.currentUrl["url"] = from.path;
    });
  },
  methods: {
    //移动邮件到其他位置
    moveDestination() {
      if (this.value === "已发送") {
        alert("已将邮件移至已发送");
        this.$router.replace(this.currentUrl["url"]);
      } else if (this.value === "收件箱") {
        alert("已将邮件移至收件箱");
        this.$router.replace(this.currentUrl["url"]);
      } else {
      }
    },
    //获取当前界面的URL中的邮件序号
    sendChooseNo() {
      let chooseNo = this.$route.query["id"]; //取得的query['id']是数组
      this.chooseNo = chooseNo[0];
      console.log("sendChoosNo", this.chooseNo);
      //测试部分，仅测试序号为1的邮件数据
      if (this.chooseNo == "1") {
        var url = "http://localhost:8080/static/testLook.json";
        //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json对象
        this.$axios.get(url).then(res => {
          this.recieveMail_form.sender = res.data["sender"];
          //this.recieveMail_form.sender.value=res.data["sender"]
          //console.log(res.data["sender"]);
          this.recieveMail_form.theme = res.data["theme"];
          // this.recieveMail_form.theme.value = res.data["theme"]

          this.recieveMail_form.date = res.data["date"];
          //this.recieveMail_form.date.value = res.data["date"]

          //this.recieveMail_form.content = res.data["content"]
          // this.recieveMail_form.content.value = res.data["content"]
        });
      } else {
        console.log("error!!!!");
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
      this.$router.push({
        path: "/sendMail",
        query: {
          id: this.chooseNo,
          judge: "0"
        }
      });
    },
    //删除该邮件
    clear_btn() {
      //测试部分
      alert("删除邮件成功");
      this.$router.replace(this.currentUrl["url"]);
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
    },
    //返回按钮
    returnTo() {
      this.$router.replace(this.currentUrl["url"]);
    }
  }
};

</script>>

<style scoped>
.contentMail {
  margin-top: 10px;
}
.bottom_btns {
  margin-top: 10px;
}
</style>>
