package com.example.server.accessors;

public interface ThreadLocalAccessor<T> {
    String getKey();

    T getValue();

    void setValue(T value);

    void clear();
}
