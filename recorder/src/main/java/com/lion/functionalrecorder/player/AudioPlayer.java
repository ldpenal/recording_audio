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
public class AudioPlayer<T> implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private PlayerListener playerListener;
    private AudioManager audioManager;
    private String dataSource;

    private final float volume = 1.0f;
    private final float minValue = 0.2f;

    private T data;

    public static final String TAG_PLAY = "play";
    public static final String TAG_PAUSE = "pause";
    public static final String TAG_STOP = "stop";

    public AudioPlayer(@NonNull Context context, @Nullable PlayerListener playerListener) {
        this.playerListener = playerListener;

        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void prepare(String url) {
        mediaPlayer.reset();

        dataSource = url;

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playerListener != null) {
                    playerListener.onFinishPlayBack(getData());
                }
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
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                if (playerListener != null) {
                    playerListener.onStartPlayBack(data, mediaPlayer.getDuration());
                }
            }
        }
    }

    public boolean isPlaying() {
        return (mediaPlayer != null) ? mediaPlayer.isPlaying() : false;
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
                try {
                    pause();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (audioManager != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.setVolume(minValue, minValue);
                    }
                }
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        play();
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (playerListener != null)
                playerListener.onPausedPlayBack(data);
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();

            if (playerListener != null)
                playerListener.onStoppedPlayBack(data);
        }
    }

    public void seek(int x) {
        int timeMillis = mediaPlayer.getDuration();
        int pos = (int) Math.ceil((timeMillis * x) / 100);

        if (mediaPlayer != null) {
            mediaPlayer.seekTo(pos);

            if (playerListener != null) {
                playerListener.onUpdateSeek(data);
            }
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = null;
        audioManager = null;
        playerListener = null;
    }

    public String getDataSource() {
        return dataSource;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int timeToPercent(int timeX, int totalDuration) {
        int percent = (timeX * 100) / totalDuration;
        return percent;
    }

    public static int percentToTime(int percent, int totalDuration) {
        int time = (totalDuration * percent) / 100;
        return time;
    }

    public interface PlayerListener<D> {
        void onFinishPlayBack(D data);
        void onPausedPlayBack(D data);
        void onStoppedPlayBack(D data);
        void onStartPlayBack(D data, int totalDuration);
        void onUpdateSeek(D data);
    }
}
