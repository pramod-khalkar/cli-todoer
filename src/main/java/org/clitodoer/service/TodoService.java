package org.clitodoer.service;

import java.util.ArrayList;
import java.util.List;
import org.clitodoer.core.EventBus;
import org.clitodoer.event.TodoAddedEvent;
import org.clitodoer.command.AddTodoCommand;
import org.clitodoer.repository.FileTodoRepository;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/

public class TodoService {
    private final List<String> todos;
    private final FileTodoRepository repository;

    public TodoService(EventBus eventBus, FileTodoRepository repo) {
        this.repository = repo;
        this.todos = new ArrayList<>(repo.loadTodos());

        eventBus.register(AddTodoCommand.class, event -> {
            todos.add(event.getTask());
            repo.saveTodo(event.getTask());
            eventBus.publish(new TodoAddedEvent(event.getTask()));
        });
    }

    public List<String> getTodos() {
        return todos;
    }
}
