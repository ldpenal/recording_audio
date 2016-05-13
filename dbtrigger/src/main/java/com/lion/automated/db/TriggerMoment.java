package com.lion.automated.db;

/**
 * Created by lion on 5/4/16.
 */
public enum  TriggerMoment {
    BEFORE("BEFORE "),
    AFTER("AFTER ");

    private final String moment;

    TriggerMoment(String moment) {
        this.moment = moment;
    }

    public String getMoment() {
        return moment;
    }
}
