package com.lion.automated.db;

/**
 * Created by lion on 5/4/16.
 */
public class LimitedTable {

    private String name;
    private int rowsLimit;

    public LimitedTable(String name, int rowsLimit) {
        this.name = name;
        this.rowsLimit = rowsLimit;
    }

    public String getName() {
        return name;
    }

    public int getRowsLimit() {
        return rowsLimit;
    }
}
