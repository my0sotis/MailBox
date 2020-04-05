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
        @selection-change="handleSelectionChange">
        <el-table-column
          fixed
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          prop="date"
          label="发送日期"
          width="120">
        </el-table-column>
        <el-table-column
          prop="name"
          label="收件人"
          width="120">
        </el-table-column>
        <el-table-column
          prop="content"
          label="主题"
          width="800"
          show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">查看</el-button>
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
          <el-button round @click="transmit_btn">转发</el-button>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>>

<script>
  export default {
    data() {
      return {
        tableData: [{
          date: '2016-05-03',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-02',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-08',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-06',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-07',
          name: '王小虎',
          content: '上海市普陀区金沙江路 1518 弄'
        }],
        multipleSelection: []
      };
    },
    methods: {
      transmit_btn() {
        console.log(this.multipleSelection);
        if(this.multipleSelection.length == 1){
          this.$router.push("/sendMail");
        }
        else{
          alert("未选中一封邮件");
        }
      },
      clear_btn(){
        console.log(this.multipleSelection);
      },
      toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;

      },
      handleEdit(index, row) {
        alert(index);
        this.$router.push("/lookMail");
      },
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
