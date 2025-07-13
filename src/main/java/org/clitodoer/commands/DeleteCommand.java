package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.DeleteTodoEvent;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "delete", description = "Delete notes or sections")
public class DeleteCommand implements Runnable {

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = "Section ID")
  String sectionId;

  @CommandLine.Option(
      names = {"--note", "-n"},
      description = "Note ID")
  Integer noteIndex;

  private EventBus eventBus;

  public DeleteCommand(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void run() {
    eventBus.publish(new DeleteTodoEvent(noteIndex, sectionId));
  }
}
