package org.clitodoer.events;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 **/
public class UpdateTodoEvent {
	public Integer noteIndex;
	public String section;
	public String noteText;

	public UpdateTodoEvent(Integer noteIndex, String section, String noteText) {
		this.noteIndex = noteIndex;
		this.section = section;
		this.noteText = noteText;
	}
}
