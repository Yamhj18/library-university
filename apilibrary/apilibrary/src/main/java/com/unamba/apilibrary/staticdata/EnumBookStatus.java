package com.unamba.apilibrary.staticdata;

public enum EnumBookStatus {
    AVAILABLE("Available"),
    UNAVAILABLE("Unavailable");

    private String value;

    EnumBookStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}