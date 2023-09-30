package protocol;

public abstract class ChatProtocol <T> implements SendMessage<T>, Subscribe<T>, SubscribeList<T> {
    public final String MESSAGE_CHANNEL = "message";
    public final String MESSAGE_LIST_CHANNEL = "history";
    public final String AUTH_CHANNEL = "auth";
    public final String ALL_CLIENTS = "[clients]";
    public final String ALL = "*";
    public final String SERVER = "server";

}
