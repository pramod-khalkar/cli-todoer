package org.clitodoer.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@UtilityClass
public class Os {

  public static Path getStoragePath() {
    String DIRECTORY_NAME = "todoer";
    String os = System.getProperty("os.name").toLowerCase();
    String home = System.getProperty("user.home");
    if (os.contains("mac")) {
      return Paths.get(home, "Library", "Application Support", DIRECTORY_NAME);
    } else if (os.contains("linux")) {
      String xdg = System.getenv("XDG_DATA_HOME");
      if (xdg != null) return Paths.get(xdg, DIRECTORY_NAME);
      return Paths.get(home, ".local", "share", DIRECTORY_NAME);
    } else {
      // fallback for Windows or others
      return Paths.get(home, DIRECTORY_NAME);
    }
  }
}
