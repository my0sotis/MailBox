import java.sql.*;
import java.util.ArrayList;
import org.javatuples.*;

public class DataOperation {
    private Connection con=null;
    private Statement state=null;
    public ResultSet rst=null;
    //�����ʼ���ı�������ʱ��ı�
    public DataOperation(){
        try{
            //��������
            Class.forName("com.mysql.cj.jdbc.Driver");
            //getConnecting������������������mysql�����ݿ�
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/mail_jd?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false  ","root","123456");
            if(!con.isClosed()){
                System.out.println("Succeeded connecting to the Database");
            }
            //����statement ���� ������ִ��sql ���
            state=con.createStatement();
        }catch (ClassNotFoundException e){
            //���ݿ��������쳣����
            System.out.println("can't find the Driver!");
            e.printStackTrace();
        }catch (SQLException e){
            //���ݿ�����ʧ���쳣����
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace(); //handle exception
        }
    }



    //�����ѷ����е��ʼ�
    public MailObject SearchSend(int ID){
        String formName="al_send";
        String condition="ID="+ID+"";
        MailObject m=SearchTeamRank(formName,ID);
        return m;
    }
    //���Ҳݸ�����ʼ�
    public MailObject Searchdraft(int ID){
        String formName="draft";
        String condition="ID="+ID+"";
        MailObject m=SearchTeamRank(formName,ID);
        return m;
    }
    //�����������ʼ�
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
            System.out.println("��ͼ�����ѯ�ɼ�����");
            e.printStackTrace();
        }
        MailObject m=new MailObject(id,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
        return m;
    }

    //����ѷ���
    public void AddSend(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        AddData("al_send",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
    }
    //��Ӳݸ���
    public void Adddraft(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        AddData("draft",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailContent,attachment);
    }
    //���������
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
            System.out.println("ID�ظ�");
            //e.printStackTrace();
            return false;
        }
    }


    //IDɾ���ѷ����ʼ�
    public boolean DeleteSend(int ID){
        String formName="al_send";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        return b;
    }
    //IDɾ���ݸ����ʼ�
    public boolean Deletedraft(int ID){
        String formName="draft";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        return b;
    }
    //IDɾ���������ʼ�
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
            System.out.println("���ݿ�ɾ�����ִ���");
            e.printStackTrace();
            return false;
        }
    }

    //�޸����ݿ⣬���� ���ݱ�����  Լ������  ��Ҫ�޸ĵ�ֵ(�����޸Ĳ���)
    public boolean ModifyData(String formName,String condition,String modified){
        String sql="update "+formName+" set "+modified+" where "+condition;
        try{
            state.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            System.out.println("���ݿ��޸ĳ��ִ���");
            e.printStackTrace();
            return false;
        }
    }

}
