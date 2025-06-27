package com.example.server.context;

import com.example.server.model.ContextParamDefault;
import com.example.server.model.CustomContextParam;
import io.micrometer.common.util.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;

import java.util.*;

public class CustomContext {

    private static ThreadLocal<Map<CustomContextParam, Object>> contextHolder = new NamedThreadLocal<>("customContextStorage");

    private static final String MODE_KEY = "mode";

    private CustomContext() {
    }

    public static void init() {
        if (isInitialized()) {
            throw new IllegalStateException("Context already initialized!!");
        }

        contextHolder.set(new HashMap<>());
        MDC.put(ContextParamDefault.REQUEST_ID.getParamKey(), UUID.randomUUID().toString());
        String correlationId = UUID.randomUUID().toString();
        MDC.put(ContextParamDefault.CORRELATION_ID.getParamKey(), correlationId);
    }

    public static void put(CustomContextParam contextParams, String values) {
        asertContextInitialized();
        if(StringUtils.isEmpty(contextParams.getParamKey())) {
            throw new IllegalArgumentException("contextParams.getParamKey() can never be null/blank");
        }

        MDC.put(contextParams.getParamKey(), values);
    }

    public static void putObject(CustomContextParam key, Object value) {
        asertContextInitialized();
        if(key == null) {
            throw new IllegalArgumentException("key can never be null/blank");
        }

        contextHolder.get().put(key, value);
    }

    public static String get(CustomContextParam contextParams) {
        asertContextInitialized();
        return MDC.get(contextParams.getParamKey());
    }

    public static <T> T getObject(CustomContextParam key) {
        asertContextInitialized();
        Class<T> type = key.contextType();
        return type.cast(contextHolder.get().get(key));
    }

    public static void remove(CustomContextParam contextParams) {
        asertContextInitialized();
        if(StringUtils.isEmpty(contextParams.getParamKey())) {
            throw new IllegalArgumentException("contextParams.getParamKey() can never be null/blank");
        }

        MDC.remove(contextParams.getParamKey());
    }

    public static Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }


    public static boolean isInitialized() {
        return contextHolder.get() != null;
    }

    public static void clear() {
        MDC.clear();
        contextHolder.remove();
    }

    public static void clear(CustomContextParam key) {
        asertContextInitialized();
        MDC.remove(key.getParamKey());
    }

    public static void clearObject(CustomContextParam key) {
        asertContextInitialized();
        contextHolder.get().remove(key);
    }


    private static void asertContextInitialized() {
        if (!isInitialized()) {
            throw new ContextNotInitializedException("Context is not initialized!!");
        }
    }

    // TODO: To be deleted. Adding it as part of support for ECDEV-11911
    public static <T> T getOpsObject(CustomContextParam key) {
        asertContextInitialized();
        Object object = contextHolder.get().get(key);
        if (Objects.nonNull(object)) {
            Class<T> type = (Class<T>) object.getClass();
            return type.cast(object);
        }
        return null;
    }

    public static boolean containsKey(CustomContextParam key) {
        return contextHolder.get().containsKey(key);
    }

    public static Map<CustomContextParam, Object> getCopyOfContextHolderMap() {
        if (contextHolder.get() == null) {
            return new HashMap<>();
        }
        return new HashMap<>(contextHolder.get());
    }

    public static void setContextHolderMap(Map<CustomContextParam, Object> map) {
        contextHolder.set(new HashMap<>(map));
    }

}
