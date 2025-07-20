package org.clitodoer.todotests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.apache.commons.io.output.TeeOutputStream;
import org.clitodoer.core.Initializer;
import org.clitodoer.mock.TestFilePathProvider;
import org.clitodoer.storage.FileManager;
import org.clitodoer.storage.FileStorage;
import org.clitodoer.storage.Operation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 11/07/25, Fri
 */
public abstract class Configuration {
  static final String ADD = "add";
  static final String LIST = "list";
  static final String UPDATE = "update";
  static final String DELETE = "delete";
  static final String SECTION_OPTION = "--section";
  static final String NOTE_OPTION = "--note";
  static final String SHOPPING_SECTION = "ShoppingTodo";
  static final String GROCERIES_SECTION = "GroceriesTodo";
  static final String GLOBAL_SECTION = "global";
  static final String BUY_MILK = "Buy milk";
  static final String BUY_BREAD = "Buy bread";
  static final String BUY_OIL = "Buy oil";
  static final String NOTE_ADDED_MSG = "Note added";
  static final String SECTION_ADDED_MSG = "Note added";
  static final String NOTE_UPDATED_MSG = "Note updated";
  private PrintStream originalOut = System.out;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  PrintStream dualStream = new PrintStream(new TeeOutputStream(originalOut, outContent));
  CommandLine cli;

  String consoleOutput() {
    return outContent.toString();
  }

  @BeforeEach
  void setUp() {
    originalOut = System.out;
    System.setOut(dualStream);
    TestFilePathProvider mockedFilePath = new TestFilePathProvider();
    Operation operation = new FileManager(new FileStorage(mockedFilePath.getFilePath()));
    cli = new Initializer().withOperation(operation).build();
  }

  @AfterEach
  void restore() {
    System.setOut(originalOut);
  }

  @AfterAll
  static void cleanUp() {
    // delete the file created for testing purpose
    TestFilePathProvider mockedFilePath = new TestFilePathProvider();
    try {
      Files.deleteIfExists(mockedFilePath.getFilePath());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete test file: " + ex.getMessage(), ex);
    }
  }
}
