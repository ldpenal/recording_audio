package com.lion.functionalrecorder.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lion.functionalrecorder.Item;
import com.lion.functionalrecorder.broadcasts.PlaybackProgressReceiver;
import com.lion.functionalrecorder.model.BaseItem;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lion on 5/16/16.
 */
public class AudioPlayer<T extends BaseItem> implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener {

    private final Context context;
    private MediaPlayer mediaPlayer;
    private PlayerListener playerListener;
    private AudioManager audioManager;
    private String dataSource;

    private Timer timer;
    private TimerTask timerTask;

    private final float volume = 1.0f;
    private final float minValue = 0.2f;

    private T data;

    public static final String TAG_PLAY = "play";
    public static final String TAG_PAUSE = "pause";
    public static final String TAG_STOP = "stop";

    private final long CHECK_INTERVAL = 100;
    private final long DELAY = CHECK_INTERVAL;

    public AudioPlayer(@NonNull Context context, @Nullable PlayerListener playerListener) {
        this.playerListener = playerListener;
        this.context = context;

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
                cancelTimers();

                if (data != null) {
                    data.setCurrentPosition(0);
                    data.setPlaying(false);
                }

                if (playerListener != null)
                    playerListener.onFinishPlayBack(getData());
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
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    Bundle extras = new Bundle();
                    extras.putString(PlaybackProgressReceiver.CURRENT_URL, ((Item) data).getUrl());
                    extras.putInt(PlaybackProgressReceiver.CURRENT_POSITION, timeToPercent(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));

                    Intent intent = new Intent(PlaybackProgressReceiver.ACTION_FILTER);
                    intent.putExtras(extras);
                    context.sendBroadcast(intent);
                }
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, DELAY, CHECK_INTERVAL);

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
                    data.setPlaying(true);
                    data.setStartSliding(true);
                    data.setDuration(mediaPlayer.getDuration());
                    playerListener.onStartPlayBack(data);
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
                    if (data.isPlaying()) {
                        mediaPlayer.start();
                        mediaPlayer.setVolume(volume, volume);
                    }
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
        cancelTimers();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

            if (data != null) {
                data.setPlaying(false);
            }

            if (playerListener != null)
                playerListener.onPausedPlayBack(data);
        }
    }

    public void stop() {
        cancelTimers();

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
        cancelTimers();

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

    private void cancelTimers() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public interface PlayerListener<D> {
        void onFinishPlayBack(D data);
        void onPausedPlayBack(D data);
        void onStoppedPlayBack(D data);
        void onStartPlayBack(D data);
        void onUpdateSeek(D data);
    }
}
