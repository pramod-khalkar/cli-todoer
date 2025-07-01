package org.clitodoer.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class FileTodoRepository {
    private static final String FILE_PATH = "todos.txt";

    public List<String> loadTodos() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Failed to read todos from file.");
            return new ArrayList<>();
        }
    }

    public void saveTodo(String task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(task);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write todo to file.");
        }
    }
}
