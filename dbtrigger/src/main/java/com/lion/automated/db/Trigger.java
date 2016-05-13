package com.lion.automated.db;

import android.os.Build;

import rx.Observable;

/**
 * Created by lion on 5/4/16.
 */
public class Trigger {

    public static final String CREATE_TRIGGER = "CREATE TRIGGER ";
    public static final String ON = "ON ";
    public static final String BEGIN = "BEGIN\n";
    public static final String END = "\nEND";

    private String sql;
    private String name;

    private Trigger(String name, String sql) {
        this.sql = sql;
        this.name = name;
    }

    public String getSqlStatement() {
        return sql;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private String name;
        private String tableName;
        private String logic;

        private TriggerMoment moment;
        private DBOperation operation;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder logicStatements(String logic) {
            this.logic = logic;
            return this;
        }

        public Builder moment(TriggerMoment moment) {
            this.moment = moment;
            return this;
        }

        public Builder operationEvent(DBOperation operation) {
            this.operation = operation;
            return this;
        }

        void testString(String string, String defaultString) {
            if (string.equals(defaultString))
                throw new IllegalStateException("");
        }

        public Trigger build() {
            testString(name, "");
            testString(tableName, "");

            StringBuilder builder = new StringBuilder();
            builder = builder
                    .append(Trigger.CREATE_TRIGGER)
                    .append(name.concat(" "))
                    .append(moment.getMoment())
                    .append(operation.getOperation())
                    .append(Trigger.ON)
                    .append(tableName.concat(" "))
                    .append(Trigger.BEGIN)
                    .append(logic.toString())
                    .append(Trigger.END);

            return new Trigger(name, builder.toString());
        }
    }
}
