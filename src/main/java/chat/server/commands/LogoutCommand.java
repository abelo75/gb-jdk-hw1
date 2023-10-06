package chat.server.commands;

import common.Commands;
import protocol.ChatProtocol;
import protocol.Message;

import java.util.ArrayList;

public class LogoutCommand<T extends Message> extends Commands<T> {
    private final ArrayList<String> clients;
    private final ChatProtocol<T> protocol;

    public LogoutCommand(ChatProtocol<T> protocol, ArrayList<String> clients) {
        this.protocol = protocol;
        this.clients = clients;
    }

    @Override
    public boolean handle(T message) {
        if (!message.getMessage().equals(protocol.LOGOUT)) {
            return checkNext(message);
        }
        if (clients.contains(message.getSender())) {
            protocol.send(protocol.AUTH_CHANNEL, message.getSender(), (T) new Message(protocol.SERVER, message.getSender(), protocol.LOGOUT, message.getTime()));
            clients.remove(message.getSender());
            protocol.send(protocol.MESSAGE_CHANNEL, protocol.ALL_CLIENTS, (T) new Message(protocol.SERVER, protocol.ALL_CLIENTS, message.getSender() + " left chat", message.getTime()));
            return true;
        }
        protocol.send(protocol.AUTH_CHANNEL, message.getSender(), (T) new Message(protocol.SERVER, message.getSender(), protocol.UNAUTHORIZED, message.getTime()));
        return true;
    }
}
