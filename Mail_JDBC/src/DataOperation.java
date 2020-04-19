import java.sql.*;
import java.util.ArrayList;
import org.javatuples.*;

public class DataOperation {
    private Connection con=null;
    private Statement state=null;
    public ResultSet rst=null;
    //这里初始化的表还是试验时候的表
    public DataOperation(){
        try{
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //getConnecting（）方法，用来连接mysql的数据库
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/mail_jd?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false  ","root","123456");
            if(!con.isClosed()){
                System.out.println("Succeeded connecting to the Database");
            }
            //创建statement 对象 ，用来执行sql 语句
            state=con.createStatement();
        }catch (ClassNotFoundException e){
            //数据库驱动类异常处理
            System.out.println("can't find the Driver!");
            e.printStackTrace();
        }catch (SQLException e){
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace(); //handle exception
        }
    }



    //查找已发送中的邮件
    public MailObject SearchSend(int ID){
        String formName="al_send";
        String condition="ID="+ID+"";
        MailObject m=SearchTeamRank(formName,ID);
        return m;
    }
    //查找草稿箱的邮件
    public MailObject Searchdraft(int ID){
        String formName="draft";
        String condition="ID="+ID+"";
        MailObject m=SearchTeamRank(formName,ID);
        return m;
    }
    //查找垃圾箱邮件
    public MailObject Searchrubbish(int ID){
        String formName="rubbish";
        String condition="ID="+ID+"";
        MailObject m=SearchTeamRank(formName,ID);
        return m;
    }
    public MailObject SearchTeamRank(String mailname,int id){
        String sql="select * from "+mailname+" where ID="+id+"";
        String sendDate=null,sendName=null,receiveName=null,sendAddress=null,receiveAddress=null,mailContent=null,attachment=null;
        try{
            rst=state.executeQuery(sql);
            while(rst.next()){
                sendDate=rst.getString("sendDate");
                sendName=rst.getString("sendName");
                receiveName=rst.getString("receiveName");
                sendAddress=rst.getString("sendAddress");
                receiveAddress=rst.getString("receiveAddress");
                mailContent=rst.getString("mailContent");
                attachment=rst.getString("attachment");
            }
        }catch (SQLException e){
            System.out.println("视图队伍查询成绩错误");
            e.printStackTrace();
        }
        MailObject m=new MailObject(id,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
        return m;
    }

    //添加已发送
    public void AddSend(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        AddData("al_send",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
    }
    //添加草稿箱
    public void Adddraft(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        AddData("draft",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
    }
    //添加垃圾箱
    public void Addrubbish(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        AddData("rubbish",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
    }
    public boolean AddData(String mailName,int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        String sql="insert into "+mailName+"(ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment) values " +
                "("+ID+",'"+sendDate+"','"+sendName+"','"+receiveName+"','"+sendAddress+"','"+receiveAddress+"','"+mailContent+"','"+attachment+"')";
        try {
            state.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("ID重复");
            //e.printStackTrace();
            return false;
        }
    }


    //ID删除已发送邮件
    public boolean DeleteSend(int ID){
        String formName="al_send";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        return b;
    }
    //ID删除草稿箱邮件
    public boolean Deletedraft(int ID){
        String formName="draft";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        return b;
    }
    //ID删除垃圾箱邮件
    public boolean Deleterubbish(int ID){
        String formName="rubbish";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        return b;
    }
    public boolean DeleteData(String formName,String condition){
        String sql="delete from "+formName+" where "+condition;
        try{
            state.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("数据库删除出现错误");
            e.printStackTrace();
            return false;
        }
    }

    //修改数据库，传入 数据表名字  约束条件  想要修改的值(基础修改操作)
    public boolean ModifyData(String formName,String condition,String modified){
        String sql="update "+formName+" set "+modified+" where "+condition;
        try{
            state.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("数据库修改出现错误");
            e.printStackTrace();
            return false;
        }
    }

}
