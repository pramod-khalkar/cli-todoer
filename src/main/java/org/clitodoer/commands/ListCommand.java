package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.ListTodoEvent;
import org.clitodoer.utils.Constant;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "list", description = Constant.LIST_CMD_DESC)
public class ListCommand implements Runnable {

  @CommandLine.Option(
      names = {"--note", "-n"},
      description = Constant.NOTE_DESC)
  private boolean note;

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = Constant.SECTION_DESC,
      arity = "0..1")
  private String section;

  private final EventBus eventBus;

  public ListCommand(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void run() {
    eventBus.publish(new ListTodoEvent(note, section));
  }
}
