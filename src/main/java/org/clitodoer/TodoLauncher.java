package org.clitodoer;

import org.clitodoer.core.Initializer;
import org.clitodoer.storage.DefaultFilePathProvider;
import org.clitodoer.storage.FileManager;
import org.clitodoer.storage.FileStorage;
import org.clitodoer.storage.Operation;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
public class TodoLauncher {
  public static void main(String[] args) {
    Operation operation =
        new FileManager(new FileStorage(new DefaultFilePathProvider().getFilePath()));
    CommandLine cmd = new Initializer().withOperation(operation).build();
    cmd.execute(args);
  }
}
