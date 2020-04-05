import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyClient extends JFrame {

    private final static int SERVER_PORT = 80;
    private final static String SERVER_ADDR = "localhost";

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private JTextField msgInputField;
    private JTextArea chat;
    private JTextField loginField;
    private JTextField passField;

    private boolean authorized;

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }


    public void prepareGUI() {

        try {
            socket = new Socket(SERVER_ADDR, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String strFromServer = in.readUTF();
                            /*if (strFromServer.equals("false")) {
                                out.writeUTF("/end");
                                setAuthorized(false);
                                break;
                            }*/
                            if  (strFromServer.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            }
                            chat.append(strFromServer + "\n");
                        }
                        while (true) {
                            String strFromServer = in.readUTF();
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                break;
                            }
                            chat.append(strFromServer);
                            chat.append("\n");
                        }
                    } catch (Exception e) {
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

    public void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        if (!socket.isClosed()) {
                            String msg;
                            msgInputField.setText(msg = msgInputField.getText());
                            out.writeUTF(msg);
                            msgInputField.setText(null);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onAuthClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!socket.isClosed()) {
                        String lgn, psw;
                        loginField.setText(lgn = loginField.getText());
                        passField.setText(psw = passField.getText());
                        out.writeUTF("/auth " + lgn + " " + psw);
                        loginField.setText(null);
                        passField.setText(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public void MyWindow() {
        // Параметры окна
        setBounds(600, 300, 500, 720);
        setTitle("Клиент");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // поле для логина и пароля
        JPanel passPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        loginField = new JTextField();

        passField = new JPasswordField();

        JButton btnLogin = new JButton("Войти");
        passPanel.add(loginField,BorderLayout.NORTH);
        passPanel.add(passField, BorderLayout.SOUTH);
        inputPanel.add(passPanel, BorderLayout.CENTER);
        inputPanel.add(btnLogin, BorderLayout.EAST);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAuthClick();
            }
        });

        // Текстовое поле для вывода сообщений
        JPanel chatPanel = new JPanel();
        chat = new JTextArea();
        chat.setEditable(false);
        chat.setLineWrap(true);
        chatPanel.add(chat);


        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();

        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        btnSendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();

            }
        });
        msgInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Настраиваем действие на закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.writeUTF("/end");
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });

        add(inputPanel, BorderLayout.NORTH);
        add(chatPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);

//        setTitle("Messenger");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setBounds(300, 200, 720, 480);
//
//        JPanel panelChat = new JPanel();
//        chat = new JTextArea();
//        panelChat.setSize(500, 500);
//        panelChat.setBackground(Color.CYAN);
//        chat.setEditable(false);
//        chat.setLineWrap(true);
//        chat.setPreferredSize(new Dimension(640, 300));
//        panelChat.add(chat);
//
//        JPanel panelText = new JPanel();
//        panelText.setBackground(Color.cyan);
//        msgInputField = new JTextField();
//        JButton buttonToSent = new JButton();
//        msgInputField.setPreferredSize(new Dimension(500, 50));
//
//
//        buttonToSent.setText("Отправить");
//        buttonToSent.setPreferredSize(new Dimension(100, 50));
//
//        buttonToSent.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                sendMessage();
//                chat.setText(msgInputField.getText());
//                msgInputField.setText(null);
//            }
//        });
//        msgInputField.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode()==KeyEvent.VK_ENTER){
//                    sendMessage();
//                    chat.append(msgInputField.getText());
//                    chat.append("\n");
//                    msgInputField.setText(null);
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//
//            }
//        });
//
//        panelText.add(msgInputField);
//        panelText.add(buttonToSent);
//
//        add(panelChat, BorderLayout.CENTER);
//        add(panelText, BorderLayout.SOUTH);
//        setVisible(true);
    }


}
