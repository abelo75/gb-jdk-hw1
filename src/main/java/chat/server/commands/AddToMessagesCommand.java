package chat.server.commands;

import common.Commands;
import protocol.Message;

import java.util.List;

public class AddToMessagesCommand<T extends Message> extends Commands<T> {
    private final List<T> messages;

    public AddToMessagesCommand(List<T> messages) {
        this.messages = messages;
    }
    @Override
    public boolean handle(T message) {
        messages.add(message);
        return checkNext(message);
    }
}
