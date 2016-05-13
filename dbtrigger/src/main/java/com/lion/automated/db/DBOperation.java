package com.lion.automated.db;

/**
 * Created by lion on 5/4/16.
 */
public enum DBOperation {
    INSERT("INSERT "),
    DELETION("DELETE "),
    UPDATE("UPDATE ");

    private final String operation;

    DBOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
