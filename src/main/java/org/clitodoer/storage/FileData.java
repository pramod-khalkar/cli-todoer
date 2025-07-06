package org.clitodoer.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.clitodoer.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileData {
	private List<Section> sections;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Section {
		private String name;
		private List<Note> notes;

		public void addNote(Note note) {
			if (notes == null) {
				notes = new ArrayList<>();
				notes.add(note);
			} else {
				notes.add(note);
			}
		}

		public void removeNote(int index) {
			if (notes != null && index >= 0 && index < notes.size()) {
				notes.remove(index);
			}
		}

		public void updateNote(int index, String newText) {
			if (notes != null && index >= 0 && index < notes.size()) {
				Note note = notes.get(index);
				note.setText(newText);
				note.setModifiedAt(java.time.Instant.now());
			}
		}
	}
}
