package com.example.server.model;

public interface CustomContextParam {
    default String getParamKey() {
        return "";
    }

    <T> Class<T> contextType();
}
