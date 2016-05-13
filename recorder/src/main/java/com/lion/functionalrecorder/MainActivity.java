package com.lion.functionalrecorder;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lion.functionalrecorder.recorder.AudioBitRate;
import com.lion.functionalrecorder.recorder.AudioChannel;
import com.lion.functionalrecorder.recorder.Recorder;
import com.lion.functionalrecorder.recorder.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.btn_recorder) Button btnRecorder;

    private Recorder recorder;
    private Settings.Builder recorderSettings;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.onDestroy();
    }

    @OnClick(R.id.btn_recorder)
    public void onRecorderClicked(View v) {
        String tag = (String) v.getTag();

        switch (tag) {
            case "record":
                try {
                    recorder.prepare(recorderSettings.build());
                } catch (Exception e) {
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
}
