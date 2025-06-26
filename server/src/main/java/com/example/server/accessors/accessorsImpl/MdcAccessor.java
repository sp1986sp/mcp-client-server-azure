package com.example.server.accessors.accessorsImpl;

import com.example.server.accessors.ThreadLocalAccessor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MdcAccessor implements ThreadLocalAccessor<Map<String, String>> {

    private static final InheritableThreadLocal<Map<String, String>> mdcContextHolder = new InheritableThreadLocal<>();

    @Override
    public String getKey() {
        return "mdc-context";
    }

    @Override
    public Map<String, String> getValue() {
        Map<String, String> stored = mdcContextHolder.get();
        if (stored != null) {
            return stored;
        }
        return MDC.getCopyOfContextMap();
    }

    @Override
    public void setValue(Map<String, String> value) {
        mdcContextHolder.set(value);
        if (value != null) {
            MDC.setContextMap(value);
        }
    }

    @Override
    public void clear() {
        mdcContextHolder.remove();
        MDC.clear();
    }
}
