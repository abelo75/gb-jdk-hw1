package chat.server.commands;

import chat.server.storage.Writable;
import common.Commands;
import protocol.Message;

public class WriteMessageCommand<T extends Message> extends Commands<T> {
    private final Writable<T> writable;

    public WriteMessageCommand(Writable<T> writable) {
        this.writable = writable;
    }

    @Override
    public boolean handle(T message) {
        writable.write(message);
        return checkNext(message);
    }
}
