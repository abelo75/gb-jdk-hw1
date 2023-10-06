package common;

import protocol.Message;

public abstract class Commands<T extends Message> implements Command<T> {
    private Commands<T> next;

    @Override
    public boolean checkNext(T message) {
        if (next == null) {
            return true;
        }
        return next.handle(message);
    }

    @SafeVarargs
    public static <P extends Message> Commands<P> link(Commands<P> first, Commands<P>... rest) {
        Commands<P> last = first;
        for (Commands<P> command : rest) {
            last.next = command;
            last = command;
        }
        return first;
    }
}
