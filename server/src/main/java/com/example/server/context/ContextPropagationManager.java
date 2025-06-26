package com.example.server.context;

import com.example.server.accessors.ThreadLocalAccessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContextPropagationManager {

    private final List<ThreadLocalAccessor<?>> accessors;

    public ContextPropagationManager(List<ThreadLocalAccessor<?>> accessors) {
        this.accessors = accessors;
    }

    public Map<String, Object> captureContext() {
        Map<String, Object> context = new HashMap<>();
        for (ThreadLocalAccessor<?> accessor : accessors) {
            try {
                Object value = accessor.getValue();
                // ADD DEBUG POINT HERE - Check what getValue() returns for each accessor
                System.out.println("Capturing " + accessor.getKey() + ": " + value);
                if (value != null) {
                    context.put(accessor.getKey(), value);
                }
            } catch (Exception e) {
                System.err.println("Failed to capture context for " + accessor.getKey() + ": " + e.getMessage());
            }
        }
        return context;
    }


    @SuppressWarnings("unchecked")
    public void restoreContext(Map<String, Object> context) {
        if (context == null) return;

        for (ThreadLocalAccessor accessor : accessors) {
            try {
                Object value = context.get(accessor.getKey());
                if (value != null) {
                    accessor.setValue(value);
                }
            } catch (Exception e) {
                // Log warning but continue with other accessors
                System.err.println("Failed to restore context for " + accessor.getKey() + ": " + e.getMessage());
            }
        }
    }

    public void clearContext() {
        for (ThreadLocalAccessor<?> accessor : accessors) {
            try {
                accessor.clear();
            } catch (Exception e) {
                // Log warning but continue with other accessors
                System.err.println("Failed to clear context for " + accessor.getKey() + ": " + e.getMessage());
            }
        }
    }
}
