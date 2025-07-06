package org.clitodoer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 **/
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
		this.priority = 0; // Default priority
		this.createdAt = Instant.now();
		this.modifiedAt = Instant.now();
	}

	public Note(Integer index, String text, Integer priority) {
		this.index = index;
		this.text = text;
		this.priority = priority;
		this.createdAt = Instant.now();
		this.modifiedAt = Instant.now();
	}
}
