<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>已发送</el-breadcrumb-item>
      <!-- <el-breadcrumb-item>活动列表</el-breadcrumb-item>
      <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
    </el-breadcrumb>
    <!-- 卡片区域 -->
    <el-card>
      <!-- <div slot="header" class="navigation">

      <!-- 显示已发送 区域 -->
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
          prop="num"
          label="序号"
          width="55">
        </el-table-column>
        <el-table-column
          prop="datetime"
          label="发送日期"
          width="120">
        </el-table-column>
        <el-table-column
          prop="receiverEmail"
          label="收件人"
          width="250">
        </el-table-column>
        <el-table-column
          prop="subject"
          label="主题"
          width="600"
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

      <!-- 后两个按钮区域 -->
      <el-row :gutter="10" class="bottom_btns">
        <el-col :span="2.5">
          <!--全部清空按钮 事件clearALL_btn-->
          <el-button round @click="toggleSelection()">取消选择</el-button>
        </el-col>
        <el-col :span="2.5">
          <!--全部清空按钮 事件clearALL_btn-->
          <el-button round @click="move2Rubbish">移至垃圾箱</el-button>
        </el-col>
        <el-col :span="2">
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
        tableData: [],
        multipleSelection: []
      };
    },
    mounted(){
      this.getJsonData();
    },
    methods: {
      //获取选择的邮件序号
      getchooseNo(mul){
        let no = [];
        for(var i = 0;i<mul.length;i++){
          no.push(mul[i].num)
        }
        return no;
      },
      //转发
      transmit() {
        let chooseNo = this.getchooseNo(this.multipleSelection);
        if(chooseNo.length === 1){
          this.$router.push({path:'/sendMail',query:{
              id : chooseNo,
              type : 0,
              judge: "1"
            }})
        }
        else{
          alert("转发的邮件数量必须为1")
        }
      },
      //读取本地json文件（用于测试，正式版本将从后台获取json文件)
      getJsonData(){
        //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json数组
        this.$axios.get("/briefDBMails/0").then(
          res=>{
            for(var i =0;i<res.data.length;i++){
              this.tableData.push(res.data[i])
            }
            console.log(this.tableData)
          }
        )
      },
      //移动选择的邮件
      move2Rubbish(){
        let deleteNo = this.getchooseNo(this.multipleSelection);
        //交互内容：传递选择的邮件序号，后台修改相应邮件的所属为rubbish，且删除al_send内的相同邮件数据
        this.$axios
          .post('/deletePostMail/'+deleteNo+"&&0")
          .then(successResponse => {
            if(successResponse.data.code === 200){
              alert('已移动邮件至垃圾箱');
              location.reload();
            }
            else{
              alert('移动邮件失败');
            }
          })
          .catch(function (error) {
            console.log(error);
          })
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
        //测试部分
        this.$router.push({path:'/lookMail',query:{
          id : this.tableData[index].num,
          type : 0
        }})
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
