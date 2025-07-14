package org.clitodoer.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.clitodoer.utils.Os;

/**
 * @author : Pramod Khalkar
 * @since : 12/07/25, Sat
 */
public class DefaultFilePathProvider implements FilePathProvider {
  private final String FILE_NAME = "data.enc";

  @Override
  public Path getFilePath() {
    try {
      Path baseDir = Os.getStoragePath();
      Files.createDirectories(baseDir);
      Path filePath = baseDir.resolve(FILE_NAME);
      if (!Files.exists(filePath)) {
        Files.createFile(filePath);
      }
      return filePath;
    } catch (IOException ex) {
      throw new RuntimeException("Failed to initialize file storage: " + ex.getMessage(), ex);
    }
  }
}
