package protocol;

import java.util.ArrayList;

public interface SubscribeList<T> {
    void subscribeList(String channel, String subscriberName, EventSubscriber<ArrayList<T>> payload);
}
