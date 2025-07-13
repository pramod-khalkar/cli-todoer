package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.DeleteTodoEvent;
import org.clitodoer.utils.Constant;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "delete", description = Constant.DELETE_CMD_DESC)
public class DeleteCommand implements Runnable {

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = Constant.SECTION_DESC)
  String sectionId;

  @CommandLine.Option(
      names = {"--note", "-n"},
      description = Constant.NOTE_ID_DESC)
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
