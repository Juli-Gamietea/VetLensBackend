package com.api.vetlens.entity;

public enum Role {
    VET("VET"),
    DEFAULT("DEFAULT"),
    STUDENT("STUDENT");

    private String role;

    public String getRole() {
        return this.role;
    }
    Role(String role) {
        this.role = role;
    }
}
