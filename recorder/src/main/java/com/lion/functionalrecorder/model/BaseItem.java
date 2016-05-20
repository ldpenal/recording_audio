package com.lion.functionalrecorder.model;

/**
 * Created by lion on 5/18/16.
 */
public class BaseItem {
    boolean isPlaying;
    boolean startSliding;
    int duration;
    int currentPosition;
    public String url;

    public BaseItem() {
        isPlaying = false;
        startSliding = false;
        duration = 0;
        currentPosition = 0;
        url = "";
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isStartSliding() {
        return startSliding;
    }

    public void setStartSliding(boolean startSliding) {
        this.startSliding = startSliding;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
