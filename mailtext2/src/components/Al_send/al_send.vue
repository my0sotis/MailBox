<template>
    <div>
        <h3>al_send</h3>
    </div>
</template>>

<script>
<<<<<<< Updated upstream
export default {
    
}
=======
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
          no.push(mul[i].no)
        }
        return no;
      },
      //转发
      transmit() {
        let chooseNo = this.getchooseNo(this.multipleSelection);
        if(chooseNo.length != 1){
          alert("转发的邮件数量必须为1")
        }
        else{
          this.$axios
            .post('/draftMail', {
              chooseNo: chooseNo,
            })
            .then(successResponse => {
              if(successResponse.data.code === 200){
                this.$router.push({path:'/sendMail',query:{
                  id : chooseNo,
                  judge:'1'
                }})
              }
              else{
                alert('转发邮件失败');
              }
            })
            .catch(function (error) {
              console.log(error);
            })
        }
      },
      //读取本地json文件（用于测试，正式版本将从后台获取json文件)
      getJsonData(){
        var url = 'http://localhost:8080/static/testSended.json';
        //交互内容：传递选择的邮件序号，后台返回该邮件对应的Json数组
        this.$axios.get(url).then(
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
          .post('/Al_send', {
            chooseNo: deleteNo,
          })
          .then(successResponse => {
            if(successResponse.data.code === 200){
              alert('已移动邮件至垃圾箱')
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
          id : this.tableData[index].no
        }})
        /*
        //交互内容：传递选择的邮件序号，后台返回相应的json数据并传递到打开的lookMail界面
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
          })*/
      },
    }
  };

>>>>>>> Stashed changes
</script>>

<style scoped>

</style>>