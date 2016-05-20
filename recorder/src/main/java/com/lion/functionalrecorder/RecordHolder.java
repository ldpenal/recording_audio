package com.lion.functionalrecorder;

import android.content.BroadcastReceiver;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.lion.functionalrecorder.broadcasts.PlaybackProgressReceiver;
import com.lion.functionalrecorder.listeners.PositionListener;
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
    private BroadcastReceiver broadcastReceiver;

    @BindView(R.id.sb_position) SeekBar sbPosition;
    @BindView(R.id.btn_play) ImageButton btnPlay;

    public RecordHolder(View itemView, ItemClicked clicked) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.eventListener = clicked;
        sbPosition.setOnSeekBarChangeListener(this);
    }

    public void bind(final Item item) {
        this.item = item;
        clean();

        broadcastReceiver = new PlaybackProgressReceiver<Item>(item, new PositionListener() {
            @Override
            public void forceUpdate() {
                if (item.getCurrentPosition() >= sbPosition.getProgress()) {
                    sbPosition.setProgress(item.getCurrentPosition());
                }
            }
        });

        sbPosition.setProgress(item.getCurrentPosition());
        updateUI();

        if (item.isStartSliding() && item.isPlaying()) {
            itemView.getContext().registerReceiver(broadcastReceiver, PlaybackProgressReceiver.INTENT_FILTER);
        }
    }

//    private void update() {
//        int restingProgress = 100 - item.currentPosition;
//        int time = (restingProgress * item.duration) / 100;
//
//        countDownTimer = new CountDownTimer(time, 100) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                int timeElapsed = (int) (item.duration - millisUntilFinished);
//                int progress = (int) Math.ceil((timeElapsed * 100) / item.duration);
//
//                item.currentPosition = progress;
//                sbPosition.setProgress(item.currentPosition);
//            }
//
//            @Override
//            public void onFinish() {
//                clean();
//                item.currentPosition = 0;
//            }
//        };
//
//        countDownTimer.start();
//    }

    private void clean() {
        stopCounter();

        if (broadcastReceiver != null) {
            try {
                itemView.getContext().unregisterReceiver(broadcastReceiver);
            } catch (Exception ex) {
            }
        }
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
                    item.setCurrentPosition(sbPosition.getProgress());
                    eventListener.play(item.getCurrentPosition(), getAdapterPosition());
                    item.setPlaying(true);
                    updateUI();
                }
                break;

            case AudioPlayer.TAG_PAUSE:
                if (canCallPlayer) {
                    eventListener.pause();
                    item.setPlaying(false);
                    stopCounter();
                    updateUI();
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        item.setCurrentPosition(progress);

        if (fromUser) {
            if (item.isPlaying()) {
                eventListener.seekTo(item.getCurrentPosition());
                stopCounter();
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
        if (item.isPlaying()) {
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
    }


}
