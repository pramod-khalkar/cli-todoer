package org.clitodoer.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.clitodoer.model.Note;
import org.clitodoer.storage.*;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 */
public class FileTodoRepository implements TodoRepository {
  private final Operation fileOperation;

  public FileTodoRepository(Operation fileOperation) {
    this.fileOperation = fileOperation;
  }

  @Override
  public void addSection(String section) {
    fileOperation.addSection(section);
  }

  @Override
  public void addNoteToSection(String section, String text) {
    fileOperation.addNoteToSection(section, text);
  }

  @Override
  public void addGlobalNote(String text) {
    fileOperation.addGlobalNote(text);
  }

  @Override
  public List<Note> getNotesBySection(String section) {
    return fileOperation.listNotes(section);
  }

  @Override
  public Map<String, List<Note>> getAllSections() {
    return fileOperation.listAllSections().stream()
        .collect(
            Collectors.toMap(Function.identity(), section -> fileOperation.listNotes(section)));
  }

  @Override
  public void deleteNoteFromSection(String section, int noteIndex) {
    fileOperation.deleteNoteFromSection(section, noteIndex);
  }

  @Override
  public void deleteNoteFromGlobalSection(int noteIndex) {
    fileOperation.deleteNoteFromGlobalSection(noteIndex);
  }

  @Override
  public void deleteSection(String section) {
    fileOperation.deleteSection(section);
  }

  @Override
  public void updateNoteInSection(String section, int noteIndex, String newText, boolean markDone) {
    fileOperation.updateNoteInSection(section, noteIndex, newText, markDone);
  }

  @Override
  public void updateNoteInGlobalSection(int noteIndex, String newText, boolean markDone) {
    fileOperation.updateNoteInGlobalSection(noteIndex, newText, markDone);
  }

  @Override
  public void updateSection(String section, String newText) {
    fileOperation.updateSection(section, newText);
  }

  @Override
  public void markNoteInSection(String section, int noteIndex, boolean mark) {
    fileOperation.markNoteInSection(section, noteIndex, mark);
  }
}
