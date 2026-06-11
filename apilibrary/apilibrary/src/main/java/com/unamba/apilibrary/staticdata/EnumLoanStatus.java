package com.unamba.apilibrary.staticdata;

public enum EnumLoanStatus {
    ACTIVE("Active"),
    RETURNED("Returned"),
    OVERDUE("Overdue");

    private String value;

    EnumLoanStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}