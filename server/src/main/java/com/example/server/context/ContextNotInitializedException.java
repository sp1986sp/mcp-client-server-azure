package com.example.server.context;

public class ContextNotInitializedException extends RuntimeException {
    public ContextNotInitializedException(String message) {
        super(message);
    }
}
