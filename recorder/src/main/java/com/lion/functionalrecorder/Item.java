package com.lion.functionalrecorder;

import com.lion.functionalrecorder.model.BaseItem;

/**
 * Created by lion on 5/16/16.
 */
public class Item extends BaseItem {
    public String title;

    public Item(String title, String url) {
        super();
        setTitle(title);
        setUrl(url);
        setCurrentPosition(0);
        setPlaying(false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "URL: " + url + "\n" +
                "TITLE: " + title + "\n" +
                "DURATION: " + getDuration() + "\n" +
                "CURRENT: " + getCurrentPosition() + "\n";
    }
}