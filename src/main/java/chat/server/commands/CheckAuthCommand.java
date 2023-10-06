package chat.server.commands;

import common.Commands;
import protocol.ChatProtocol;
import protocol.Message;

import java.util.ArrayList;

public class CheckAuthCommand<T extends Message> extends Commands<T> {
    private final ArrayList<String> users;
    private final ChatProtocol<T> chatProtocol;

    public CheckAuthCommand(ChatProtocol<T> chatProtocol, ArrayList<String> users) {
        this.users = users;
        this.chatProtocol = chatProtocol;
    }

    @Override
    public boolean handle(T message) {
        if (users.contains(message.getSender())) {
            return checkNext(message);
        }
        chatProtocol.send(chatProtocol.AUTH_CHANNEL, message.getSender(), (T) new Message(chatProtocol.SERVER, message.getSender(), chatProtocol.UNAUTHORIZED, message.getTime()));
        return true;
    }
}
