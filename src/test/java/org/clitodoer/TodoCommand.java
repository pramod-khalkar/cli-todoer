package org.clitodoer;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;

import org.clitodoer.storage.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 11/07/25, Fri
 */
public abstract class TodoCommand {
  public CommandLine cli = new CommandLine(new org.clitodoer.commands.TodoCommand());
  FileManager fileManager;

  static final String ADD = "add";
  static final String LIST = "list";
  static final String UPDATE = "update";
  static final String DELETE = "delete";

  static final String SHOPPING_SECTION = "ShoppingTodo";
  static final String BUY_MILK = "Buy milk";
  static final String BUY_BREAD = "Buy bread";
  static final String BUY_EGGS = "Buy oil";

  String consoleOutput() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    cli.setOut(new PrintWriter(out, true));
    return out.toString();
  }

  @BeforeEach
  void setUp() {
    fileManager = Mockito.spy(new FileManager());
    Mockito.doReturn(Path.of("")).when(fileManager).
  }
}
