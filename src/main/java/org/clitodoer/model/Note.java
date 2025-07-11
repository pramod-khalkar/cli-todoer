package org.clitodoer.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
  private Integer index;
  private String text;
  private Integer priority;
  private Instant createdAt;
  private Instant modifiedAt;

  public Note(Integer index, String text) {
    this.index = index;
    this.text = text;
    this.priority = 0;
    this.createdAt = Instant.now();
    this.modifiedAt = Instant.now();
  }
}
