package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.UpdateTodoEvent;
import org.clitodoer.utils.Constant;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "update", description = Constant.UPDATE_CMD_DESC)
public class UpdateCommand implements Runnable {

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = Constant.SECTION_DESC)
  String sectionId;

  @CommandLine.Option(
      names = {"--note", "-n"},
      description = Constant.NOTE_ID_DESC)
  Integer index;

  @CommandLine.Option(
      names = {"--tick", "-t"},
      description = Constant.TICK_DESC)
  boolean isDone;

  @CommandLine.Parameters(index = "0", description = Constant.NOTE_DESC)
  String newText;

  private EventBus eventBus;

  public UpdateCommand(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void run() {
    eventBus.publish(new UpdateTodoEvent(index, sectionId, newText, isDone));
  }
}
