package org.clitodoer.handlers;

import org.clitodoer.core.EventBus;
import org.clitodoer.events.AddTodoEvent;
import org.clitodoer.events.DeleteTodoEvent;
import org.clitodoer.events.ListTodoEvent;
import org.clitodoer.events.UpdateTodoEvent;
import org.clitodoer.service.TodoService;

/**
 * @author : Pramod Khalkar
 * @since : 05/07/25, Sat
 **/
public class TodoEventHandler {
	private final TodoService service;

	public TodoEventHandler(EventBus bus, TodoService service) {
		this.service = service;

		bus.register(AddTodoEvent.class, this::handleAdd);
		bus.register(ListTodoEvent.class, this::handleList);
		bus.register(UpdateTodoEvent.class, this::handleUpdate);
		bus.register(DeleteTodoEvent.class, this::handleDelete);
	}

	void handleAdd(AddTodoEvent event) {
		if (event.section != null) {
			service.addToSection(event.section, event.note);
		} else {
			service.addGlobal(event.note);
		}
		System.out.println("✔ Note added");
	}

	void handleList(ListTodoEvent event) {
		if (event.section != null) {
			// List notes in a specific section
			service.listSection(event.section);
		} else if (event.listNotes) {
			// List all sections notes
			service.listNotes();
		} else {
			// List all sections and their notes
			service.listAll();
		}
	}

	void handleUpdate(UpdateTodoEvent event) {
		if (event.section != null) {
			service.updateNoteInSection(event.section, event.noteIndex, event.noteText);
		} else {
			service.updateGlobalNote(event.noteIndex, event.noteText);
		}
		System.out.println("✔ Note updated");
	}

	void handleDelete(DeleteTodoEvent event) {
		if (event.section != null) {
			service.deleteNoteInSection(event.section, event.noteIndex);
		} else {
			service.deleteGlobalNote(event.noteIndex);
		}
		System.out.println("✔ Note deleted");
	}
}
