package org.clitodoer.repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.clitodoer.handlers.NoteStatus;
import org.clitodoer.model.Note;
import org.clitodoer.storage.FileData;
import org.clitodoer.storage.Storage;

/**
 * @author : Pramod Khalkar
 * @since : 26/07/25, Sat
 */
public class FileTodoRepository implements TodoRepository {
  private final Storage storage;
  private final String GLOBAL_SECTION = "global";

  public FileTodoRepository(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void addSection(String section) {
    FileData fileData = storage.read();
    fileData.addOrUpdateSection(new FileData.Section(section, new LinkedHashSet<>()));
    storage.write(fileData);
  }

  @Override
  public void addNoteToSection(String section, String text) {
    FileData fileData = storage.read();
    if (fileData.getSections() == null) {
      fileData.addOrUpdateSection(new FileData.Section(section, new LinkedHashSet<>()));
    }
    FileData.Section targetSection =
        fileData.getSections().stream()
            .filter(sec -> section.equals(sec.getName()))
            .findFirst()
            .orElseGet(() -> new FileData.Section(section, new LinkedHashSet<>()));
    targetSection.addOrUpdateNote(new Note(targetSection.getNotes().size() + 1, text));
    fileData.addOrUpdateSection(targetSection);
    storage.write(fileData);
  }

  @Override
  public void addGlobalNote(String text) {
    FileData fileData = storage.read();
    if (fileData.getSections() == null) {
      addSection(GLOBAL_SECTION);
    }
    FileData.Section globalSection =
        fileData.getSections().stream()
            .filter(section -> GLOBAL_SECTION.equals(section.getName()))
            .findFirst()
            .orElseGet(() -> new FileData.Section(GLOBAL_SECTION, new LinkedHashSet<>()));
    globalSection.addOrUpdateNote(new Note(globalSection.getNotes().size() + 1, text));
    fileData.addOrUpdateSection(globalSection);
    storage.write(fileData);
  }

  @Override
  public List<Note> getNotesBySection(String section) {
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
  public Map<String, List<Note>> getAllSections() {
    return listAllSections().stream()
        .collect(Collectors.toMap(Function.identity(), this::listNotes));
  }

  private List<Note> listNotes(String section) {
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

  private List<String> listAllSections() {
    FileData fileData = storage.read();
    if (fileData == null || fileData.getSections() == null) {
      return new ArrayList<>();
    }
    return fileData.getSections().stream().map(FileData.Section::getName).toList();
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
  public void updateNoteInGlobalSection(int noteIndex, String newText, NoteStatus noteStatus) {
    updateNoteInSection(GLOBAL_SECTION, noteIndex, newText, noteStatus);
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
}
