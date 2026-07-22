package com.unamba.apilibrary.staticdata;

public enum EnumGuaranteeType {
    DNI("DNI"),
    CARNET_UNIVERSITARIO("CarnetUniversitario");

    private final String value;

    EnumGuaranteeType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
