package chat.client;

import common.Logger;
import protocol.ChatProtocol;
import protocol.Message;

import java.util.HashMap;

public class MessageClient<T extends Message> {

    private final Logger logger;
    private final ChatProtocol<T> chatProtocol;
    private final String name;
    private final HashMap<String, String> authMessages = new HashMap<>();


    public MessageClient(ChatProtocol<T> chatProtocol, Logger logger, String name) {
        this.logger = logger;
        this.chatProtocol = chatProtocol;
        this.name = name;
        initAuthMessages();
        addListeners();
    }

    private void initAuthMessages() {
        authMessages.put(chatProtocol.ALREADY_LOGGED, "You are already logged in");
        authMessages.put(chatProtocol.UNAUTHORIZED, "You are not authorized");
        authMessages.put(chatProtocol.LOGIN, "You are logged in");
        authMessages.put(chatProtocol.LOGOUT, "You are logged out");
    }

    public void addListeners() {
        chatProtocol.subscribe(
                chatProtocol.MESSAGE_CHANNEL,
                name,
                (eventPayload) -> logger.addToLog(eventPayload.toString())
        );
        chatProtocol.subscribeList(chatProtocol.MESSAGE_LIST_CHANNEL, name, (eventPayload) -> eventPayload.forEach(message -> logger.addToLog(message.toString())));
        chatProtocol.subscribe(chatProtocol.AUTH_CHANNEL, name, (eventPayload) -> {
            String message = eventPayload.getMessage();
            String translatedMessage = authMessages.get(message);
            logger.addToLog(translatedMessage == null ? message : translatedMessage);
        });
    }

    public void removeListeners() {
        chatProtocol.unsubscribe(chatProtocol.MESSAGE_CHANNEL, name);
        chatProtocol.unsubscribe(chatProtocol.MESSAGE_LIST_CHANNEL, name);
        chatProtocol.unsubscribe(chatProtocol.AUTH_CHANNEL, name);
    }

    public void login(String login) {
        chatProtocol.send(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER, (T) new Message(login, chatProtocol.SERVER, chatProtocol.LOGIN));
    }

    public void logout() {
        chatProtocol.send(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER, (T) new Message(name, chatProtocol.SERVER, chatProtocol.LOGOUT));
    }

    public void sendMessage(String message) {
        chatProtocol.send(chatProtocol.MESSAGE_CHANNEL, chatProtocol.SERVER, (T) new Message(name, chatProtocol.SERVER, message));
    }
}
