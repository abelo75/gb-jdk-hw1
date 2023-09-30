package protocol;

public interface Subscribe<T> {
    void subscribe(String channel, String subscriberName, EventSubscriber<T> payload);
    void unsubscribe(String channel, String subscriberName);
}
