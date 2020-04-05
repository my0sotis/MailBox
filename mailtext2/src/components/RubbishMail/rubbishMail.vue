 <template>
    <div>
      <!-- 面包屑导航区域 -->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>垃圾箱</el-breadcrumb-item>
        <!-- <el-breadcrumb-item>活动列表</el-breadcrumb-item>
        <el-breadcrumb-item>活动详情</el-breadcrumb-item>-->
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
            prop="no"
            label="序号"
            width="55">
          </el-table-column>
          <el-table-column
            prop="date"
            label="发送日期"
            width="120">
          </el-table-column>
          <el-table-column
            prop="name"
            label="发件人"
            width="120">
          </el-table-column>
          <el-table-column
            prop="content"
            label="主题"
            width="700"
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

        <!-- 后两个按钮区域 -->
        <el-row :gutter="10" class="bottom_btns">
          <el-col :span="2.5">
            <!--全部清空按钮 事件clearALL_btn-->
            <el-button round @click="toggleSelection()">取消选择</el-button>
          </el-col>
          <el-col :span="2">
            <!--全部清空按钮 事件clearALL_btn-->
            <el-button round @click="clear_btn">清空</el-button>
          </el-col>
          <el-col :span="4">
            <!--移动邮件按钮 事件clearALL_btn-->
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
      lookMailJson : null,
      options: [{
        value: '收件箱',
        label: '收件箱'
      }, {
        value: '已发送',
        label: '已发送'
      }],
      value: '',
      tableData : [],

      /*tableData: [{
        no:1,
        date: '2016-05-03',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄'
      }, {
        no:2,
        date: '2016-05-02',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄'
      }, {
        no:3,
        date: '2016-05-04',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄adasdadasdasdasdaas'
      }, {
        no:4,
        date: '2016-05-01',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄'
      }, {
        no:5,
        date: '2016-05-08',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄'
      }, {
        no:6,
        date: '2016-05-06',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄'
      }, {
        no:7,
        date: '2016-05-07',
        name: '王小虎',
        content: '上海市普陀区金沙江路 1518 弄'
      }]
      ,*/
      multipleSelection: []
    };
  },
  mounted(){
     this.getJsonData();
     alert(this.tableData);
  },
  methods: {
    getchooseNo(mul){
      let no = [];
      for(var i = 0;i<mul.length;i++){
        no.push(mul[i].no)
      }
      return no;
    },
    getJsonData(){
      var url = 'http://localhost:8082/static/testTable.json';
      this.$axios.get(url).then(
        res=>{
            console.log(res.data)
            for(var i =0;i<res.data.length;i++){
              this.tableData.push(res.data[i])
            }
            console.log(this.tableData)
        }
      )
    },
    move_btn(){
      if(this.value == "收件箱" && this.multipleSelection.length != 0){
        let receiveNo = getchooseNo(this.multipleSelection);

        alert("已将邮件移至收件箱" + receiveNo);
        this.$axios
          .post('/receiveMail', {
            chooseNo: receiveNo,
          })
          .then(successResponse => {
            alert('成功移至收件箱')
          })
          .catch(function (error) {
            console.log(error);
            alert('移动邮件失败')
          })
      }
      else if(this.value =="已发送" && this.multipleSelection.length != 0){
        let al_SendNo = getchooseNo(this.multipleSelection);

        this.$axios
          .post('/receiveMail', {
            chooseNo: al_SendNo,
          })
          .then(successResponse => {
            alert('成功移至已发送')
          })
          .catch(function (error) {
            console.log(error);
            alert('移动邮件失败')
          })
        alert("已将邮件移至已发送");
      }
      else{
        alert("请选择移动的邮件和移至的地方");
      }
    },
    clear_btn(){
      let deleteNo = getchooseNo(this.multipleSelection);
      this.$axios
        .post('/rubbishMail', {
          chooseNo: deleteNo,
        })
        .then(successResponse => {
          if(successResponse.data.code === 200){
            alert('清空邮件成功')
          }
          else{
            alert('清空邮件失败');
          }
        })
        .catch(function (error) {
          console.log(error);
        })
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
    handleEdit(index) {
      this.$axios
        .post('/lookMail', {
           chooseNo: this.tableData[index].no,
        })
        .then(successResponse => {
           if(successResponse.data.code === 200){
               this.$router.push({path:'/lookMail',query:{
                 id : this.tableData[index].no
               }})
           }
           else{
             alert('查看邮件失败');
           }
        })
        .catch(function (error) {
            console.log(error);
        })
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
