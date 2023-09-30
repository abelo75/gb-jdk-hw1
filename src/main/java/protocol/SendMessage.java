package protocol;

import java.util.ArrayList;

public interface SendMessage<T> {
    void send(String type, String receiver, T message);
    void sendList(String type, String receiver, ArrayList<T> messages);
}
