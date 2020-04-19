public class MailObject {
    public  int ID;
    public String sendDate;
    public String sendName;
    public String receiveName;
    public String sendAddress;
    public String receiveAddress;
    public String mailContent;
    public String attachment;
    public MailObject(int ID,String sendDate,String sendName,String receiveName,String sendAddress,String receiveAddress,String mailContent,String attachment){
        this.ID=ID;
        this.sendDate=sendDate;
        this.sendName=sendName;
        this.receiveName=receiveName;
        this.sendAddress=sendAddress;
        this.receiveAddress=receiveAddress;
        this.mailContent=mailContent;
        this.attachment=attachment;
    }
}
