package com.lion.functionalrecorder.recorder;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Created by lion on 5/11/16.
 */
public class Settings {

    private AudioBitRate encodingBitRate;
    private AudioBitRate samplingBitRate;
    private AudioChannel audioChannel;
    private String filePath;
    private String fileName;

    public Settings(AudioChannel audioChannel, AudioBitRate encodingBitRate, String fileName, String filePath, AudioBitRate samplingBitRate) {
        this.audioChannel = audioChannel;
        this.encodingBitRate = encodingBitRate;
        this.fileName = fileName;
        this.filePath = filePath;
        this.samplingBitRate = samplingBitRate;
    }

    public AudioChannel getAudioChannel() {
        return audioChannel;
    }

    public AudioBitRate getEncodingBitRate() {
        return encodingBitRate;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getAbsolutePath() {
        return filePath + "/" + fileName;
    }

    public AudioBitRate getSamplingBitRate() {
        return samplingBitRate;
    }

    public String getFileName() {
        return fileName;
    }

    public static class Builder {

        private AudioBitRate encodingBitRate;
        private AudioBitRate samplingBitRate;
        private AudioChannel audioChannel;
        private String filePath;
        private String fileName;

        public Builder() {
            encodingBitRate = AudioBitRate.DEFAULT_ENCODING;
            samplingBitRate = AudioBitRate.DEFAULT_SAMPLING;
            audioChannel = AudioChannel.DEFAULT_CHANNELS;
        }

        public Builder encodingBitRate(AudioBitRate bitRate) {
            encodingBitRate = bitRate;
            return this;
        }

        public Builder samplingBitRate(AudioBitRate bitRate) {
            samplingBitRate = bitRate;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        protected Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder channelsAmount(AudioChannel amount) {
            audioChannel = amount;
            return this;
        }

        public Settings build() {
            fileName(System.currentTimeMillis() + ".mp4");
            return new Settings(audioChannel, encodingBitRate, fileName, filePath, samplingBitRate);
        }
    }
}
