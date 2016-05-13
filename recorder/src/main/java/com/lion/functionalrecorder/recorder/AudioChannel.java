package com.lion.functionalrecorder.recorder;

/**
 * Created by lion on 5/11/16.
 */

public enum AudioChannel {
    MONO(1),
    STEREO(2),
    DEFAULT_CHANNELS(MONO.getChannels());

    private int channels;

    AudioChannel(int channels) {
        this.channels = channels;
    }

    public int getChannels() {
        return channels;
    }
}