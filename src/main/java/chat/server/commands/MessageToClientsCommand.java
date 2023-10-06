package chat.server.commands;
import common.Commands;
import protocol.ChatProtocol;
import protocol.Message;

public class MessageToClientsCommand<T extends Message> extends Commands<T> {
    private final ChatProtocol<T> chatProtocol;

    public MessageToClientsCommand(ChatProtocol<T> chatProtocol) {
        this.chatProtocol = chatProtocol;
    }

    @Override
    public boolean handle(T message) {
        chatProtocol.send(chatProtocol.MESSAGE_CHANNEL, chatProtocol.ALL_CLIENTS, message);
        return checkNext(message);
    }
}
