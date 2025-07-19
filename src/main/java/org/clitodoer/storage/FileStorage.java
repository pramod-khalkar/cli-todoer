package org.clitodoer.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.clitodoer.model.Note;

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
    fileData.setSections(
        fileData.getSections().stream().map(this::indexing).collect(Collectors.toSet()));
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

  private FileData.Section indexing(FileData.Section section) {
    if (section.getNotes() != null && !section.getNotes().isEmpty()) {
      // Separate notes
      List<Note> notDone = new ArrayList<>();
      List<Note> done = new ArrayList<>();

      for (Note note : section.getNotes()) {
        if (note.isDone()) {
          done.add(note);
        } else {
          notDone.add(note);
        }
      }

      // Combine: notDone first, then done
      List<Note> reordered = new ArrayList<>();
      reordered.addAll(notDone);
      reordered.addAll(done);

      // Re-index from 1
      for (int i = 0; i < reordered.size(); i++) {
        reordered.get(i).setIndex(i + 1);
      }

      // Update notes in section (preserve type if using Set)
      section.setNotes(new LinkedHashSet<>(reordered));
    }
    return section;
  }
}
