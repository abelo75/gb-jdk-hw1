package protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventBusChatProtocol<T extends Message> extends ChatProtocol<T> {
    private static final EventBusChatProtocol<?> instance = new EventBusChatProtocol<>();
    private final Map<String, Map<String, EventSubscriber<T>>> subscribers = new HashMap<>();
    private final Map<String, Map<String, EventSubscriber<ArrayList<T>>>> historySubscribers = new HashMap<>();

    private EventBusChatProtocol() {
    }

    public static <T extends Message> EventBusChatProtocol<T> getInstance() {
        return (EventBusChatProtocol<T>) instance;
    }

    @Override
    public void send(String type, String receiver, T message) {
        Map<String, EventSubscriber<T>> eventSubscribers = subscribers.get(type);
        if (eventSubscribers != null) {

            for (String key : eventSubscribers.keySet()) {
                if (key.equals(receiver) || receiver.equals(ALL) || (receiver.equals(ALL_CLIENTS) && !key.equals(SERVER))) {
                    eventSubscribers.get(key).onMessage(message);
                }
            }
        }
    }

    @Override
    public void unsubscribe(String type, String subscriberName) {
        Map<String, EventSubscriber<T>> eventSubscribers = subscribers.get(type);
        if (eventSubscribers != null) {
            eventSubscribers.remove(subscriberName);
        }
    }

    @Override
    public void subscribe(String channel, String subscriberName, EventSubscriber<T> payload) {
        subscribers.computeIfAbsent(channel, k -> new HashMap<>()).put(subscriberName, payload);
    }

    @Override
    public void subscribeList(String channel, String subscriberName, EventSubscriber<ArrayList<T>> payload) {
        historySubscribers.computeIfAbsent(channel, k -> new HashMap<>()).put(subscriberName, payload);
    }

    @Override
    public void sendList(String type, String receiver, ArrayList<T> messages) {
        Map<String, EventSubscriber<ArrayList<T>>> eventSubscribers = historySubscribers.get(type);
        if (eventSubscribers != null) {

            for (String key : eventSubscribers.keySet()) {
                if (key.equals(receiver) || receiver.equals(ALL)) {
                    eventSubscribers.get(key).onMessage(messages);
                }
            }
        }
    }
}
