 <template>
    <div>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>垃圾箱</el-breadcrumb-item>
      </el-breadcrumb>
      <!-- 卡片区域 -->
      <el-card>
        <!-- 显示垃圾邮件 区域 -->
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
            prop="senderEmail"
            label="发件人"
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
                @click="handleEdit(scope.$index)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 按钮区域 -->
        <el-row :gutter="10" class="bottom_btns">
          <el-col :span="2.5">
            <!--取消多选按钮-->
            <el-button round @click="toggleSelection()">取消选择</el-button>
          </el-col>
          <el-col :span="2">
            <!--清空按钮-->
            <el-button round @click="clear_btn">清空</el-button>
          </el-col>
          <el-col :span="4">
            <!--移动邮件按钮-->
            <el-select v-model="value" placeholder = "移动至">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"></el-option>
            </el-select>
          </el-col>
          <el-col :span="2">
            <el-button round @click="move_btn">移动</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </template>>

<script>
export default {
  data() {
    return {
      //查看邮件具体内容的Json变量
      lookMailJson : null,
      options: [{
        value: '草稿箱',
        label: '草稿箱'
      }
      //   {
      //   value: '已发送',
      //   label: '已发送'
      // }
      ],
      value: '',
      //接收垃圾邮件的数组
      tableData : [],
      //存储选择的垃圾邮件数据
      multipleSelection: []
    };
  },
  //初始化界面时加载邮件数据
  mounted(){
     this.getJsonData();
     console.log(this.$route.fullPath.split('/')[1])
  },
  //可能还需要单独写一个后台修改数据后，前端同步修改的方法
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
            type : 2,
            judge: "1",
          }})
      }
      else{
        alert("转发的邮件数量必须为1")
      }
    },
    //读取本地json文件（用于测试，正式版本将从后台获取json文件)
    getJsonData(){
      //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json数组
      this.$axios.get("/briefDBMails/2").then(
        res=>{
          for(var i =0;i<res.data.length;i++){
            this.tableData.push(res.data[i])
          }
          console.log(this.tableData)
        }
      )
    },
    //移动邮件至收件箱或者已发送
    move_btn(){
      var type = 0;
      if(this.value === "已发送" && this.multipleSelection.length != 0){
        type = 0;
      }
      else if(this.value ==="草稿箱" && this.multipleSelection.length != 0) {
        type = 1;
      }
      let receiveNo = this.getchooseNo(this.multipleSelection);
      //交互内容：传递选择的邮件序号，后台标记相应的邮件为收件箱的，返回一个alert作为结果
      this.$axios
        .get('/moveMails/'+receiveNo+"&&"+type)
        .then(successResponse => {
          if(type === 0){
            console.log('/moveMails/'+receiveNo+"&&"+type);
            alert('成功移至已发送');
            location.reload();
          }
          else if(type === 1){
            location.reload();
            alert('成功移至草稿箱')
          }
        })
        .catch(function (error) {
          console.log(error);
          alert('移动邮件失败')
        });
    },
    //删除选择的邮件
    clear_btn(){
      let deleteNo = this.getchooseNo(this.multipleSelection);
      if(deleteNo === null)
        return;
      //交互内容：传递参数deleteNo邮件序号，后台删除相同序号的邮件
      this.$axios
        .post('/deletePostMail/'+deleteNo+"&&2")
        .then(successResponse => {
          if(successResponse.data.code === 200){
            alert('清空邮件成功');
            location.reload();
          }
          else{
            alert('清空邮件失败');
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
          type : 2
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
