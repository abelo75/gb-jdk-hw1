package chat.server.commands;

import common.Logger;
import common.Commands;
import protocol.Message;

public class LoggerCommand<T extends Message> extends Commands<T> {
    private final Logger logger;

    public LoggerCommand(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean handle(T message) {
        logger.addToLog("[" + message.getTime() + "] " + message.getSender() + " -> " + message.getReceiver() + ", " + message.getMessage());
        return checkNext(message);
    }
}
