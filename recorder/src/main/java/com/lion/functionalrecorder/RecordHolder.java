package com.lion.functionalrecorder;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.lion.functionalrecorder.player.AudioPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lion on 5/16/16.
 */
public class RecordHolder extends RecyclerView.ViewHolder implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "ldpenal";

    private Item item;
    private ItemClicked eventListener;
    private CountDownTimer countDownTimer;

    @BindView(R.id.sb_position) SeekBar sbPosition;
    @BindView(R.id.btn_play) ImageButton btnPlay;

    public RecordHolder(View itemView, ItemClicked clicked) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.eventListener = clicked;
        sbPosition.setOnSeekBarChangeListener(this);
    }

    public void bind(Item item) {
        this.item = item;
        clean();

        sbPosition.setProgress(item.currentPosition);
        updateUI();

        if (item.startSliding && item.isPlaying) {
            update();
        }
    }

    private void update() {
        int restingProgress = 100 - item.currentPosition;
        int time = (restingProgress * item.duration) / 100;

        countDownTimer = new CountDownTimer(time, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int timeElapsed = (int) (item.duration - millisUntilFinished);
                int progress = (int) Math.ceil((timeElapsed * 100) / item.duration);

                item.currentPosition = progress;
                sbPosition.setProgress(item.currentPosition);
            }

            @Override
            public void onFinish() {
                clean();
                item.currentPosition = 0;
            }
        };

        countDownTimer.start();
    }

    private void clean() {
        stopCounter();
    }

    private void stopCounter() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = null;
    }

    @OnClick(R.id.btn_play)
    public void onPlayClicked(View view) {
        String tag = (String) view.getTag();
        boolean canCallPlayer = eventListener != null;

        switch (tag) {
            case AudioPlayer.TAG_PLAY:
                if (canCallPlayer) {
                    item.currentPosition = sbPosition.getProgress();
                    eventListener.play(item.currentPosition, getAdapterPosition());
                    item.isPlaying = true;
                    updateUI();
                }
                break;

            case AudioPlayer.TAG_PAUSE:
                if (canCallPlayer) {
                    eventListener.pause();
                    item.isPlaying = false;
                    stopCounter();
                    updateUI();
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        item.currentPosition = progress;

        if (fromUser) {
            if (item.isPlaying) {
                eventListener.seekTo(item.currentPosition);
                stopCounter();
                update();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    protected void updateUI() {
        if (item.isPlaying) {
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
            btnPlay.setTag(AudioPlayer.TAG_PAUSE);
        } else {
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            btnPlay.setTag(AudioPlayer.TAG_PLAY);
        }
    }

    public Item getItem() {
        return item;
    }

    interface ItemClicked {
        void play(int percentage, int position);
        void pause();
        void seekTo(int position);
        void removeIfPlaying(Item item);
    }
}
