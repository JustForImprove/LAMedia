package com.la.mymedia.clip.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.la.mymedia.R;
import com.la.mymedia.live.NativePusher;

import java.io.File;

/**
 * 使用ffmpeg推流直播
 * Created by frank on 2018/2/2.
 */
public class PushActivity extends BaseActivity {

    private static final String TAG = PushActivity.class.getSimpleName();
    private static final String FILE_PATH = "/storage/4925-382B/1234.mp4";
    private static final String LIVE_URL = "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_177141605_8516378&key=76c1b39ac9038e0a70ffbe5563aa7ecf&schedule=rtmp";

    private EditText edit_file_path;

    private EditText edit_live_url;

    @Override
    int getLayoutId() {
        return R.layout.activity_push;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();
        initView();
    }

    private void initView() {
        edit_file_path = getView(R.id.edit_file_path);
        edit_live_url = getView(R.id.edit_live_url);
        edit_file_path.setText(FILE_PATH);
        edit_live_url.setText(LIVE_URL);

        initViewsWithClick(R.id.btn_push_stream);
    }

    private void startPushStreaming() {
        //TODO 视频文件格式为flv
        final String filePath = edit_file_path.getText().toString();
        final String liveUrl = edit_live_url.getText().toString();
        Log.i(TAG, "filePath=" + filePath);
        Log.i(TAG, "liveUrl=" + liveUrl);

        if(!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            //判断文件是否存在
            if(file.exists()){
                //开启子线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //开始推流
                        new NativePusher().pushStream(filePath, liveUrl);
                    }
                }).start();
            }else {
                showToast(getString(R.string.file_not_found));
            }
        }
    }

    @Override
    void onViewClick(View view) {
        if (view.getId() == R.id.btn_push_stream) {
            startPushStreaming();
        }
    }

    @Override
    void onSelectedFile(String filePath) {

    }
}
