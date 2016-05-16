package com.lion.functionalrecorder.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by lion on 5/16/16.
 */
public class AudioPlayer implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private PlayerListener playerListener;
    private AudioManager audioManager;

    private final float volume = 1.0f;
    private final float minValue = 0.2f;

    public AudioPlayer(@NonNull Context context, @Nullable PlayerListener playerListener) {
        this.playerListener = playerListener;

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void prepare(String url) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    public void release() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();

        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void play() {
        int result = 0;

        if (audioManager != null)
            result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
        } else {
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    mediaPlayer.setVolume(volume, volume);
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                release();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(minValue, minValue);
                }
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void seek(int x) {
        int tmills = mediaPlayer.getDuration();
        int pos = (int) Math.ceil((tmills * x) / 100);

        if (mediaPlayer != null) {
            mediaPlayer.seekTo(pos);
        }
    }

    public void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        audioManager = null;
    }

    interface PlayerListener {
        void onFinishPlayBack();
    }
}
