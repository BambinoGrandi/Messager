//package sample.chat;
//
//import sample.controller.ChatController;
//import sample.controller.Controller;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//public class MyClient {
//    private Socket socket;
//    private DataInputStream in;
//    private DataOutputStream out;
//
////    private final static int PORT = 80;
////    private final static String HOST = "localhost";
//
//    public DataOutputStream getOut() {
//        return out;
//    }
//
//    public Socket getSocket() {
//        return socket;
//    }
//
//    private boolean authorized;
//
//    public boolean isAuthorized() {
//        return authorized;
//    }
//
//    public void setAuthorized(boolean authorized) {
//        this.authorized = authorized;
//    }
//
//    private String strLogin;
//    private String strPassword;
//
//    public MyClient( String strLogin, String strPassword) {
//        this.strLogin = strLogin;
//        this.strPassword = strPassword;
//    }
//
//    ChatController chatController = new ChatController();
//    Controller controller = new Controller();
//
//    public void authorizedChat (){
//
//        try {
//            socket = new Socket(HOST, PORT);
//            in = new DataInputStream(socket.getInputStream());
//            out = new DataOutputStream(socket.getOutputStream());
//            setAuthorized(false);
//
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        while (true) {
//                            String strFromServer = in.readUTF();
//                            if (strFromServer.startsWith("/authok")) {
//                                setAuthorized(true);
//                                break;
//                            }
////                            chatController.getChatTextArea().appendText(strFromServer + "\n");
//                        }
//                        while(true){
//                            String strFromServer = in.readUTF();
//                            if (strFromServer.equals("/end")){
//                                break;
//                            }
////                            chatController.getChatTextArea().appendText(strFromServer);
////                            chatController.getChatTextArea().appendText("\n");
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            t.setDaemon(true);
//            t.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void authBool (boolean authorizedTimeOut){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if(!socket.isClosed()){
//                    try {
//                        out.writeUTF(String.valueOf(authorizedTimeOut));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public void sendMessage (){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (socket.isClosed()) {
//                        String msg;
//                        messageChat.setText(msg = messageChat.getText());
//                        myClient.getOut().writeUTF(msg);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    public void onAuthClick(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (!socket.isClosed()){
//                        out.writeUTF("/auth " + strLogin + " " + strPassword);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//}
