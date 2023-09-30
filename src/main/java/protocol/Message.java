package protocol;

import java.util.Date;

public class Message {
    private final String FIELD_DELIMITER = "~~";
    private final String sender;
    private final String message;

    private final String receiver;
    private final Date time;

    public Message(String sender, String receiver, String message, Date time) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
        this.time = time;
    }

    public Message(String packed) {
        String[] parts = packed.split(FIELD_DELIMITER);
        this.sender = parts[0];
        this.receiver = parts[1];
        this.message = parts[2];
        this.time = new Date(Long.parseLong(parts[3]));
    }

    public String getReceiver() {
        return receiver;
    }

    public Date getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + getTime() + "] " + getSender() + ">" + getMessage();
    }

    public String pack() {
        return getSender() + FIELD_DELIMITER + getReceiver() + FIELD_DELIMITER + getMessage() + FIELD_DELIMITER + getTime().getTime();
    }
}
