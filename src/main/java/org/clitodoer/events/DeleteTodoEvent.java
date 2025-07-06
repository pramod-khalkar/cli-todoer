package org.clitodoer.events;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 **/
public class DeleteTodoEvent {
	public Integer noteIndex;
	public String section;

	public DeleteTodoEvent(Integer noteIndex, String section) {
		this.noteIndex = noteIndex;
		this.section = section;
	}
}
