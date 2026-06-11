package com.unamba.apilibrary.staticdata;

public enum EnumUserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String value;

    EnumUserStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}