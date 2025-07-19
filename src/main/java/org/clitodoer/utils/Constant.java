package org.clitodoer.utils;

/**
 * @author : Pramod Khalkar
 * @since : 13/07/25, Sun
 */
public class Constant {
  public static final String SECTION_DESC =
      "Section/category  ( you can use section to divide notes into categories. keep this single word for better results )";
  public static final String NOTE_ID_DESC =
      "Note ID ( you can use note ID shown in list to delete/update a specific note in a section )";
  public static final String NOTE_DESC =
      "Note text ( you can use this to add a new note, or update an existing note )";
  public static final String ADD_CMD_DESC =
      "Add a new note to a section ( If you don't specify a section, it will be added to the default section \"global\" ))";
  public static final String UPDATE_CMD_DESC =
      "Update an existing note in a section ( you can use this to update a specific note in a section/category )";
  public static final String DELETE_CMD_DESC =
      "Delete a note or section ( you can use this to delete a specific note or an entire section/category )";
  public static final String LIST_CMD_DESC =
      "List all notes or sections ( you can use this to list all notes in a section or all sections/categories )";
  public static final String TODO_CMD_DESC =
      """
      CLI Todoer - A simple command line todo manager.\n  Use 'todo --help' to see available commands and options \n
      you can use --section or -s , --note or -n options to specify section/category or note ID respectively.\n
                  """;
  public static final String TODO_EXAMPLES =
      """
          todo add --section shopping \\"Buy eggs\\",
          todo add --section work,
          todo add \\"Complete the report\\",
          todo list --section global,
          todo list,
          todo update --section work --note 1 \\"Complete the report by EOD\\",
          todo update --note 1 \\"Complete the report by EOD\\",
          todo update --note 1 --mark true,
          todo delete --section work --note 1,
          todo delete --note 1,
          todo delete --section work
          """;
  public static final String TICK_DESC = "Presence of this flag assume done else not done";
}
