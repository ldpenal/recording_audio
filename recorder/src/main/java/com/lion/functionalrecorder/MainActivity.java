package com.lion.functionalrecorder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lion.functionalrecorder.player.AudioPlayer;
import com.lion.functionalrecorder.recorder.AudioBitRate;
import com.lion.functionalrecorder.recorder.AudioChannel;
import com.lion.functionalrecorder.recorder.Recorder;
import com.lion.functionalrecorder.recorder.Settings;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.btn_recorder) Button btnRecorder;
    @BindView(R.id.btn_play) Button btnPlay;

    private Recorder recorder;
    private Settings.Builder recorderSettings;
    AudioPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recorder = new Recorder();

        recorderSettings= new Settings.Builder()
                .channelsAmount(AudioChannel.STEREO)
                .encodingBitRate(AudioBitRate.ENCODING_HIGH)
                .samplingBitRate(AudioBitRate.SAMPLING_48khz)
                .filePath(Environment.getExternalStorageDirectory().getAbsolutePath().concat("/Documents"));

        player = new AudioPlayer(this, null);
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
        player.pause();
    }

    @OnClick(R.id.btn_recorder)
    public void onRecorderClicked(View v) {
        String tag = (String) v.getTag();

        switch (tag) {
            case "record":
                try {
                    player.prepare(recorder.getSettings().getAbsolutePath());
                    recorder.prepare(recorderSettings.build());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Settings.FileNameIllegalState fileNameIllegalState) {
                    fileNameIllegalState.printStackTrace();
                } catch (Settings.DirectoryException e) {
                    e.printStackTrace();
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
                player.play();
                btnPlay.setTag("pause");
                break;

            case "pause":
                player.pause();
                btnPlay.setTag("play");
                break;
        }
    }
}
