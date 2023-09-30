package chat.gui;

import protocol.EventBusChatProtocol;
import protocol.Message;

import javax.swing.*;

public class Starter extends JFrame {
    private final String CLIENT_NAME = "client";
    JButton serverButton = new JButton("Server");
    JButton clientButton = new JButton("Client");
    private int clientNumber = 1;
    private final EventBusChatProtocol<Message> chatProtocol = EventBusChatProtocol.getInstance();
    Starter() {
        setTitle("Chat starter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        createUI();
    }

    void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        serverButton.addActionListener(e -> {
            serverButton.setEnabled(false);
            clientButton.setEnabled(true);
            new Server(chatProtocol);
        });

        clientButton.addActionListener(e -> new Client(chatProtocol, CLIENT_NAME + clientNumber++));
        clientButton.setEnabled(false);
        panel.add(serverButton);
        panel.add(clientButton);
        add(panel);
    }
    public static void main(String[] args) {
        new Starter();
    }
}
