package com.lion.automated.db;

/**
 * Created by lion on 5/4/16.
 */
public enum Constraint {
    CONSTRAINT_NOTHING(""),
    KEEP_LASTS_LIMITED("");

    private final String cons;

    Constraint(String cons) {
        this.cons = cons;
    }

    @Override
    public String toString() {
        return cons;
    }
}
