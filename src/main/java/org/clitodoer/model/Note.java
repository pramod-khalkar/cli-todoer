package org.clitodoer.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"createdAt", "modifiedAt", "priority"})
public class Note {
  private Integer index;
  private String text;
  private Integer priority;
  private boolean isDone = false;
  private Instant createdAt;
  private Instant modifiedAt;

  public Note(Integer index, String text) {
    this.index = index;
    this.text = text;
    this.priority = 0;
    this.isDone = false;
    this.createdAt = Instant.now();
    this.modifiedAt = Instant.now();
  }
}
