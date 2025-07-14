package org.clitodoer.storage;

import java.util.List;
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

  void updateNoteInGlobalSection(int noteIndex, String newText);

  void updateNoteInSection(String section, int noteIndex, String newText);

  void updateSection(String section, String newText);

  List<Note> listNotes(String section);

  List<String> listAllSections();
}
