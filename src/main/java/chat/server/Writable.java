package chat.server;

import protocol.Message;

public interface Writable {
    void write(String fileName);
    void write(String fileName, Message message);
}
