package org.clitodoer.core;

import java.util.*;

/**
 * @author : Pramod Khalkar
 * @since : 02/07/25, Wed
 **/
public class EventBus {
    private Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();

    public <T> void register(Class<T> eventType, Consumer<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    public <T> void publish(T event) {
        List<Consumer<?>> consumers = handlers.getOrDefault(event.getClass(), Collections.emptyList());
        for (Consumer<?> consumer : consumers) {
            ((Consumer<T>) consumer).accept(event);
        }
    }

    public interface Consumer<T> {
        void accept(T event);
    }
}
