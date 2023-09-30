package chat.gui;

import protocol.ChatProtocol;
import protocol.Message;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Client extends JFrame {

    private final String SEND_BUTTON_TEXT = "Send";
    private final String LOGIN_BUTTON_TEXT = "Login";
    private final TextArea text = new TextArea("");
    private final TextField messageText = new TextField("");
    private final JTextField login = new JTextField("");
    private final JTextField password = new JPasswordField("");
    private final ChatProtocol<Message> chatProtocol;
    private final String name;

    Client(ChatProtocol<Message> chatProtocol, String name) {
        this.chatProtocol = chatProtocol;
        this.name = name;
        setTitle(name);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        createUI();
        addChatEventListeners();
    }

    @Override
    public String getName() {
        return name;
    }

    void addChatEventListeners() {
        chatProtocol.subscribe(chatProtocol.MESSAGE_CHANNEL, name, (eventType, eventPayload) -> {
            addMessage(eventPayload.toString());
        });

        chatProtocol.subscribeList(chatProtocol.MESSAGE_LIST_CHANNEL, name, (eventType, eventPayload) -> {
            eventPayload.forEach(e -> addMessage(e.toString()));
        });
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
        JButton buttonLogin = new JButton(LOGIN_BUTTON_TEXT);
        buttonLogin.addActionListener(e -> auth(login.getText(), password.getText()));
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
        JButton sendButton = new JButton(SEND_BUTTON_TEXT);
        sendButton.addActionListener(e -> {
            sendMessage(messageText.getText());
            messageText.setText("");
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);

        return bottomPanel;
    }

    void sendMessage(String message) {
        chatProtocol.send(chatProtocol.MESSAGE_CHANNEL, chatProtocol.SERVER, new Message(getName(), chatProtocol.SERVER, message, new Date()));
    }

    void addMessage(String message) {
        text.append(message + "\n");
    }

    void auth(String login, String password) {
        chatProtocol.send(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER, new Message(getName(), chatProtocol.SERVER, login + ":" + password, new Date()));
    }

}
