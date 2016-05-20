package com.lion.functionalrecorder;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lion.functionalrecorder.player.AudioPlayer;
import com.lion.functionalrecorder.recorder.AudioBitRate;
import com.lion.functionalrecorder.recorder.AudioChannel;
import com.lion.functionalrecorder.recorder.Recorder;
import com.lion.functionalrecorder.recorder.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AudioPlayer.PlayerListener<Item> {

    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.btn_recorder) Button btnRecorder;
    @BindView(R.id.btn_play) Button btnPlay;
    @BindView(R.id.rv_test) RecyclerView rvTests;
    @BindView(R.id.srl_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private Recorder recorder;
    private Settings.Builder recorderSettings;
    private AudioPlayer<Item> player;

    private String file = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Documents");

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initRecycler();
        loadUrls();

        swipeRefreshLayout.setOnRefreshListener(this);

        recorder = new Recorder();
        recorderSettings = new Settings.Builder()
                .channelsAmount(AudioChannel.STEREO)
                .encodingBitRate(AudioBitRate.ENCODING_HIGH)
                .samplingBitRate(AudioBitRate.SAMPLING_48khz)
                .filePath(file);

        player = new AudioPlayer(this, this);
    }

    private void loadUrls() {
        File[] files = new File(file).listFiles();
        ArrayList<Item> items = new ArrayList<>();

        for (File f : files) {
            if (f.getName().contains(".mp4")) {
                Item item = new Item(f.getName(), f.getAbsolutePath());
                items.add(item);
            } else {
                continue;
            }
        }

        adapter.addItems(items);
        adapter.notifyDataSetChanged();
    }

    private void initRecycler() {
        adapter = new Adapter(new RecordHolder.ItemClicked() {
            @Override
            public void play(int percentage, int position) {
                Item item = adapter.getItem(position);

                ArrayList<Item> items = adapter.getItems();

                for (Item aux : items) {
                    if (!aux.equals(item)) {
                        aux.setPlaying(false);
                        aux.setStartSliding(false);
                    }
                }

                adapter.notifyDataSetChanged();

                player.prepare(item.getUrl());
                player.seek(percentage);
                player.setData(item);
                player.play();
            }

            @Override
            public void pause() {
                if (player.isPlaying()) {
                    player.stop();
                }
            }

            @Override
            public void seekTo(int position) {
                player.seek(position);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTests.setLayoutManager(linearLayoutManager);
        rvTests.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.onDestroy();
        player.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
        recorder.onPause();
    }

    @OnClick(R.id.btn_recorder)
    public void onRecorderClicked(View v) {
        String tag = (String) v.getTag();

        switch (tag) {
            case "record":
                try {
                    recorder.prepare(recorderSettings.build());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (Settings.FileNameIllegalState fnise) {
                    fnise.printStackTrace();
                } catch (Settings.DirectoryException de) {
                    de.printStackTrace();
                }

                btnRecorder.setText("recording");
                btnRecorder.setTag("recording");

                recorder.startRecording();
                break;

            case "recording":
                btnRecorder.setText("record");
                btnRecorder.setTag("record");

                recorder.stopRecording();
                break;
        }
    }

    @OnClick(R.id.btn_play)
    public void onClick(View v) {
        String tag = (String) v.getTag();

        switch (tag) {
            case "play":
                player.prepare(recorder.getSettings().getAbsolutePath());
                player.play();
                btnPlay.setTag("pause");
                break;

            case "pause":
                player.pause();
                btnPlay.setTag("play");
                break;
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        loadUrls();
    }

    @Override
    public void onFinishPlayBack(Item data) {
        adapter.updateItem(data);
    }

    @Override
    public void onPausedPlayBack(Item data) {
        adapter.updateItem(data);
    }

    @Override
    public void onStoppedPlayBack(Item data) {
    }

    @Override
    public void onStartPlayBack(Item data) {
        adapter.updateItem(data);
    }

    @Override
    public void onUpdateSeek(Item data) {
        // int index = indexOf(data);
    }
}
