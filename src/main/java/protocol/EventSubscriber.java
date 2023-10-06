package protocol;

public interface EventSubscriber <T> {
    void onMessage(T payload);
}
