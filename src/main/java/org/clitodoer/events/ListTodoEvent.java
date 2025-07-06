package org.clitodoer.events;

/**
 * @author : Pramod Khalkar
 * @since : 06/07/25, Sun
 **/
public class ListTodoEvent {
	public final boolean listNotes;
	public final String section;

	public ListTodoEvent(boolean listNotes, String section) {
		this.listNotes = listNotes;
		this.section = section;
	}
}
