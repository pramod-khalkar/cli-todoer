package org.clitodoer.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * @author : Pramod Khalkar
 * @since : 20/07/25, Sun
 */
@UtilityClass
public class Helper {
  public Map<String, List<String>> extractNotesBySection(String text) {
    Map<String, List<String>> sectionNotes = new LinkedHashMap<>();
    String[] sections = text.split("(?=‣\\s)");
    Pattern rowPattern = Pattern.compile("\\|\\s*\\d+\\s*\\|\\s*(.*?)\\s*\\|");
    for (String sectionBlock : sections) {
      if (!sectionBlock.startsWith("‣")) continue;
      String[] lines = sectionBlock.split("\\R", 2);
      String sectionName = lines[0].trim().substring(1).trim(); // Remove '‣'
      Matcher matcher = rowPattern.matcher(sectionBlock);
      List<String> notes = matcher.results().map(m -> m.group(1)).toList();
      sectionNotes.put(sectionName, notes);
    }
    return sectionNotes;
  }

  public List<String> extractNotesFromOutput(String output) {
    return Pattern.compile("\\|\\s*\\d+\\s*\\|\\s*(.*?)\\s*\\|")
        .matcher(output)
        .results()
        .map(mr -> mr.group(1))
        .toList();
  }

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
