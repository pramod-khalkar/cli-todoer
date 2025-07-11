package org.clitodoer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author : Pramod Khalkar
 * @since : 11/07/25, Fri
 */
public class TodoAddCommandTest extends TodoCommand {

  @Test
  void addNoteInSectionTest() {
    // when
    int exitCode = cli.execute(ADD, BUY_MILK, "--section", SHOPPING_SECTION);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains("Note added");
  }
}
