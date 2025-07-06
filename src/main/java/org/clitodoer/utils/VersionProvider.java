package org.clitodoer.utils;

import java.io.InputStream;
import java.util.Properties;
import picocli.CommandLine;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 */
public class VersionProvider implements CommandLine.IVersionProvider {
  public String[] getVersion() throws Exception {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("version.properties")) {
      Properties props = new Properties();
      props.load(is);
      return new String[] {props.getProperty("version")};
    }
  }
}
