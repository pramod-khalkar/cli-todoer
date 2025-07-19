package org.clitodoer.handlers;

/**
 * @author : Pramod Khalkar
 * @since : 19/07/25, Sat
 */
public enum NoteStatus {
  TICK(true),
  CROSS(false);

  private final boolean value;

  NoteStatus(boolean value) {
    this.value = value;
  }

  public boolean toBoolean() {
    return value;
  }
}
