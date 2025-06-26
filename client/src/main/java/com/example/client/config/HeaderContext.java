package com.example.client.config;

import java.util.Map;

public class HeaderContext {
    private static final ThreadLocal<Map<String, String>> context = new ThreadLocal<>();

    public static void set(Map<String, String> headers) {
        context.set(headers);
    }

    public static Map<String, String> get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}