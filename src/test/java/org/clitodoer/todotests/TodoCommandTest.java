package org.clitodoer.todotests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.clitodoer.utils.Constant;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author : Pramod Khalkar
 * @since : 11/07/25, Fri @Todo This class tests todo CLI commands but it is following order of
 *     execution. Please consider cucumber for future testing wth independent scenarios.
 *     <p>PENDING : todo update --section <section_name> <new_value_of_section>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoCommandTest extends Configuration {

  static Stream<Arguments> addNoteArgsProvider() {
    return Stream.of(
        Arguments.of(ADD, SECTION_OPTION, SHOPPING_SECTION, BUY_MILK),
        Arguments.of(ADD, SECTION_OPTION, SHOPPING_SECTION, BUY_BREAD),
        Arguments.of(ADD, SECTION_OPTION, SHOPPING_SECTION, BUY_EGGS));
  }

  @Order(1)
  @ParameterizedTest(name = "todo add --section  \"{2}\" \"{3}\"")
  @MethodSource("addNoteArgsProvider")
  void addNoteInSectionTest(String cmd, String option, String section, String note) {
    // when
    int exitCode = cli.execute(cmd, option, section, note);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_ADDED_MSG);
  }

  static Stream<Arguments> addNoteGlobalArgsProvider() {
    return Stream.of(
        Arguments.of(ADD, BUY_MILK), Arguments.of(ADD, BUY_BREAD), Arguments.of(ADD, BUY_EGGS));
  }

  @Order(2)
  @ParameterizedTest(name = "todo add \"{1}\"")
  @MethodSource("addNoteGlobalArgsProvider")
  void addNoteInGlobalSection(String cmd, String note) {
    // when
    int exitCode = cli.execute(cmd, note);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_ADDED_MSG);
  }

  @Order(3)
  @DisplayName("todo add --section GroceriesTodo")
  @Test
  void addOnlySectionTest() {
    // when
    int exitCode = cli.execute(ADD, SECTION_OPTION, GROCERIES_SECTION);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(SECTION_ADDED_MSG);
  }

  @Order(4)
  @DisplayName("todo list --section ShoppingTodo")
  @Test
  void listNotesInSectionTest() {
    // when
    int exitCode = cli.execute(LIST, SECTION_OPTION, SHOPPING_SECTION);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(Constant.noOfLines(consoleOutput())).isEqualTo(3);
  }

  @Order(5)
  @DisplayName("todo list")
  @Test
  void listNotesFromAllTheSectionsTest() {
    // when
    int exitCode = cli.execute(LIST);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(Constant.noOfLines(consoleOutput())).isEqualTo(9);
  }

  @Order(6)
  @DisplayName("todo update --section ShoppingTodo 1 Buy milk and bread")
  @Test
  void updateNoteInSectionTest() {
    // when
    int exitCode =
        cli.execute(
            UPDATE, SECTION_OPTION, SHOPPING_SECTION, NOTE_OPTION, "1", BUY_MILK + " and bread");
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_UPDATED_MSG);

    // Validate update by hitting list command
    exitCode = cli.execute(LIST);
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput())
        .contains(BUY_MILK + " and bread"); // @Todo add index validation as well
  }

  @Order(7)
  @DisplayName("todo update --note 1 Buy milk and bread")
  @Test
  void updateNoteInGlobalSectionTest() {
    // when
    int exitCode = cli.execute(UPDATE, NOTE_OPTION, "1", BUY_MILK + " and bread");
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_UPDATED_MSG);

    // Validate update by hitting list command
    exitCode = cli.execute(LIST, SECTION_OPTION, GLOBAL_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Constant.noOfLinesIgnoreFirstLine(consoleOutput())).isEqualTo(3);
    assertThat(consoleOutput())
        .contains(BUY_MILK + " and bread"); // @Todo add index validation as well
  }

  @Order(8)
  @DisplayName("todo delete --note <index_no>")
  @Test
  void deleteNoteFromGlobalSectionTest() {
    // when
    int exitCode = cli.execute(DELETE, NOTE_OPTION, "1");
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains("Note deleted");

    // Validate deletion by hitting list command
    exitCode = cli.execute(LIST, SECTION_OPTION, GLOBAL_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Constant.noOfLinesIgnoreFirstLine(consoleOutput())).isEqualTo(2);
  }

  @Order(9)
  @DisplayName("todo delete --section ShoppingTodo --note <index_no>")
  @Test
  void deleteNoteFromSectionTest() {
    // when
    int exitCode = cli.execute(DELETE, SECTION_OPTION, SHOPPING_SECTION, NOTE_OPTION, "1");
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains("Note deleted");

    // Validate deletion by hitting list command
    exitCode = cli.execute(LIST, SECTION_OPTION, SHOPPING_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Constant.noOfLinesIgnoreFirstLine(consoleOutput())).isEqualTo(2);
  }

  @Order(10)
  @DisplayName("todo delete --section ShoppingTodo")
  @Test
  void deleteSectionTest() {
    // when
    int exitCode = cli.execute(DELETE, SECTION_OPTION, SHOPPING_SECTION);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains("Note deleted");

    // Validate deletion by hitting list command
    exitCode = cli.execute(LIST, SECTION_OPTION, SHOPPING_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Constant.noOfLinesIgnoreFirstLine(consoleOutput())).isEqualTo(0);
  }
}
