package common;

import protocol.Message;

public interface Command<T extends Message> {
    boolean handle(T message);
    boolean checkNext(T message);
}
