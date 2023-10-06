package chat.server;

import chat.server.commands.*;
import chat.server.storage.Storage;
import common.Commands;
import common.Logger;
import protocol.ChatProtocol;
import protocol.Message;

import java.util.ArrayList;
import java.util.Date;

public class MessageServer<T extends Message> {
    private final ChatProtocol<T> chatProtocol;
    private final ArrayList<T> messages = new ArrayList<>();
    private final Storage<T> storage;
    private final ArrayList<String> clients = new ArrayList<>();
    private final Commands<T> authCommands;
    private final Commands<T> messageCommands;
    private final Logger logger;
    private boolean started;

    public MessageServer(ChatProtocol<T> chatProtocol, Storage<T> storage, Logger logger) {
        this.logger = logger;
        this.chatProtocol = chatProtocol;
        this.storage = storage;
        authCommands = Commands.link(new LoggerCommand<>(logger), new LoginCommand<>(chatProtocol, clients, logger, messages), new LogoutCommand<>(chatProtocol, clients));
        messageCommands = Commands.link(new LoggerCommand<>(logger), new CheckAuthCommand<>(chatProtocol, clients), new MessageToClientsCommand<>(chatProtocol), new AddToMessagesCommand<>(messages), new WriteMessageCommand<>(storage));
    }

    public void start() {
        if (started) {
            return;
        }
        started = true;
        logger.addToLog("Server started " + new Date());
        storage.read(messages);
        chatProtocol.subscribe(chatProtocol.MESSAGE_CHANNEL, chatProtocol.SERVER, (messageCommands::handle));
        chatProtocol.subscribe(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER, authCommands::handle);
    }

    public void disconnectClient(String client) {
        if (!clients.contains(client)) {
            logger.addToLog("Client " + client + " not found for reset");
            return;
        }
        chatProtocol.send(chatProtocol.AUTH_CHANNEL, client, (T) new Message(chatProtocol.SERVER, client, chatProtocol.LOGOUT));
        clients.remove(client);
    }

    public void disconnectAllClients() {
        while (!clients.isEmpty()) {
            disconnectClient(clients.get(0));
        }
    }

    public void stop() {
        chatProtocol.unsubscribe(chatProtocol.MESSAGE_CHANNEL, chatProtocol.SERVER);
        chatProtocol.unsubscribe(chatProtocol.AUTH_CHANNEL, chatProtocol.SERVER);
        disconnectAllClients();
        logger.addToLog("Server stopped " + new Date());
    }
}
