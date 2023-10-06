package chat.server.storage;

public interface Writable<T> {
    void write(T message);
}
