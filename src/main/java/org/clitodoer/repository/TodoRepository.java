package org.clitodoer.repository;

import java.util.List;
import java.util.Map;
import org.clitodoer.handlers.NoteStatus;
import org.clitodoer.model.Note;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
public interface TodoRepository {
  void addSection(String section);

  void addNoteToSection(String section, String text);

  void addGlobalNote(String text);

  List<Note> getNotesBySection(String section);

  Map<String, List<Note>> getAllSections();

  void deleteNoteFromSection(String section, int noteIndex);

  void deleteNoteFromGlobalSection(int noteIndex);

  void deleteSection(String section);

  void updateNoteInSection(String section, int noteIndex, String newText, NoteStatus noteStatus);

  void updateNoteInGlobalSection(int noteIndex, String newText, NoteStatus noteStatus);

  void updateSection(String section, String newText);

  void markNoteInSection(String section, int noteIndex, boolean mark);
}
