package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.UpdateTodoEvent;
import org.clitodoer.handlers.NoteStatus;
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

  @CommandLine.Option(
      names = {"--cross", "-c"},
      description = Constant.CROSS_DESC)
  boolean isCross;

  @CommandLine.Parameters(index = "0", description = Constant.NOTE_DESC, arity = "0..1")
  String newText;

  private EventBus eventBus;

  public UpdateCommand(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void run() {
    if (isDone && isCross) {
      System.err.println("Error: You cannot use both --tick and --cross options at the same time.");
      return;
    }
    NoteStatus noteStatus = isDone ? NoteStatus.TICK : NoteStatus.CROSS;
    eventBus.publish(new UpdateTodoEvent(index, sectionId, newText, noteStatus));
  }
}
