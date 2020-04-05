public class ClientApp {
    public static void main(String[] args) throws Exception {
        MyClient myClient = new MyClient();
        myClient.MyWindow();
        myClient.prepareGUI();
        myClient.authBool(myClient.isAuthorized());
    }
}
