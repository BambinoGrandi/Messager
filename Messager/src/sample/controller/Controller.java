//package sample.controller;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ResourceBundle;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import sample.chat.MyClient;
//
//public class Controller {
//
//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private URL location;
//
//    @FXML
//    private TextField fieldHost;
//
//    @FXML
//    private PasswordField fieldPassword;
//
//    public PasswordField getFieldPassword() {
//        return fieldPassword;
//    }
//
//    public TextField getFieldLogin() {
//        return fieldLogin;
//    }
//
//    @FXML
//    private TextField fieldPort;
//
//    @FXML
//    private TextField fieldLogin;
//
//    @FXML
//    private Button btnInputChat;
//
//    MyClient myClient;
//
//    @FXML
//    void initialize() {
//        btnInputChat.setOnAction(event ->  {
////            myClient = new MyClient();
//            myClient.authorizedChat();
//            myClient.onAuthClick();
//
//                btnInputChat.getScene().getWindow().hide();
//
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(getClass().getResource("/sample/view/chat.fxml"));
//
//                try {
//                    loader.load();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Parent root = loader.getRoot();
//                Stage stage = new Stage();
//                stage.setScene(new Scene(root));
//                stage.show();
//
//        });
//
//
//    }
//}