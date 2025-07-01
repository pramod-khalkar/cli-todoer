package org.clitodoer.event;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class TodoAddedEvent {
    private final String task;

    public TodoAddedEvent(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }
}
