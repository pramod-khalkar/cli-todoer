package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.AddTodoEvent;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "add", description = "Add a new note")
public class AddCommand implements Runnable {

  @CommandLine.Parameters(index = "0", description = "The note text")
  private String note;

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = "Section  (optional)")
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
