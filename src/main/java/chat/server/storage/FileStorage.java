package chat.server.storage;
import protocol.Message;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FileStorage<T extends Message> extends Storage<T>{
    private final String fileName;
    public FileStorage(Class<T> type, String fileName) {
        super(type);
        this.fileName = fileName;
    }

    @Override
    public void read(List<T> content) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T message = type.getDeclaredConstructor().newInstance();
                message.unpack(line);
                content.add(message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void write(T message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(message.pack());
            writer.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
