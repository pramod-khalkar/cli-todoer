package org.clitodoer.commands;

import org.clitodoer.utils.VersionProvider;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(
    name = "todo",
    description = "CLI tool for managing todos",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class)
public class TodoCommand implements Runnable {
  @Override
  public void run() {
    System.out.println("Use a subcommand: add, list, delete, update");
  }
}
