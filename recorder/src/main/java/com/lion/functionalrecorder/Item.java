package com.lion.functionalrecorder;

/**
 * Created by lion on 5/16/16.
 */
public class Item {
    public String url;
    public String title;
    public int currentPosition;

    public Item(String title, String url) {
        this.title = title;
        this.url = url;
        this.currentPosition = 0;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}