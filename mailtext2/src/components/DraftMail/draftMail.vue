<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>草稿箱</el-breadcrumb-item>
      <!-- <el-breadcrumb-item>活动列表</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
    </el-breadcrumb>
    <!-- 卡片区域 -->
    <el-card>
      <!-- 显示草稿箱 区域 -->
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column fixed type="selection" width="55"></el-table-column>
        <el-table-column prop="no" label="序号" width="55"></el-table-column>
        <el-table-column prop="date" label="发送日期" width="120"></el-table-column>
        <el-table-column prop="receiver" label="收件人" width="250"></el-table-column>
        <el-table-column prop="theme" label="主题" width="600" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 后三个按钮区域 -->
      <el-row :gutter="10" class="bottom_btns">
        <el-col :span="2.5">
          <!--取消选择-->
          <el-button round @click="toggleSelection()">取消选择</el-button>
        </el-col>
        <el-col :span="2">
          <!--删除-->
          <el-button round @click="clear_btn">删除</el-button>
        </el-col>
        <el-col :span="2">
          <!--转发-->
          <el-button round @click="transmit()">转发</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>>

<script>
export default {
  data() {
    return {
      tableData: [],
      multipleSelection: []
    };
  },
  //初始化界面时加载邮件数据
  mounted() {
    console.log("loading.start");
    const rLoading = this.openLoading();
    //加载数据
    this.getJsonData();

    rLoading.close();
    console.log("loading.close");
  },
  //可能还需要单独写一个后台修改数据后，前端同步修改的方法
  methods: {
    //获取选择的邮件序号
    getchooseNo(mul) {
      let no = [];
      for (var i = 0; i < mul.length; i++) {
        no.push(mul[i].no);
      }
      return no;
    },
    //转发
    transmit() {
      let chooseNo = this.getchooseNo(this.multipleSelection);
      if (chooseNo.length != 1) {
        alert("转发的邮件数量必须为1");
      } else {
        //交互内容：传递需要转发的邮件序号，成功即打开发送邮件的界面，同时获取相关邮件信息
        this.$axios
          .post("/draftMail", {
            chooseNo: chooseNo
          })
          .then(successResponse => {
            if (successResponse.data.code === 200) {
              this.$router.push({
                path: "/sendMail",
                query: {
                  id: chooseNo,
                  judge: "1"
                }
              });
            } else {
              alert("转发邮件失败");
            }
          })
          .catch(function(error) {
            console.log(error);
          });
      }
    },
    //读取本地json文件（用于测试，正式版本将从后台获取json文件)
    getJsonData() {
      var url = "http://localhost:8080/static/testDraft.json";
      //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json数组
      this.$axios.get(url).then(res => {
        for (var i = 0; i < res.data.length; i++) {
          this.tableData.push(res.data[i]);
        }
        console.log(this.tableData);
      });
    },
    //删除选择的邮件
    clear_btn() {
      let deleteNo = this.getchooseNo(this.multipleSelection);
      //交互内容：传递需要删除的草稿邮件序号，后台直接删除，不需要放到垃圾箱
      this.$axios
        .post("/draftMail", {
          chooseNo: deleteNo
        })
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            alert("删除草稿成功");
          } else {
            alert("删除草稿失败");
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    //取消选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    //获取多选的邮件信息
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //查看特定的邮件信息
    handleEdit(index) {
      //交互内容：传递选择的邮件序号，后台返回相应的json数据并传递到打开的lookMail界面
      this.$axiosA
        .post("/lookMail", {
          chooseNo: this.tableData[index].no,
          A
        })
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            /*  */
            this.$router.push({
              path: "/lookMail",
              query: {
                id: this.tableData[index].no
              }
            });
          } else {
            alert("查看邮件失败");
          }
        })
        .catch(function(error) {
          console.log(error);
        });
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
