package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.AddTodoEvent;
import org.clitodoer.utils.Constant;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "add", description = Constant.ADD_CMD_DESC)
public class AddCommand implements Runnable {

  @CommandLine.Parameters(index = "0", description = Constant.NOTE_DESC, arity = "0..1")
  private String note;

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = Constant.SECTION_DESC)
  private String section;

  private final EventBus eventBus;

  public AddCommand(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void run() {
    eventBus.publish(new AddTodoEvent(note, section));
  }
}
