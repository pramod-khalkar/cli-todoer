package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.UpdateTodoEvent;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 **/
@CommandLine.Command(name = "update", description = "Update notes or sections")
public class UpdateCommand implements Runnable {

	@CommandLine.Option(names = "--sec", description = "Section ID")
	String sectionId;

	@CommandLine.Option(names = "--note", description = "Note ID (optional)")
	Integer noteId;

	@CommandLine.Parameters(index = "0", description = "Updated text")
	String newText;

	private EventBus eventBus;

	public UpdateCommand(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void run() {
		eventBus.publish(new UpdateTodoEvent(noteId, sectionId, newText));
	}
}
