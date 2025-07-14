package org.clitodoer.storage;

/**
 * @author : Pramod Khalkar
 * @since : 12/07/25, Sat
 */
public interface Storage {
  FileData read();

  void write(FileData fileData);
}
