package com.la.media.Live;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.la.mymedia.R;
import com.la.mymedia.live.LivePusherNew;
import com.la.mymedia.live.camera2.Camera2Helper;
import com.la.mymedia.live.listener.LiveStateChangeListener;
import com.la.mymedia.live.param.AudioParam;
import com.la.mymedia.live.param.VideoParam;

public class LiveFragment extends Fragment implements Live.View, LiveStateChangeListener {
    private Context context;
    private Live.Callback Callback;
    private Activity activity;
    private View view;
    private SurfaceView surfaceView;
    private ImageButton camera_switcher;
    private CheckBox start_or_stop;
    private LivePusherNew livePusher;
    private String rtmpUrl = "";

    public LiveFragment(Live.Callback liveCallback) {
        this.Callback = liveCallback;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.live_fragment, null);
        surfaceView = view.findViewById(R.id.live_surface_view);
        camera_switcher = view.findViewById(R.id.live_camera_switcher);
        start_or_stop = view.findViewById(R.id.live_start_or_stop);
        camera_switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livePusher.switchCamera();
            }
        });

        start_or_stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (start_or_stop.isChecked() == true) {
                    // 选中开始推流
                    initPusher();
                    livePusher.startPush(rtmpUrl, LiveFragment.this);
                } else {
                    // 未选中停止推流
                    livePusher.stopPush();
                }
            }
        });

        return view;
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
        livePusher = new LivePusherNew(activity, videoParam, audioParam);
        livePusher.setPreviewDisplay(surfaceView.getHolder());
    }

    @Override
    public void onError(String msg) {

    }

}