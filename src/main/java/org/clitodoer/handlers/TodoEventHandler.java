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
 */
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
    if (event.section != null && event.note != null) {
      service.addToSection(event.section, event.note);
    } else if (event.section != null) {
      service.addSection(event.section);
    } else {
      service.addGlobal(event.note);
    }
    System.out.println("✔ Note added");
  }

  void handleList(ListTodoEvent event) {
    if (event.sectionOnly) {
      service.listSectionsWithoutNote();
    } else {
      if (event.section != null && !event.section.isEmpty()) {
        service.listSectionNotes(event.section);
      } else if (event.listNotes) {
        service.listAllSectionNotesWithoutSectionName();
      } else {
        service.listAllSectionNotes();
      }
    }
  }

  void handleUpdate(UpdateTodoEvent event) {
    if (event.section != null) {
      service.updateNoteInSection(event.section, event.noteIndex, event.noteText, event.noteStatus);
    } else {
      service.updateGlobalNote(event.noteIndex, event.noteText, event.noteStatus);
    }
    System.out.println("✔ Note updated");
  }

  void handleDelete(DeleteTodoEvent event) {
    if (event.section != null && event.noteIndex != null) {
      service.deleteNoteInSection(event.section, event.noteIndex);
    } else if (event.section != null) {
      service.deleteSection(event.section);
    } else if (event.noteIndex != null) {
      service.deleteGlobalNote(event.noteIndex);
    } else {
      System.out.println("❌ Error: Note index or section must be provided for deletion.");
      return;
    }
    System.out.println("✔ Note deleted");
  }
}
