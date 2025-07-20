package org.clitodoer.events;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 */
public class ListTodoEvent {
  public final boolean listNotes;
  public final String section;
  public final boolean sectionOnly;

  public ListTodoEvent(boolean listNotes, String section, boolean sectionOnly) {
    this.listNotes = listNotes;
    this.sectionOnly = sectionOnly;
    this.section = section;
  }
}
