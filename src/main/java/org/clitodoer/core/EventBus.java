package org.clitodoer.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class EventBus {
    private final Map<Class<?>, List<EventListener>> listeners = new HashMap<>();

    public <T> void register(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T> void publish(T event) {
        List<EventListener> registered = listeners.getOrDefault(event.getClass(), List.of());
        for (EventListener listener : registered) {
            listener.onEvent(event);
        }
    }

    public interface EventListener<T> {
        void onEvent(T event);
    }
}
