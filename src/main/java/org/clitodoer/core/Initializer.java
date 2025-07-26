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

  private final FilePathProvider filePathProvider;

  public Initializer(FilePathProvider filePathProvider) {
    this.filePathProvider = filePathProvider;
  }

  public CommandLine init() {
    EventBus bus = new EventBus();
    TodoRepository repo = new FileTodoRepository(new FileStorage(filePathProvider.getFilePath()));
    TodoService service = new TodoServiceImpl(repo);
    new TodoEventHandler(bus, service);
    CommandLine cmd = new CommandLine(new TodoCommand());
    cmd.addSubcommand("add", new AddCommand(bus));
    cmd.addSubcommand("list", new ListCommand(bus));
    cmd.addSubcommand("update", new UpdateCommand(bus));
    cmd.addSubcommand("delete", new DeleteCommand(bus));
    return cmd;
  }
}
