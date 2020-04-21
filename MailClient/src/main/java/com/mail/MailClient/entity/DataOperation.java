package com.mail.MailClient.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 赵彪 高战立
 */

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
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/birthday?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false  ","root","123456");
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
        String sendDate=null,sendName=null,receiveName=null,sendAddress=null,receiveAddress=null,mailSubject=null,mailContent=null;
        String[] attachment = null;
        try{
            rst=state.executeQuery(sql);
            while(rst.next()){
                sendDate=rst.getString("sendDate");
                sendName=rst.getString("sendName");
                receiveName=rst.getString("receiveName");
                sendAddress=rst.getString("sendAddress");
                receiveAddress=rst.getString("receiveAddress");
                mailSubject=rst.getString("mailSubject");
                mailContent=rst.getString("mailContent");
                attachment=rst.getString("attachment").split("#");
            }
        }catch (SQLException e){
            System.out.println("视图队伍查询成绩错误");
            e.printStackTrace();
        }
        MailObject m=new MailObject(id,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailSubject,mailContent,attachment);
        return m;
    }

    //查找所有已发送邮件
    public List<MailObject> SearchAllSend(){
        return SearchAll("al_send");
    }
    //查找所有草稿箱内邮件
    public List<MailObject> SearchAllDraft(){
        return SearchAll("draft");
    }
    //查找所有垃圾箱内邮件
    public List<MailObject> SearchAllRubbish(){
        return SearchAll("rubbish");
    }
    public List<MailObject> SearchAll(String mailName){
        String sql = "select * from " + mailName;
        List<MailObject> list = new ArrayList<>();
        MailObject m = null;
        String id = null,sendDate=null,sendName=null,receiveName=null,sendAddress=null,receiveAddress=null,mailSubject=null,mailContent=null;
        String[] attachment=null;
        try{
            rst=state.executeQuery(sql);
            while(rst.next()){
                id = rst.getString("ID");
                sendDate=rst.getString("sendDate");
                sendName=rst.getString("sendName");
                receiveName=rst.getString("receiveName");
                sendAddress=rst.getString("sendAddress");
                receiveAddress=rst.getString("receiveAddress");
                mailSubject=rst.getString("mailSubject");
                mailContent=rst.getString("mailContent");
                attachment=rst.getString("attachment").split("#");
                m=new MailObject(Integer.parseInt(id),sendDate,sendName,receiveName,sendAddress,receiveAddress,mailSubject,mailContent,attachment);
                list.add(m);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }


    //添加已发送
    public void AddSend(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailSubject,String mailContent,String attachment){
        AddData("al_send",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailSubject,mailContent,attachment);
    }
    //添加草稿箱
    public void Adddraft(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailSubject,String mailContent,String attachment){
        AddData("draft",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailSubject,mailContent,attachment);
    }
    //添加垃圾箱
    public void Addrubbish(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailSubject,String mailContent,String attachment){
        AddData("rubbish",ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailSubject,mailContent,attachment);
    }
    public boolean AddData(String mailName,int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailSubject,String mailContent,String attachment){
        String sql="insert into "+mailName+"(ID,sendDate,sendName,receiveName,sendAddress,receiveAddress,mailSubject,mailContent,attachment) values " +
            "("+ID+",'"+sendDate+"','"+sendName+"','"+receiveName+"','"+sendAddress+"','"+receiveAddress+"','"+mailSubject+"','"+mailContent+"','"+attachment+"')";
        try {
            state.executeUpdate(sql);
            reset();
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
        reset();
        return b;
    }
    //ID删除草稿箱邮件
    public boolean Deletedraft(int ID){
        String formName="draft";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        reset();
        return b;
    }
    //ID删除垃圾箱邮件
    public boolean Deleterubbish(int ID){
        String formName="rubbish";
        String condition="ID="+ID+"";
        boolean b=DeleteData(formName,condition);
        reset();
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

    //MailObject类型转换为BriefMail
    public List<BriefMail> transmit(List<MailObject> list){
        List<BriefMail> briefMails =  new ArrayList<>();
        BriefMail briefMail = null;
        for(MailObject mailObject:list){
            briefMail = new BriefMail(mailObject.ID);
            briefMail.setNum(mailObject.ID);
            briefMail.setDatetime(mailObject.date);
            briefMail.setSenderEmail(mailObject.sendAddress);
            briefMail.setSenderName(mailObject.sendName);
            briefMail.setReceiverName(mailObject.receiveName);
            briefMail.setReceiverEmail(mailObject.receiver);
            briefMail.setSubject(mailObject.subject);
            briefMails.add(briefMail);
        }
        return briefMails;
    }
    //重置序号
    public void reset(){
        for(int type = 0; type < 3; type++){
            String form = "";
            switch(type){
                case 0:
                    form="al_send";
                    break;
                case 1:
                    form="draft";
                    break;
                case 2:
                    form="rubbish";
                    break;
            }
            List<MailObject> mailObjects = SearchAll(form);
            int length = SearchAll(form).size();
            for(int i = 1; i <= length; i++){
                String sql = "update " + form + " set id = " + i + " where id = " + mailObjects.get(i-1).ID;
                try{
                    state.executeUpdate(sql);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //返回DetailedMail
    public DetailedMail transmitToDetail(MailObject mailObject){
        DetailedMail detailedMail = new DetailedMail();
        BriefMail briefMail = new BriefMail(mailObject.ID);
        briefMail.setNum(mailObject.ID);
        briefMail.setDatetime(mailObject.date);
        briefMail.setSenderEmail(mailObject.sendAddress);
        briefMail.setSenderName(mailObject.sendName);
        briefMail.setReceiverName(mailObject.receiveName);
        briefMail.setReceiverEmail(mailObject.receiver);
        briefMail.setSubject(mailObject.subject);

        detailedMail.setAttachments(Arrays.asList(mailObject.attachments));
        detailedMail.setContent(new StringBuilder(mailObject.content));
        detailedMail.setBriefInfo(briefMail);
        return detailedMail;
    }

    //根据类型查找不同数据表，获取邮件简略信息
    public List<BriefMail> getBriefMail(int type){
        List<BriefMail> list = null;
        switch (type){
            case 0:
                list = transmit(SearchAllSend());
                break;
            case 1:
                list = transmit(SearchAllDraft());
                break;
            case 2:
                list = transmit(SearchAllRubbish());
                break;
        }
        return list;
    }

    //根据类型查找不同数据表，获得某邮件详细内容
    public DetailedMail getDetailMail(int no, int type){
        DetailedMail detailedMail = null;
        switch (type){
            case 0:
                detailedMail = transmitToDetail(SearchSend(no));
                break;
            case 1:
                detailedMail = transmitToDetail(Searchdraft(no));
                break;
            case 2:
                detailedMail = transmitToDetail(Searchrubbish(no));
                break;
        }
        return detailedMail;
    }

    //根据类型查找不同数据表, 删除邮件
    public Result deleteMails(Integer[] list, int type){
        MailObject m = null;
        switch (type){
            case 0:  //已发送
                for(int i:list){
                    m = SearchSend(i);
                    StringBuilder attachment = new StringBuilder("");
                    for(String s:m.attachments)
                        attachment.append(s).append("#");
                    int index = SearchAllRubbish().size(); //获取所有已发送的数量
                    Addrubbish(index+1,m.date,m.sendName,m.receiveName,m.sendAddress,m.receiver,m.subject,m.content,attachment.toString());
                    DeleteSend(i);
                }
                break;
            case 1:  //草稿
                for(int i:list){
                    m = Searchdraft(i);
                    StringBuilder attachment = new StringBuilder("");
                    for(String s:m.attachments)
                        attachment.append(s).append("#");
                    int index = SearchAllRubbish().size(); //获取所有已发送的数量
                    Addrubbish(index+1,m.date,m.sendName,m.receiveName,m.sendAddress,m.receiver,m.subject,m.content,attachment.toString());
                    Deletedraft(i);
                }
                break;
            case 2:
                for(int i:list){
                    Deleterubbish(i);
                }
                break;
        }
        return new Result(200);
    }

    //垃圾箱中的邮件移动到已发送或者草稿箱
    public Result moveMails(Integer[] list, int type){
        MailObject m = null;
        switch (type){
            case 0:
                for(int i:list){
                    m = Searchrubbish(i);
                    StringBuilder attachment = new StringBuilder("");
                    for(String s:m.attachments)
                        attachment.append(s).append("#");
                    int index = SearchAllSend().size(); //获取所有已发送的数量
                    AddSend(index+1,m.date,m.sendName,m.receiveName,m.sendAddress,m.receiver,m.subject,m.content,attachment.toString());
                    Deleterubbish(i);
                }
                break;
            case 1:
                for(int i:list){
                    m = Searchrubbish(i);
                    StringBuilder attachment = new StringBuilder("");
                    for(String s:m.attachments)
                        attachment.append(s).append("#");
                    int index = SearchAllDraft().size(); //获取所有已发送的数量
                    Adddraft(index+1,m.date,m.sendName,m.receiveName,m.sendAddress,m.receiver,m.subject,m.content,attachment.toString());
                    Deleterubbish(i);
                }
                break;
        }
        return new Result(200);
    }

    //将briefmaill类型邮件添加至垃圾箱
    public void moveMails(DetailedMail detailedMail){
        BriefMail b = detailedMail.getBriefInfo();
        StringBuilder attachment = new StringBuilder("");
        for(String s:detailedMail.getAttachments())
            attachment.append(s).append("#");
        String a = b.getDatetime();
        Addrubbish(b.getNum(),b.getDatetime(),b.getSenderName(),b.getReceiverName(),b.getSenderEmail(),b.getReceiverEmail(),b.getSubject(),detailedMail.getContent(),attachment.toString());
    }
}
