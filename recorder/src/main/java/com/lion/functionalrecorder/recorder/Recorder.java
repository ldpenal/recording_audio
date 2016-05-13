package com.lion.functionalrecorder.recorder;

import android.media.MediaRecorder;

/**
 * Created by lion on 5/11/16.
 */
public class Recorder {

    private MediaRecorder mediaRecorder;
    private Settings settings;
    private boolean prepared = false;
    private boolean isRecording = false;

    public Recorder() {
    }

    public void prepare(Settings settings) throws Exception {
        if (settings == null) {
            throw new IllegalStateException("Settings not provided to MediaRecord;");
        }

        this.settings = settings;

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mediaRecorder.setOutputFile(settings.getAbsolutePath());
        mediaRecorder.setAudioChannels(settings.getAudioChannel().getChannels());
        mediaRecorder.setAudioEncodingBitRate(settings.getEncodingBitRate().getBitRate());
        mediaRecorder.setAudioSamplingRate(settings.getSamplingBitRate().getBitRate());

        try {
            mediaRecorder.prepare();
            prepared = true;
        } catch (Exception exception) {
            prepared = false;
        }
    }

    public void startRecording() {
        if (!prepared || isRecording) {
            return;
        }

        try {
            mediaRecorder.start();
            isRecording = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopRecording() {
        if (isRecording) {
            try {
                isRecording = !isRecording;
                prepared = false;

                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onDestroy() {
        try {
            mediaRecorder.release();
            settings = null;
            mediaRecorder = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Settings getSettings() {
        return settings;
    }
}
