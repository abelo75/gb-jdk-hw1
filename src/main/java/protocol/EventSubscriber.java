package protocol;

public interface EventSubscriber <T> {
    void onMessage(String channel, T payload);
}
