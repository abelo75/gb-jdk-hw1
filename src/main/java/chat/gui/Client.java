package chat.gui;

import chat.client.MessageClient;
import common.Logger;
import protocol.ChatProtocol;
import protocol.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Client extends JFrame implements Logger {

    private final static String SEND_BUTTON_TEXT = "Send";
    private final static String LOGIN_BUTTON_TEXT = "Login";
    private final TextArea text = new TextArea("");
    private final TextField messageText = new TextField("");
    private final JTextField login = new JTextField("");
    private final JTextField password = new JPasswordField("");
    private final String name;
    private JButton buttonLogin;
    private JButton sendButton;
    private final MessageClient<Message> messageClient;


    Client(ChatProtocol<Message> chatProtocol, String name) {
        this.name = name;
        messageClient = new MessageClient<>(chatProtocol, this, name);
        setTitle(name);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        createUI();
        addListeners();
    }

    void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                messageClient.logout();
                messageClient.removeListeners();
                super.windowClosing(e);
            }
        });
        buttonLogin.addActionListener(e -> messageClient.login(login.getText()));
        sendButton.addActionListener(e -> sendMessage());
        messageText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        messageClient.sendMessage(messageText.getText());
        messageText.setText("");
    }

    void createUI() {
        add(getTopPanel(), BorderLayout.NORTH);
        add(getMiddlePanel(), BorderLayout.CENTER);
        add(getBottomPanel(), BorderLayout.SOUTH);
    }

    Component getTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 3, 2, 2));
        JTextField address = new JTextField("localhost");
        JTextField port = new JTextField("10101");
        topPanel.add(address);
        topPanel.add(port);
        topPanel.add(new JPanel());
        login.setText(name);
        topPanel.add(login);
        password.setText("password");
        topPanel.add(password);
        buttonLogin = new JButton(LOGIN_BUTTON_TEXT);
        topPanel.add(buttonLogin);
        return topPanel;
    }

    Component getMiddlePanel() {
        JPanel panelMiddle = new JPanel(new GridLayout(1, 1, 2, 2));
        panelMiddle.add(text);
        add(panelMiddle, BorderLayout.CENTER);
        return panelMiddle;
    }

    Component getBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageText, BorderLayout.CENTER);
        sendButton = new JButton(SEND_BUTTON_TEXT);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        return bottomPanel;
    }

    @Override
    public void addToLog(String message) {
        text.append(message + "\n");
    }
}
