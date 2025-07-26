package org.clitodoer;

import org.clitodoer.core.Initializer;
import org.clitodoer.storage.DefaultFilePathProvider;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
public class TodoLauncher {
  public static void main(String[] args) {
    CommandLine cmd = new Initializer(new DefaultFilePathProvider()).init();
    cmd.execute(args);
  }
}
