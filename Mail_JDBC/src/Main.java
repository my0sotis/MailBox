public class Main {

    public static void main(String[] args) {

        DataOperation dataOperation=new DataOperation();
        dataOperation.Adddraft(1,"2","2","2","2","2","2","2");
        MailObject m=dataOperation.Searchdraft(1);
        System.out.println(m.sendDate);
        dataOperation.Deletedraft(1);
    }
}
