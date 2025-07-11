package org.clitodoer.service;

import java.util.List;
import java.util.Map;
import org.clitodoer.model.Note;
import org.clitodoer.repository.TodoRepository;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 */
public class TodoServiceImpl implements TodoService {
  private static final String TRIANGULAR_BULLET = "\u2023";

  private final TodoRepository repository;

  public TodoServiceImpl(TodoRepository repository) {
    this.repository = repository;
  }

  @Override
  public void addSection(String section) {
    repository.addSection(section);
  }

  @Override
  public void addToSection(String section, String note) {
    repository.addNoteToSection(section, note);
  }

  @Override
  public void addGlobal(String note) {
    repository.addGlobalNote(note);
  }

  @Override
  public void listSectionNotes(String section) {
    List<Note> notes = repository.getNotesBySection(section);
    printNoteList(notes);
  }

  @Override
  public void listAllSectionNotes() {
    repository.getAllSections().forEach(this::printSection);
  }

  @Override
  public void listSectionsWithoutNote() {
    repository
        .getAllSections()
        .forEach(
            (k, v) -> {
              System.out.printf("%s %s\n", TRIANGULAR_BULLET, k);
            });
  }

  @Override
  public void listAllSectionNotesWithoutSectionName() {
    Map<String, List<Note>> allSections = repository.getAllSections();
    allSections.forEach((k, v) -> v.forEach(this::printNote));
  }

  @Override
  public void updateNoteInSection(String section, Integer noteIndex, String updatedText) {
    repository.updateNoteInSection(section, noteIndex, updatedText);
  }

  @Override
  public void updateGlobalNote(Integer noteIndex, String updatedText) {
    repository.updateNoteInGlobalSection(noteIndex, updatedText);
  }

  @Override
  public void deleteNoteInSection(String section, Integer noteIndex) {
    repository.deleteNoteFromSection(section, noteIndex);
  }

  @Override
  public void deleteGlobalNote(Integer noteIndex) {
    repository.deleteNoteFromGlobalSection(noteIndex);
  }

  private void printSection(String section, List<Note> notes) {
    if (section != null) {
      System.out.printf("%s %s\n", TRIANGULAR_BULLET, section);
      printNoteList(notes);
    }
  }

  private void printNote(Note note) {
    if (note != null) System.out.printf("[%d]  %s\n", note.getIndex(), note.getText());
  }

  private void printNoteList(List<Note> noteList) {
    noteList.forEach(this::printNote);
  }
}
