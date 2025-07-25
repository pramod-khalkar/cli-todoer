package org.clitodoer.service;

import org.clitodoer.handlers.NoteStatus;

/**
 * @author : Pramod Khalkar
 * @since : 11/07/25, Fri
 */
public interface TodoService {
  void addSection(String section);

  void addToSection(String section, String note);

  void addGlobal(String note);

  void listSectionNotes(String section);

  void listAllSectionNotes();

  void listAllSectionNotesWithoutSectionName();

  void listSectionsWithoutNote();

  void updateNoteInSection(
      String section, Integer noteIndex, String noteText, NoteStatus noteStatus);

  void updateGlobalNote(Integer noteIndex, String noteText, NoteStatus noteStatus);

  void deleteNoteInSection(String section, Integer noteIndex);

  void deleteGlobalNote(Integer noteIndex);

  void deleteSection(String section);
}
