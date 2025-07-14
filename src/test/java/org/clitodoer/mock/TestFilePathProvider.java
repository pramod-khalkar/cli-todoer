package org.clitodoer.mock;

import static org.clitodoer.utils.Constant.TEST_FILE_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.clitodoer.storage.FilePathProvider;

/**
 * @author : Pramod Khalkar
 * @since : 13/07/25, Sun
 */
public class TestFilePathProvider implements FilePathProvider {

  @Override
  public Path getFilePath() {
    Path tmpDir = Path.of(TEST_FILE_PATH);
    try {
      if (!Files.exists(tmpDir)) {
        Files.createFile(tmpDir);
      }
    } catch (IOException ex) {
      throw new RuntimeException("Failed to initialize test file storage: " + ex.getMessage(), ex);
    }
    return tmpDir;
  }
}
