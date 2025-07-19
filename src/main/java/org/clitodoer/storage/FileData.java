package org.clitodoer.storage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.clitodoer.model.Note;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileData {
  private Set<Section> sections;

  public void addOrUpdateSection(Section section) {
    if (sections == null) {
      sections = new LinkedHashSet<>();
      sections.add(section);
    } else {
      sections.add(section);
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Section {
    private String name;
    private Set<Note> notes;

    public void addOrUpdateNote(Note note) {
      if (notes == null) {
        notes = new LinkedHashSet<>();
        notes.add(note);
      } else {
        notes.add(note);
      }
    }

    public void removeNote(int index) {
      for (Note note : new LinkedHashSet<>(notes)) {
        if (note.getIndex() == index) {
          notes.remove(note);
          break;
        }
      }
    }

    public void updateNote(int index, String newText, boolean markDone) {
      for (Note note : new LinkedHashSet<>(notes)) {
        if (note.getIndex() == index) {
          notes.remove(note);
          note.setText(newText);
          note.setDone(markDone);
          note.setModifiedAt(Instant.now());
          notes.add(note);
          break;
        }
      }
    }

    public void updateNote(int index, String newText) {
      updateNote(index, newText, false);
    }

    public void markNoteDone(int index, boolean mark) {
      for (Note note : new LinkedHashSet<>(notes)) {
        if (note.getIndex() == index) {
          notes.remove(note);
          note.setDone(mark);
          note.setModifiedAt(Instant.now());
          notes.add(note);
          break;
        }
      }
    }
  }
}
