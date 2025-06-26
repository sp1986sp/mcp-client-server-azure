package com.example.server.accessors.accessorsImpl;

import com.example.server.accessors.ThreadLocalAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HeaderContextAccessor implements ThreadLocalAccessor<Map<String, String>> {

    private static final InheritableThreadLocal<Map<String, String>> headersHolder = new InheritableThreadLocal<>();

    @Override
    public String getKey() {
        return "headers-context";
    }

    @Override
    public Map<String, String> getValue() {
        return headersHolder.get();
    }

    @Override
    public void setValue(Map<String, String> value) {
        headersHolder.set(value);
    }

    @Override
    public void clear() {
        headersHolder.remove();
    }
}
