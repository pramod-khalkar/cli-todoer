package org.clitodoer.commands;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.ListTodoEvent;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(name = "list", description = "List notes or sections")
public class ListCommand implements Runnable {

  @CommandLine.Option(
      names = {"--note", "-n"},
      description = "List only notes")
  private boolean listNotes;

  @CommandLine.Option(
      names = {"--section", "-s"},
      description = "Section ID (optional)")
  private String section;

  private final EventBus eventBus;

  public ListCommand(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void run() {
    eventBus.publish(new ListTodoEvent(listNotes, section));
  }
}
