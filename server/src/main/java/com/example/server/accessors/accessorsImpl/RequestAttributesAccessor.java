package com.example.server.accessors.accessorsImpl;

import com.example.server.accessors.ThreadLocalAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class RequestAttributesAccessor implements ThreadLocalAccessor<RequestAttributes> {

    private static final InheritableThreadLocal<RequestAttributes> requestAttributesHolder = new InheritableThreadLocal<>();

    @Override
    public String getKey() {
        return "request-attributes";
    }

    @Override
    public RequestAttributes getValue() {
        RequestAttributes stored = requestAttributesHolder.get();
        if (stored != null) {
            return stored;
        }
        // Try to get from Spring's RequestContextHolder as fallback
        try {
            return RequestContextHolder.getRequestAttributes();
        } catch (IllegalStateException e) {
            // No request context available
            return null;
        }
    }

    @Override
    public void setValue(RequestAttributes value) {
        requestAttributesHolder.set(value);
        if (value != null) {
            try {
                RequestContextHolder.setRequestAttributes(value);
            } catch (Exception e) {
                System.err.println("Failed to set RequestAttributes: " + e.getMessage());
            }
        }
    }

    @Override
    public void clear() {
        requestAttributesHolder.remove();
        try {
            RequestContextHolder.resetRequestAttributes();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
}