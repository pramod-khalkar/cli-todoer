package org.clitodoer.commands;

import org.clitodoer.utils.Constant;
import org.clitodoer.utils.VersionProvider;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
@CommandLine.Command(
    name = "todo",
    description = Constant.TODO_CMD_DESC,
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    footerHeading = "%nExamples:%n%n",
    footer = {Constant.TODO_EXAMPLES})
public class TodoCommand implements Runnable {
  @Override
  public void run() {
    System.out.println("Use a subcommand: add, list, delete, update");
  }
}
