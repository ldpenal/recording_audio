package com.lion.functionalrecorder.storage;

import android.media.MediaRecorder;

/**
 * Created by lion on 5/11/16.
 */
public class AudioFile {

    private AudioType audioType;
    private String outputFile;

    public AudioFile() {
    }

    public AudioType getAudioType() {
        return audioType;
    }

    public void setAudioType(AudioType audioType) {
        this.audioType = audioType;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public enum AudioType {
        MP4(MediaRecorder.OutputFormat.MPEG_4);

        private final int format;

        AudioType(int format) {
            this.format = format;
        }

        public int getFormat() {
            return format;
        }
    }
}
