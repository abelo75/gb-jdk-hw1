package chat.server.storage;

import java.util.List;

public abstract class Storage<T> implements Writable<T>, Readable<T> {
    Class<T> type;
    public Storage(Class<T> type) {
        this.type = type;
    }
    @Override
    abstract public void read(List<T> content);

    @Override
    abstract public void write(T message);
}
