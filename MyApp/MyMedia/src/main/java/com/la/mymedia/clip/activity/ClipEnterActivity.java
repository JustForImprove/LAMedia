package com.la.mymedia.clip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.la.mymedia.R;

/**
 * The main entrance of all Activity
 * Created by frank on 2018/1/23.
 */
public class ClipEnterActivity extends BaseActivity {

    @Override
    int getLayoutId() {
        return R.layout.activity_clip_enter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViewsWithClick(
                R.id.btn_audio,
                R.id.btn_video,
                R.id.btn_media,
                R.id.btn_play,
                R.id.btn_push,
                R.id.btn_live,
                R.id.btn_filter,
                R.id.btn_preview,
                R.id.btn_probe
        );
    }

    @Override
    public void onViewClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        if (id == R.id.btn_audio) {//handle audio
            intent.setClass(ClipEnterActivity.this, AudioHandleActivity.class);
        } else if (id == R.id.btn_video) {//handle video
            intent.setClass(ClipEnterActivity.this, VideoHandleActivity.class);
        } else if (id == R.id.btn_media) {//handle media
            intent.setClass(ClipEnterActivity.this, MediaHandleActivity.class);
        } else if (id == R.id.btn_play) {//media play
            intent.setClass(ClipEnterActivity.this, MediaPlayerActivity.class);
        } else if (id == R.id.btn_push) {//pushing
            intent.setClass(ClipEnterActivity.this, PushActivity.class);
        } else if (id == R.id.btn_live) {//realtime living with rtmp stream
            intent.setClass(ClipEnterActivity.this, LiveActivity.class);
        } else if (id == R.id.btn_filter) {//filter effect
            intent.setClass(ClipEnterActivity.this, FilterActivity.class);
        } else if (id == R.id.btn_preview) {//preview thumbnail
            intent.setClass(ClipEnterActivity.this, VideoPreviewActivity.class);
        } else if (id == R.id.btn_probe) {//probe media format
            intent.setClass(ClipEnterActivity.this, ProbeFormatActivity.class);
        }
        startActivity(intent);
    }

    @Override
    void onSelectedFile(String filePath) {

    }

}
