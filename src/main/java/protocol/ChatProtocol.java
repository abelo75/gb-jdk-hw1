package protocol;

public abstract class ChatProtocol <T extends Message> implements SendMessage<T>, Subscribe<T>, SubscribeList<T> {
    public final String MESSAGE_CHANNEL = "message";
    public final String MESSAGE_LIST_CHANNEL = "history";
    public final String AUTH_CHANNEL = "auth";
    public final String ALL_CLIENTS = "[clients]";
    public final String ALL = "*";
    public final String SERVER = "server";
    public final String ALREADY_LOGGED = "already_logged";
    public final String UNAUTHORIZED = "not_authorized";
    public final String LOGIN = "login";
    public final String LOGOUT = "logout";
}
