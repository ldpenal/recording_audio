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
    private boolean customFileName;

    public Settings(AudioChannel audioChannel, boolean customFileName, AudioBitRate encodingBitRate, String fileName, String filePath, AudioBitRate samplingBitRate) {
        this.audioChannel = audioChannel;
        this.customFileName = customFileName;
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
        private boolean customFileName;

        public Builder() {
            encodingBitRate = AudioBitRate.DEFAULT_ENCODING;
            samplingBitRate = AudioBitRate.DEFAULT_SAMPLING;
            audioChannel = AudioChannel.DEFAULT_CHANNELS;
            customFileName = false;

            fileName = "";
            filePath = "";
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

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder channelsAmount(AudioChannel amount) {
            audioChannel = amount;
            return this;
        }

        public Builder customFileName(boolean customFileName) {
            this.customFileName = customFileName;
            return this;
        }

        public Settings build() throws FileNameIllegalState, DirectoryException {
            if (!customFileName) {
                fileName(System.currentTimeMillis() + ".mp4");
            }

            if ("".equals(fileName))
                throw new FileNameIllegalState("The file name was not provided");

            if ("".equals(filePath))
                throw new DirectoryException("The file directory was not provided");

            return new Settings(audioChannel, customFileName, encodingBitRate, fileName, filePath, samplingBitRate);
        }
    }

    public static class FileNameIllegalState extends Exception {

        public FileNameIllegalState(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class DirectoryException extends Exception {

        public DirectoryException(String detailMessage) {
            super(detailMessage);
        }
    }
}
