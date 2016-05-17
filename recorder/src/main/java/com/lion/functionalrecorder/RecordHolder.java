package com.lion.functionalrecorder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lion on 5/16/16.
 */
public class RecordHolder extends RecyclerView.ViewHolder implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "ldpenal";
    private Item item;
    private ItemClicked itemClicked;

    @BindView(R.id.sb_position) SeekBar sbPosition;
    @BindView(R.id.btn_play) ImageButton btnPlay;

    public RecordHolder(View itemView, ItemClicked clicked) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.itemClicked = clicked;
    }

    public void bind(Item item) {
        this.item = item;

        sbPosition.setProgress(item.currentPosition);
    }

    public void clean() {
        item.currentPosition = 0;
    }

    @OnClick(R.id.btn_play)
    public void onPlayClicked(View view) {
        itemClicked.play(sbPosition.getProgress(), getAdapterPosition());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        item.currentPosition = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public Item getItem() {
        return item;
    }

    interface ItemClicked {
        void play(int percentage, int position);

        void removeIfPlaying(Item item);
    }
}
