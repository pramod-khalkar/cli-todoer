package org.clitodoer.events;

import org.clitodoer.handlers.NoteStatus;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 */
public class UpdateTodoEvent {
  public Integer noteIndex;
  public String section;
  public String noteText;
  public NoteStatus noteStatus;

  public UpdateTodoEvent(
      Integer noteIndex, String section, String noteText, NoteStatus noteStatus) {
    this.noteIndex = noteIndex;
    this.section = section;
    this.noteText = noteText;
    this.noteStatus = noteStatus;
  }
}
