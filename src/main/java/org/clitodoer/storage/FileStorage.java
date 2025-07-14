package org.clitodoer.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author : Pramod Khalkar
 * @since : 12/07/25, Sat
 */
public class FileStorage implements Storage {
  private final Path filePath;
  private static final ObjectMapper mapper =
      new ObjectMapper().registerModule(new JavaTimeModule());

  public FileStorage(Path filePath) {
    this.filePath = filePath;
  }

  @Override
  public FileData read() {
    if (!Files.exists(filePath)) {
      throw new RuntimeException("Failed to initialize file storage: " + filePath);
    } else {
      try {
        if (Files.size(filePath) == 0) {
          return new FileData();
        }
        return mapper.readValue(
            Files.newInputStream(filePath, StandardOpenOption.READ), FileData.class);
      } catch (IOException ex) {
        throw new RuntimeException("Failed to read file data: " + ex.getMessage(), ex);
      }
    }
  }

  @Override
  public void write(FileData fileData) {
    if (!Files.exists(filePath)) {
      throw new RuntimeException("Failed to initialize file storage: " + filePath);
    } else {
      try {
        mapper.writeValue(Files.newBufferedWriter(filePath, StandardOpenOption.WRITE), fileData);
      } catch (IOException ex) {
        throw new RuntimeException("Failed to write file data: " + ex.getMessage(), ex);
      }
    }
  }
}
