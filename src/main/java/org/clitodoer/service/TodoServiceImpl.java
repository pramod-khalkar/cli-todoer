package org.clitodoer.service;

import java.util.List;
import java.util.stream.Collectors;
import org.clitodoer.handlers.NoteStatus;
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
    printTable(notes);
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
    List<Note> allNotes =
        repository.getAllSections().values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    printTable(allNotes);
    System.out.println("📝 Note: Indexing is based on the section, not globally.");
  }

  @Override
  public void updateNoteInSection(
      String section, Integer noteIndex, String updatedText, NoteStatus noteStatus) {
    repository.updateNoteInSection(section, noteIndex, updatedText, noteStatus);
  }

  @Override
  public void updateGlobalNote(Integer noteIndex, String updatedText, NoteStatus noteStatus) {
    repository.updateNoteInGlobalSection(noteIndex, updatedText, noteStatus);
  }

  @Override
  public void deleteNoteInSection(String section, Integer noteIndex) {
    repository.deleteNoteFromSection(section, noteIndex);
  }

  @Override
  public void deleteGlobalNote(Integer noteIndex) {
    repository.deleteNoteFromGlobalSection(noteIndex);
  }

  @Override
  public void deleteSection(String section) {
    repository.deleteSection(section);
  }

  private void printSection(String section, List<Note> notes) {
    if (section != null && !notes.isEmpty()) {
      System.out.printf("%s %s\n", TRIANGULAR_BULLET, section);
      printTable(notes);
    }
  }

  private void printNote(Note note) {
    if (note != null)
      System.out.printf(
          "[%d]  %s   %s\n", note.getIndex(), note.getText(), note.isDone() ? "✓" : "✗");
  }

  void printTable(List<Note> notes) {
    // Determine max length of note text
    int maxNoteLength = "Note".length();
    for (Note note : notes) {
      maxNoteLength = Math.max(maxNoteLength, note.getText().length());
    }

    // Calculate formats based on dynamic width
    String format = "| %-6s | %-" + maxNoteLength + "s | %-4s |\n";
    String line =
        "+" + "-".repeat(8) + "+" + "-".repeat(maxNoteLength + 2) + "+" + "-".repeat(6) + "+";

    // Print table
    System.out.println(line);
    System.out.printf("| %-6s | %-" + maxNoteLength + "s | %-4s |\n", "Index", "Note", "Done");
    System.out.println(line);
    for (Note note : notes) {
      System.out.printf(format, note.getIndex(), note.getText(), note.isDone() ? "✓" : "✗");
    }
    System.out.println(line);
  }
}
