<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>收件箱</el-breadcrumb-item>
      <!-- <el-breadcrumb-item>活动列表</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
    </el-breadcrumb>
    <!-- 卡片区域 -->
    <el-card>
      <!-- 显示收件箱 区域 -->
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column fixed type="selection" width="55"></el-table-column>
        <el-table-column prop="num" label="序号" width="55"></el-table-column>
        <el-table-column prop="datetime" label="发送日期" width="120"></el-table-column>
        <el-table-column prop="senderEmail" label="发件人" width="250"></el-table-column>
        <el-table-column prop="subject" label="主题" width="600" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 后两个按钮区域 -->
      <el-row :gutter="10" class="bottom_btns">
        <el-col :span="2.5">
          <!--取消选择按钮-->
          <el-button round @click="toggleSelection()">取消选择</el-button>
        </el-col>
        <el-col :span="2.5">
          <el-button round @click="move2Rubbish">移至垃圾箱</el-button>
        </el-col>
        <el-col :span="2">
          <!--转发-->
          <el-button round @click="transmit">转发</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>>

<script>
export default {
  data() {
    return {
      value: "",
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
        no.push(mul[i].num);
      }
      return no;
    },

    //转发
    transmit() {
      let chooseNo = this.getchooseNo(this.multipleSelection);
      if(chooseNo != null && chooseNo.length === 1){
        //转发内容
        this.$router.push({
          path: "/sendMail",
          query: {
            id: this.tableData.length - chooseNo[0] + 1,
            judge: "1",
            type:-1
          }
        });
      }
      else{
        alert("转发的邮件数量必须为1");
      }
    },

    //移至垃圾箱
    move2Rubbish() {
      let deleteNo = this.getchooseNo(this.multipleSelection);
      if(deleteNo.length <= 0)
        return;
      for(let i = 0; i < deleteNo.length; i++)
        deleteNo[i] = this.tableData.length - deleteNo[i] + 1
      //交互内容：传递选择的邮件序号，后台修改相应邮件的所属为rubbish，且删除Receive内的相同邮件数据
      this.$axios
        .post("/deleteMail/"+deleteNo)
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            alert("已移动邮件至垃圾箱");
            location.reload();
          } else {
            alert("删除失败");
          }
        })
        .catch(function(error) {
          console.log(error);
        });
    },

    //获取邮件简要信息
    getJsonData() {
      //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json数组
      this.$axios.get("/briefMails").then(res => {
        for (let i = 0; i < res.data.length; i++) {
          this.tableData.push(res.data[i]);
        }
        console.log(this.tableData);
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
      //交互内容：传递选择的邮件序号，后台返回相应的json数据并传递到打开的lookMail界面，注意，receive呈现的内容是发件人，与其他
      //页面不同，处理数据时要传递发件人的数据
      let count = this.tableData.length - this.tableData[index].num + 1;
      console.log(count);
      this.$router.push({
        path: "/lookMail",
        query: {
          id: count,
          type : -1
        }
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
