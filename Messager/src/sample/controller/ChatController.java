package sample.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.chat.HistoryMessage;

public class ChatController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnICloseSocket;

    @FXML
    private TextField messageChat;

    @FXML
    private TextField fieldLogin;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private TextArea chatTextArea;

    @FXML
    private Button btnSendMessage;

    @FXML
    private TextArea countPerson;

    @FXML
    private Button btnIAuth;

    @FXML
    private AnchorPane authPane;


    @FXML
    private Button btnNick;

    //поля для окна смены ника
    @FXML
    private Button btnNewNick;

    @FXML
    private TextField fieldNewNick;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final static int PORT = 80;
    private final static String HOST = "localhost";

    private boolean authorized;

    HistoryMessage historyMessage = new HistoryMessage();
    private String nick;

    public ChatController() {
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public void sendMessage (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!socket.isClosed()) {
                        String msg;
                        messageChat.setText(msg = messageChat.getText());
                        out.writeUTF(msg);
                        messageChat.setText(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onAuthClick(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!socket.isClosed()){
                        String strLogin, strPassword;
                        fieldLogin.setText(strLogin = fieldLogin.getText());
                        fieldPassword.setText(strPassword = fieldPassword.getText());
                        out.writeUTF("/auth " + strLogin + " " + strPassword);
                        fieldLogin.clear();
                        fieldPassword.clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void authBool (boolean authorizedTimeOut) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!socket.isClosed()){
                    try {
                        out.writeUTF(String.valueOf(authorizedTimeOut));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void authorizedPerson(){
        try {
            socket = new Socket(HOST, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String strFromServer = in.readUTF();
                            if (strFromServer.startsWith("/authok")) {
                                String[] tokens = strFromServer.split("\\s");
                                nick = tokens [1];
                                setAuthorized(true);
                                break;
                            }

                            chatTextArea.appendText(strFromServer + "\n");
                            historyMessage.addMessageInHistory(strFromServer);
                        }
                        while(true){
                            String strFromServer = in.readUTF();
                            if (strFromServer.equals("/end")){
                                break;
                            }
                            chatTextArea.appendText(strFromServer);
                            chatTextArea.appendText("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadMessageFromHistory(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chatTextArea.appendText(historyMessage.loadMessageFromHistory ());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void newNick (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!socket.isClosed()){
                    String nick;
                    nick = fieldNewNick.getText();
                    fieldNewNick.setText("/n " + nick);
                }
            }
        });
    }

    @FXML
    void initialize() {
        btnIAuth.setOnAction(event -> {
            authorizedPerson();
            authBool(isAuthorized());
            onAuthClick();
            loadMessageFromHistory();
            try {
                historyMessage.createFile(nick);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btnSendMessage.setOnAction(event -> {
            sendMessage();
        });

        btnNick.setOnAction(event -> {
            newNick();
        });


    }
}