package org.clitodoer;

import org.clitodoer.command.AddTodoCommand;
import org.clitodoer.core.EventBus;
import org.clitodoer.event.TodoAddedEvent;
import org.clitodoer.repository.FileTodoRepository;
import org.clitodoer.service.TodoService;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class Application {
    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("add")) {
            System.out.println("Usage: todo add \"Task description\"");
            return;
        }

        String task = args[1];

        EventBus bus = new EventBus();
        FileTodoRepository repo = new FileTodoRepository();
        TodoService service = new TodoService(bus, repo);

        bus.register(TodoAddedEvent.class, event -> {
            System.out.println("Added: " + event.getTask());
        });

        bus.publish(new AddTodoCommand(task));
    }
}