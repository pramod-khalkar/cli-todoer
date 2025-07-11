package org.clitodoer.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.clitodoer.model.Note;
import org.clitodoer.storage.FileManager;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 */
public class FileTodoRepository implements TodoRepository {
  FileManager fileManager = new FileManager();

  @Override
  public void addSection(String section) {
    fileManager.addSection(section);
  }

  @Override
  public void addNoteToSection(String section, String text) {
    fileManager.addNoteToSection(section, text);
  }

  @Override
  public void addGlobalNote(String text) {
    fileManager.addGlobalNote(text);
  }

  @Override
  public List<Note> getNotesBySection(String section) {
    return fileManager.listNotes(section);
  }

  @Override
  public Map<String, List<Note>> getAllSections() {
    return fileManager.listAllSections().stream()
        .collect(Collectors.toMap(Function.identity(), section -> fileManager.listNotes(section)));
  }

  @Override
  public void deleteNoteFromSection(String section, int noteIndex) {
    fileManager.deleteNoteFromSection(section, noteIndex);
  }

  @Override
  public void deleteNoteFromGlobalSection(int noteIndex) {
    fileManager.deleteNoteFromGlobalSection(noteIndex);
  }

  @Override
  public void deleteSection(String section) {
    fileManager.deleteSection(section);
  }

  @Override
  public void updateNoteInSection(String section, int noteIndex, String newText) {
    fileManager.updateNoteInSection(section, noteIndex, newText);
  }

  @Override
  public void updateNoteInGlobalSection(int noteIndex, String newText) {
    fileManager.updateNoteInGlobalSection(noteIndex, newText);
  }

  @Override
  public void updateSection(String section, String newText) {
    fileManager.updateSection(section, newText);
  }
}
