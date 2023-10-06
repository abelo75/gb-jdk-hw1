package chat.server.storage;

import java.util.List;

public interface Readable<T> {
    void read(List<T> content);
}
