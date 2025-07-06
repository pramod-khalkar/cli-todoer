package org.clitodoer.events;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 */
public class AddTodoEvent {
  public final String note;
  public final String section;

  public AddTodoEvent(String note, String section) {
    this.note = note;
    this.section = section;
  }
}
