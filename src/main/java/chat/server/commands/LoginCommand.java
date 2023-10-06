package chat.server.commands;

import common.Logger;
import common.Commands;
import protocol.ChatProtocol;
import protocol.Message;

import java.util.ArrayList;

public class LoginCommand<T extends Message> extends Commands<T> {
    private final ArrayList<String> clients;
    private final ChatProtocol<T> protocol;
    private final Logger logger;
    private final ArrayList<T> messages;

    public LoginCommand(ChatProtocol<T> protocol, ArrayList<String> clients, Logger logger, ArrayList<T> messages) {
        this.logger = logger;
        this.protocol = protocol;
        this.clients = clients;
        this.messages = messages;
    }

    @Override
    public boolean handle(T message) {
        if (!message.getMessage().equals(protocol.LOGIN)) {
            return checkNext(message);
        }
        if (clients.contains(message.getSender())) {
            protocol.send(protocol.AUTH_CHANNEL, message.getSender(), (T) new Message(protocol.SERVER, message.getSender(), protocol.ALREADY_LOGGED, message.getTime()));
            return true;
        }
        clients.add(message.getSender());
        protocol.send(protocol.AUTH_CHANNEL, message.getSender(), (T) new Message(protocol.SERVER, message.getSender(), protocol.UNAUTHORIZED, message.getTime()));
        protocol.send(protocol.MESSAGE_CHANNEL, protocol.ALL_CLIENTS, (T) new Message(protocol.SERVER, protocol.ALL_CLIENTS, message.getSender() + " joined to chat", message.getTime()));
        protocol.sendList(protocol.MESSAGE_LIST_CHANNEL, message.getSender(), messages);
        logger.addToLog("[" + message.getTime() + "] " + message.getSender() + " joined to chat");
        return true;
    }
}
