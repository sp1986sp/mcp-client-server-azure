package com.example.server.accessors.accessorsImpl;

import com.example.server.accessors.ThreadLocalAccessor;
import com.example.server.context.CustomContext;
import com.example.server.model.CustomContextParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomContextAccessor implements ThreadLocalAccessor<Map<CustomContextParam, Object>> {

    private static final InheritableThreadLocal<Map<CustomContextParam, Object>> contextHolder = new InheritableThreadLocal<>();

    @Override
    public String getKey() {
        return "dt-context";
    }

    @Override
    public Map<CustomContextParam, Object> getValue() {
        Map<CustomContextParam, Object> stored = contextHolder.get();
        if (stored != null) {
            return stored;
        }
        return CustomContext.isInitialized()
                ? CustomContext.getCopyOfContextHolderMap()
                : new HashMap<>();
    }

    @Override
    public void setValue(Map<CustomContextParam, Object> value) {
        contextHolder.set(value);
        if (value != null) {
            CustomContext.setContextHolderMap(value);
        }
    }

    @Override
    public void clear() {
        contextHolder.remove();
        CustomContext.clear();
    }
}