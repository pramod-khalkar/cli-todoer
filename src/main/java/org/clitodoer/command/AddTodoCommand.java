package org.clitodoer.command;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class AddTodoCommand {
    private final String task;

    public AddTodoCommand(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }
}
