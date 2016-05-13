package com.lion.automated.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Hashtable;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by lion on 5/4/16.
 */
public class AutomatedTriggers {

    private Hashtable<String, Trigger> triggers = null;

    public AutomatedTriggers() {
        triggers = new Hashtable<>();
    }

    public void push(Trigger trigger) {
        triggers.put(trigger.getName(), trigger);
    }

    public void onCreate(SQLiteDatabase database) {
        Observable.from(triggers.values())
                .observeOn(Schedulers.io())
                .subscribe(trigger -> {
                    database.execSQL(trigger.getSqlStatement());
                });
    }
}
