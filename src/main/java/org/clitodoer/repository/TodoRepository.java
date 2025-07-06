package org.clitodoer.repository;

import org.clitodoer.model.Note;

import java.util.List;
import java.util.Map;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 **/
public interface TodoRepository {
	void addNoteToSection(String section, String text);

	void addGlobalNote(String text);

	List<Note> getNotesBySection(String section);

	Map<String, List<Note>> getAllSections();

	void deleteNoteFromSection(String section, int noteIndex);

	void deleteNoteFromGlobalSection(int noteIndex);

	void deleteSection(String section);

	void updateNoteInSection(String section, int noteIndex, String newText);

	void updateNoteInGlobalSection(int noteIndex, String newText);

	void updateSection(String section, String newText);
}
