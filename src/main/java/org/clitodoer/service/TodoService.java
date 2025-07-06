package org.clitodoer.service;

import org.clitodoer.model.Note;
import org.clitodoer.repository.TodoRepository;

import java.util.List;
import java.util.Map;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class TodoService {
	private final TodoRepository repository;

	public TodoService(TodoRepository repository) {
		this.repository = repository;
	}

	// Add a todo to a section
	public void addToSection(String section, String note) {
		repository.addNoteToSection(section, note);
	}

	// Add a global (unsectioned) todo
	public void addGlobal(String note) {
		repository.addGlobalNote(note);
	}

	// List all sections and their notes
	public void listAll() {
		repository.getAllSections().forEach((section, notes) -> {
			System.out.println("Section: " + section);
			notes.forEach(n -> {
				System.out.printf("Index : %d , Note : %s%n%n", n.getIndex(), n.getText());
			});
		});
	}

	// List notes in a specific section
	public void listSection(String section) {
		List<Note> notes = repository.getNotesBySection(section);
		if (notes == null || notes.isEmpty()) {
			System.out.println("No notes in section " + section);
		} else {
			for (int i = 0; i < notes.size(); i++) {
				System.out.println("[" + i + "] " + notes.get(i).getText());
			}
		}
	}

	public void listNotes() {
		Map<String, List<Note>> allSections = repository.getAllSections();
		if (allSections.isEmpty()) {
			System.out.println("Notes not found.");
		} else {
			allSections.forEach((k, v) -> {
				v.forEach(n -> {
					System.out.printf("Section: %s, Index: %d, Note: %s%n", k, n.getIndex(), n.getText());
				});
			});
		}
	}

	// Delete note from section
	public void deleteNoteInSection(String section, int noteIndex) {
		repository.deleteNoteFromSection(section, noteIndex);
	}

	public void deleteGlobalNote(int noteIndex) {
		repository.deleteNoteFromGlobalSection(noteIndex);
	}

	// Delete entire section
	public void deleteSection(String section) {
		repository.deleteSection(section);
	}

	// Update note in section
	public void updateNoteInSection(String section, int noteIndex, String updatedText) {
		repository.updateNoteInSection(section, noteIndex, updatedText);
	}

	public void updateGlobalNote(int noteIndex, String updatedText) {
		repository.updateNoteInGlobalSection(noteIndex, updatedText);
	}

	// Update section title or description (if needed)
	public void updateSection(String section, String newText) {
		repository.updateSection(section, newText);
	}
}
