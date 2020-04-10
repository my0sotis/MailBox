<template>
    <div>
        <h3>draftMail</h3>
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
    //初始化界面时加载邮件数据
    mounted(){
      this.getJsonData();
    },
    //可能还需要单独写一个后台修改数据后，前端同步修改的方法
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
          //交互内容：传递需要转发的邮件序号，成功即打开发送邮件的界面，同时获取相关邮件信息
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
        var url = 'http://localhost:8080/static/testDraft.json';
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
      //删除选择的邮件
      clear_btn(){
        let deleteNo = this.getchooseNo(this.multipleSelection);
        //交互内容：传递需要删除的草稿邮件序号，后台直接删除，不需要放到垃圾箱
        this.$axios
          .post('/draftMail', {
            chooseNo: deleteNo,
          })
          .then(successResponse => {
            if(successResponse.data.code === 200){
              alert('删除草稿成功')
            }
            else{
              alert('删除草稿失败');
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
        //交互内容：传递选择的邮件序号，后台返回相应的json数据并传递到打开的lookMail界面
        this.$axiosA
          .post('/lookMail', {
            chooseNo: this.tableData[index].no,A
          })
          .then(successResponse => {
            if(successResponse.data.code === 200){/*  */
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

>>>>>>> Stashed changes
</script>>

<style scoped>

</style>>