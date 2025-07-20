package org.clitodoer.storage;

import java.util.List;
import org.clitodoer.handlers.NoteStatus;
import org.clitodoer.model.Note;

/**
 * @author : Pramod Khalkar
 * @since : 12/07/25, Sat
 */
public interface Operation {
  void addGlobalNote(String note);

  void addSection(String section);

  void addNoteToSection(String section, String note);

  void deleteNoteFromSection(String section, int noteIndex);

  void deleteNoteFromGlobalSection(int noteIndex);

  void deleteSection(String section);

  void updateNoteInGlobalSection(int noteIndex, String newText, NoteStatus noteStatus);

  void updateNoteInSection(String section, int noteIndex, String newText, NoteStatus noteStatus);

  void updateSection(String section, String newText);

  void markNoteInSection(String section, int noteIndex, boolean mark);

  List<Note> listNotes(String section);

  List<String> listAllSections();
}
