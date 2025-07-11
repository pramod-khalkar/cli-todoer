package org.clitodoer.core;

import org.clitodoer.commands.*;
import org.clitodoer.handlers.TodoEventHandler;
import org.clitodoer.repository.FileTodoRepository;
import org.clitodoer.repository.TodoRepository;
import org.clitodoer.service.TodoServiceImpl;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
public class Initializer {
  public void run(String[] args) {
    EventBus bus = new EventBus();
    TodoRepository repo = new FileTodoRepository();
    TodoServiceImpl service = new TodoServiceImpl(repo);
    new TodoEventHandler(bus, service);

    CommandLine cmd = new CommandLine(new RootCommand());
    cmd.addSubcommand("add", new AddCommand(bus));
    cmd.addSubcommand("list", new ListCommand(bus));
    cmd.addSubcommand("update", new UpdateCommand(bus));
    cmd.addSubcommand("delete", new DeleteCommand(bus));
    cmd.execute(args);
  }
}
