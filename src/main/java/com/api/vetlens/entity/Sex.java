package com.api.vetlens.entity;

public enum Sex {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String sex;

    public String getSex() {
        return this.sex;
    }

    Sex(String sex) {
        this.sex = sex;
    }
}
