package com.la.mymedia.live.activity;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.la.mymedia.R;
import com.la.mymedia.live.LivePusherNew;
import com.la.mymedia.live.camera2.Camera2Helper;
import com.la.mymedia.live.listener.LiveStateChangeListener;
import com.la.mymedia.live.param.AudioParam;
import com.la.mymedia.live.param.VideoParam;

public class LiveActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, LiveStateChangeListener {

    private final static String TAG = LiveActivity.class.getSimpleName();
    private final static String LIVE_URL = "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_177141605_8516378&key=76c1b39ac9038e0a70ffbe5563aa7ecf&schedule=rtmp";
    private final static int MSG_ERROR = 100;
    private SurfaceView textureView;
    private LivePusherNew livePusher;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_ERROR) {
                String errMsg = (String) msg.obj;
                if (!TextUtils.isEmpty(errMsg)) {
                    Toast.makeText(LiveActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        initView();
        initPusher();
    }

    private void initView() {
        findViewById(R.id.btn_swap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_swap) {//switch camera
                    livePusher.switchCamera();
                }
            }
        });
        ((ToggleButton) findViewById(R.id.btn_live)).setOnCheckedChangeListener(this);
        ((ToggleButton) findViewById(R.id.btn_mute)).setOnCheckedChangeListener(this);
        textureView = findViewById(R.id.surface_camera);
    }

    private void initPusher() {
        int width = 640;//resolution
        int height = 480;
        int videoBitRate = 800_000;//kb/s
        int videoFrameRate = 10;//fps
        VideoParam videoParam = new VideoParam(width, height,
                Integer.valueOf(Camera2Helper.CAMERA_ID_BACK), videoBitRate, videoFrameRate);
        int sampleRate = 44100;//sample rate: Hz
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int numChannels = 2;//channel number
        AudioParam audioParam = new AudioParam(sampleRate, channelConfig, audioFormat, numChannels);
        livePusher = new LivePusherNew(this, videoParam, audioParam);
        livePusher.setPreviewDisplay(textureView.getHolder());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.btn_live) {//start or stop living
            if (isChecked) {
                livePusher.startPush(LIVE_URL, this);
            } else {
                livePusher.stopPush();
            }
        } else if (id == R.id.btn_mute) {//mute or not
            Log.i(TAG, "isChecked=" + isChecked);
            livePusher.setMute(isChecked);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (livePusher != null) {
            livePusher.release();
        }
    }

    @Override
    public void onError(String msg) {
        Log.e(TAG, "errMsg=" + msg);
        mHandler.obtainMessage(MSG_ERROR, msg).sendToTarget();
    }
}
