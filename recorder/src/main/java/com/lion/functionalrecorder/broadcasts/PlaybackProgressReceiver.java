package com.lion.functionalrecorder.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.lion.functionalrecorder.model.BaseItem;
import com.lion.functionalrecorder.listeners.PositionListener;

/**
 * Created by lion on 5/20/16.
 */
public class PlaybackProgressReceiver<T extends BaseItem> extends BroadcastReceiver {

    public static final String CURRENT_POSITION = "current_position";
    public static final String ACTION_FILTER = "com.lion.functionalrecorder.play.".concat(CURRENT_POSITION);
    public static final String CURRENT_URL = "current_url";

    private T item;
    private PositionListener positionListener;

    public static final IntentFilter INTENT_FILTER = new IntentFilter(ACTION_FILTER);

    public PlaybackProgressReceiver(T item, PositionListener positionListener) {
        this.item = item;
        this.positionListener = positionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        if (ACTION_FILTER.equals(intentAction)) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                int currentPosition = extras.getInt(CURRENT_POSITION);
                String currentUrl = extras.getString(CURRENT_URL);

                if (item.getUrl().equals(currentUrl)) {
                    item.setCurrentPosition(currentPosition);

                    if (positionListener != null)
                        positionListener.forceUpdate();
                }
            }
        }
    }
}