package com.unamba.apilibrary.staticdata;

public enum EnumUserRole {
    ADMIN("ADMIN"),
    STUDENT("STUDENT");

    private String value;

    EnumUserRole(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}