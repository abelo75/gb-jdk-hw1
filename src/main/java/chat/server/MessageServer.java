package chat.server;
import protocol.ChatProtocol;
import protocol.Message;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class MessageServer implements Readable, Writable {
    private final ChatProtocol<Message> chatProtocol;
    JTextArea textArea;
    private final ArrayList<Message> messages = new ArrayList<>();

    public MessageServer(ChatProtocol<Message> chatProtocol, JTextArea textArea) {
        this.textArea = textArea;
        this.chatProtocol = chatProtocol;
    }

    @Override
    public void read(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Message message = new Message(line);
                messages.add(message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void write(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            messages.forEach(message -> {
                try {
                    writer.write(message.pack());
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void write(String fileName, Message message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(message.pack());
            writer.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void start() {
        addToLog("Server started " + new Date());
        read("messages.txt");
        chatProtocol.subscribe(chatProtocol.MESSAGE_CHANNEL, chatProtocol.SERVER, (channel, payload) -> {
            chatProtocol.send(chatProtocol.MESSAGE_CHANNEL, chatProtocol.ALL_CLIENTS, payload);
            addToLog("[" + payload.getTime() + "] " + payload.getSender() + ">" + payload.getMessage());
            messages.add(payload);
            write("messages.txt", payload);
        });

        chatProtocol.subscribe(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER, (channel, payload) -> {
            chatProtocol.send(chatProtocol.MESSAGE_CHANNEL, chatProtocol.ALL_CLIENTS, new Message(chatProtocol.SERVER, chatProtocol.ALL_CLIENTS, payload.getSender() + " joined to chat", payload.getTime()));
            addToLog("auth request [" + payload.getTime() + "] " + payload.getSender() + ">" + payload.getMessage());
            chatProtocol.sendList(chatProtocol.MESSAGE_LIST_CHANNEL, payload.getSender(), messages);
        });
    }

    public void stop() {
        chatProtocol.unsubscribe(chatProtocol.MESSAGE_CHANNEL, chatProtocol.SERVER);
        chatProtocol.unsubscribe(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER);
        addToLog("Server stopped " + new Date());
    }

    void addToLog(String message) {
        textArea.append(message + "\n");
    }
}
