package com.lion.functionalrecorder.recorder;

/**
 * Created by lion on 5/11/16.
 */
public enum AudioBitRate {
    ENCODING_LOW(24000),
    ENCODING_MID(64000),
    ENCODING_HIGH(128000),
    SAMPLING_8kHz(8000),
    SAMPLING_16kHz(16000),
    SAMPLING_32kHz(32000),
    SAMPLING_48khz(48000),
    DEFAULT_ENCODING(ENCODING_HIGH.getBitRate()),
    DEFAULT_SAMPLING(SAMPLING_16kHz.getBitRate());

    private final int bitRate;

    AudioBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getBitRate() {
        return bitRate;
    }
}
