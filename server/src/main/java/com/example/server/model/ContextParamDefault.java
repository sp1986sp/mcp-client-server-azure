package com.example.server.model;

public enum ContextParamDefault implements CustomContextParam {
    REQUEST_ID("REQUEST_ID"),

    CORRELATION_ID("CORRELATION_ID"),

    X_TRAFFIC_TYPE("x-traffic-type"),

    X_TRAFFIC_COLOR("x-traffic-color");


    private String paramName;

    ContextParamDefault(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String getParamKey() {
        return paramName;
    }

    @Override
    public Class<?> contextType() {
        return String.class;
    }
}
