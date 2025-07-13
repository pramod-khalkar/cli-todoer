package org.clitodoer.utils;

import java.util.Arrays;
import lombok.experimental.UtilityClass;

/**
 * @author : Pramod Khalkar
 * @since : 13/07/25, Sun
 */
@UtilityClass
public class Constant {
  public static final String TEST_FILE_PATH = "data.enc";

  public long noOfLines(String consoleOutput) {
    if (consoleOutput == null || consoleOutput.isEmpty()) {
      return 0;
    }
    return Arrays.stream(consoleOutput.split("\\R")).filter(line -> !line.trim().isEmpty()).count();
  }

  public long noOfLinesIgnoreFirstLine(String consoleOutput) {
    if (consoleOutput == null || consoleOutput.isEmpty()) {
      return 0;
    }
    return Arrays.stream(consoleOutput.split("\\R")).filter(line -> !line.trim().isEmpty()).count()
        - 1;
  }
}
