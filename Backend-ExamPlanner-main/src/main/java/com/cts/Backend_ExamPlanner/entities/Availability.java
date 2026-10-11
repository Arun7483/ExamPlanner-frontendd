package com.cts.Backend_ExamPlanner.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Availability {
    YES,NO;

    @JsonCreator
    public static Availability fromString(String value) {
        return Availability.valueOf(value.toUpperCase());
    }
}
