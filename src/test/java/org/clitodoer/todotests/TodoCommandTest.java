package org.clitodoer.todotests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.clitodoer.utils.Helper;
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

  private final List<String> SHOPPING_TODO = List.of(BUY_MILK, BUY_OIL, BUY_BREAD);
  private final List<String> GLOBAL_TODO = List.of(BUY_MILK, BUY_BREAD, BUY_OIL);

  static Stream<Arguments> addNoteArgsProvider() {
    return Stream.of(
        Arguments.of(ADD, SECTION_OPTION, SHOPPING_SECTION, BUY_MILK),
        Arguments.of(ADD, SECTION_OPTION, SHOPPING_SECTION, BUY_BREAD),
        Arguments.of(ADD, SECTION_OPTION, SHOPPING_SECTION, BUY_OIL));
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
        Arguments.of(ADD, BUY_MILK), Arguments.of(ADD, BUY_BREAD), Arguments.of(ADD, BUY_OIL));
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
    List<String> notes =
        Pattern.compile("\\|\\s*\\d+\\s*\\|\\s*(.*?)\\s*\\|")
            .matcher(consoleOutput())
            .results()
            .map(mr -> mr.group(1))
            .toList();
    assertThat(notes).containsExactlyElementsOf(SHOPPING_TODO);
  }

  @Order(5)
  @DisplayName("todo list")
  @Test
  void listNotesFromAllTheSectionsTest() {
    // when
    int exitCode = cli.execute(LIST);
    // then
    assertThat(exitCode).isEqualTo(0);
    Map<String, List<String>> sectionToNotes = Helper.extractNotesBySection(consoleOutput());
    assertThat(sectionToNotes.get(SHOPPING_SECTION)).containsExactlyElementsOf(SHOPPING_TODO);

    assertThat(sectionToNotes.get(GLOBAL_SECTION)).containsExactlyElementsOf(GLOBAL_TODO);
  }

  @Order(6)
  @DisplayName("todo update --section ShoppingTodo 1 Buy milk and bread")
  @Test
  void updateNoteInSectionTest() {
    // when
    int exitCode =
        cli.execute(
            UPDATE, SECTION_OPTION, SHOPPING_SECTION, NOTE_OPTION, "1", BUY_MILK + BUY_BREAD);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_UPDATED_MSG);

    exitCode = cli.execute(LIST, SECTION_OPTION, SHOPPING_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Helper.extractNotesFromOutput(consoleOutput()))
        .containsExactlyInAnyOrderElementsOf(
            SHOPPING_TODO.stream()
                .map(n -> n.equals(BUY_MILK) ? BUY_MILK + BUY_BREAD : n)
                .toList());
  }

  @Order(7)
  @DisplayName("todo update --note 1 Buy milk and bread")
  @Test
  void updateNoteInGlobalSectionTest() {
    // when
    int exitCode = cli.execute(UPDATE, NOTE_OPTION, "1", BUY_MILK + BUY_BREAD);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_UPDATED_MSG);

    exitCode = cli.execute(LIST, SECTION_OPTION, GLOBAL_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Helper.extractNotesFromOutput(consoleOutput()))
        .containsExactlyInAnyOrderElementsOf(
            GLOBAL_TODO.stream().map(n -> n.equals(BUY_MILK) ? BUY_MILK + BUY_BREAD : n).toList());
  }

  @Order(8)
  @DisplayName("todo update --section ShoppingTodo --note 1 --tick")
  @Test
  void updateNoteInSectionWithTickTest() {
    // when
    int exitCode =
        cli.execute(UPDATE, SECTION_OPTION, SHOPPING_SECTION, NOTE_OPTION, "1", "--tick");
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains(NOTE_UPDATED_MSG);

    exitCode = cli.execute(LIST, SECTION_OPTION, SHOPPING_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains("Buy oil           | ✓" );
  }

  @Order(9)
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
    assertThat(Helper.extractNotesFromOutput(consoleOutput()))
        .containsExactlyInAnyOrderElementsOf(List.of(BUY_BREAD, BUY_MILK + BUY_BREAD));
  }

  @Order(10)
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
    assertThat(Helper.extractNotesFromOutput(consoleOutput()))
        .containsExactlyInAnyOrderElementsOf(List.of(BUY_OIL, BUY_BREAD));
  }

  @Order(11)
  @DisplayName("todo delete --section ShoppingTodo")
  @Test
  void deleteSectionTest() {
    // when
    int exitCode = cli.execute(DELETE, SECTION_OPTION, SHOPPING_SECTION);
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(consoleOutput()).contains("Note deleted");

    exitCode = cli.execute(LIST, SECTION_OPTION, SHOPPING_SECTION);
    assertThat(exitCode).isEqualTo(0);
    assertThat(Helper.extractNotesFromOutput(consoleOutput())).isEmpty();
  }

  @Order(12)
  @DisplayName("todo list --section-only")
    @Test
  void listSectionsOnlyTest() {
    // when
    int exitCode = cli.execute(LIST, "--section-only");
    // then
    assertThat(exitCode).isEqualTo(0);
    assertThat(Helper.noOfLines(consoleOutput())).isEqualTo(2);
  }
}
