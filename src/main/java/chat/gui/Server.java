package chat.gui;

import common.Logger;
import chat.server.storage.FileStorage;
import chat.server.MessageServer;
import protocol.ChatProtocol;
import protocol.Message;

import javax.swing.*;
import java.awt.*;

public class Server extends JFrame implements Logger {
    private static final String START_LABEL = "Start";
    private static final String STOP_LABEL = "Stop";
    private static final String FILENAME = "messages.txt";


    JButton buttonStart, buttonStop;
    JTextArea textArea;
    private boolean isStarted = false;

    private final MessageServer<Message> messageServer;

    public Server(ChatProtocol<Message> chatProtocol) {
        super("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        createUserInterface();
        setVisible(true);
        FileStorage<Message> storage = new FileStorage<>(Message.class, FILENAME);
        messageServer = new MessageServer<>(chatProtocol, storage, this);
    }

    public void setStarted(boolean started) {
        if (isStarted == started) {
            return;
        }
        isStarted = started;
        buttonStart.setEnabled(!isStarted);
        buttonStop.setEnabled(isStarted);
        if (isStarted) {
            startServer();
        } else {
            stopServer();
        }
    }

    private void startServer() {
        messageServer.start();
    }

    private void stopServer() {
         messageServer.stop();
    }

    Component createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        buttonStart = new JButton(START_LABEL);
        buttonStop = new JButton(STOP_LABEL);
        buttonStop.setEnabled(false);

        panel.add(buttonStart);
        panel.add(buttonStop);
        buttonStart.addActionListener(e -> setStarted(true));
        buttonStop.addActionListener(e -> setStarted(false));
        return panel;
    }

    Component createTextArea() {
        textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        return new JScrollPane(textArea);
    }

    void createUserInterface() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        panel.add(createTextArea());
        add(panel);
    }

    @Override
    public void addToLog(String message) {
        textArea.append(message + "\n");
    }
}
