package org.clitodoer.core;

import org.clitodoer.commands.*;
import org.clitodoer.handlers.TodoEventHandler;
import org.clitodoer.repository.FileTodoRepository;
import org.clitodoer.repository.TodoRepository;
import org.clitodoer.service.TodoService;
import org.clitodoer.service.TodoServiceImpl;
import org.clitodoer.storage.*;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 12/07/25, Sat
 */
public class Initializer {
  private Operation operation;

  public Initializer withOperation(Operation operation) {
    this.operation = operation;
    return this;
  }

  public CommandLine build() {
    if (operation == null) {
      throw new IllegalStateException("Operation must be set before building.");
    }
    EventBus bus = new EventBus();
    TodoRepository repo = new FileTodoRepository(operation);
    TodoService service = new TodoServiceImpl(repo);
    new TodoEventHandler(bus, service);
    CommandLine cmd = new CommandLine(new TodoCommand());
    cmd.addSubcommand("add", new AddCommand(bus));
    cmd.addSubcommand("list", new ListCommand(bus));
    cmd.addSubcommand("update", new UpdateCommand(bus));
    cmd.addSubcommand("delete", new DeleteCommand(bus));
    // cmd.execute(args);
    return cmd;
  }
}
