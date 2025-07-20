package org.clitodoer.storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.clitodoer.handlers.NoteStatus;
import org.clitodoer.model.Note;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 */
public final class FileManager implements Operation {
  private final Storage storage;
  private final String GLOBAL_SECTION = "global";

  public FileManager(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void addGlobalNote(String note) {
    FileData fileData = storage.read();
    if (fileData.getSections() == null) {
      addSection(GLOBAL_SECTION);
    }
    FileData.Section globalSection =
        fileData.getSections().stream()
            .filter(section -> GLOBAL_SECTION.equals(section.getName()))
            .findFirst()
            .orElseGet(() -> new FileData.Section(GLOBAL_SECTION, new LinkedHashSet<>()));
    globalSection.addOrUpdateNote(new Note(globalSection.getNotes().size() + 1, note));
    fileData.addOrUpdateSection(globalSection);
    storage.write(fileData);
  }

  @Override
  public void addSection(String section) {
    FileData fileData = storage.read();
    fileData.addOrUpdateSection(new FileData.Section(section, new LinkedHashSet<>()));
    storage.write(fileData);
  }

  @Override
  public void addNoteToSection(String section, String note) {
    FileData fileData = storage.read();
    if (fileData.getSections() == null) {
      fileData.addOrUpdateSection(new FileData.Section(section, new LinkedHashSet<>()));
    }
    FileData.Section targetSection =
        fileData.getSections().stream()
            .filter(sec -> section.equals(sec.getName()))
            .findFirst()
            .orElseGet(() -> new FileData.Section(section, new LinkedHashSet<>()));
    targetSection.addOrUpdateNote(new Note(targetSection.getNotes().size() + 1, note));
    fileData.addOrUpdateSection(targetSection);
    storage.write(fileData);
  }

  @Override
  public void deleteNoteFromSection(String section, int noteIndex) {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return;
    }
    FileData.Section targetSection =
        fileData.getSections().stream()
            .filter(sec -> section.equals(sec.getName()))
            .findFirst()
            .orElse(null);
    if (targetSection != null) {
      targetSection.removeNote(noteIndex);
      fileData.addOrUpdateSection(targetSection);
      storage.write(fileData);
    }
  }

  @Override
  public void deleteNoteFromGlobalSection(int noteIndex) {
    deleteNoteFromSection(GLOBAL_SECTION, noteIndex);
  }

  @Override
  public void deleteSection(String section) {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return;
    }
    fileData.setSections(
        fileData.getSections().stream()
            .filter(sec -> !section.equals(sec.getName()))
            .collect(Collectors.toSet()));
    storage.write(fileData);
  }

  @Override
  public void updateNoteInGlobalSection(int noteIndex, String newText, NoteStatus noteStatus) {
    updateNoteInSection(GLOBAL_SECTION, noteIndex, newText, noteStatus);
  }

  @Override
  public void updateNoteInSection(
      String section, int noteIndex, String newText, NoteStatus noteStatus) {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return;
    }
    FileData.Section targetSection =
        fileData.getSections().stream()
            .filter(sec -> section.equals(sec.getName()))
            .findFirst()
            .orElse(null);
    if (targetSection != null && noteIndex > 0) {
      targetSection.updateNote(noteIndex, newText, noteStatus.toBoolean());
      storage.write(fileData);
    }
  }

  @Override
  public void updateSection(String section, String newText) {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return;
    }
    FileData.Section targetSection =
        fileData.getSections().stream()
            .filter(sec -> section.equals(sec.getName()))
            .findFirst()
            .orElse(null);
    if (targetSection != null) {
      targetSection.setName(newText);
      storage.write(fileData);
    }
  }

  @Override
  public void markNoteInSection(String section, int noteIndex, boolean mark) {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return;
    }
    FileData.Section targetSection =
        fileData.getSections().stream()
            .filter(sec -> section.equals(sec.getName()))
            .findFirst()
            .orElse(null);
    if (targetSection != null && noteIndex > 0 && noteIndex <= targetSection.getNotes().size()) {
      targetSection.markNoteDone(noteIndex, mark);
      storage.write(fileData);
    }
  }

  @Override
  public List<Note> listNotes(String section) {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return new ArrayList<>();
    }
    return fileData.getSections().stream()
        .filter(sec -> section.equals(sec.getName()))
        .flatMap(sec -> sec.getNotes().stream())
        .sorted(Comparator.comparingInt(Note::getIndex))
        .toList();
  }

  @Override
  public List<String> listAllSections() {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return new ArrayList<>();
    }
    return fileData.getSections().stream().map(FileData.Section::getName).toList();
  }
}
