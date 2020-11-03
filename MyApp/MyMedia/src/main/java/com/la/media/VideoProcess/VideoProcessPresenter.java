package com.la.media.VideoProcess;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.la.mymedia.clip.activity.VideoHandleActivity;
import com.la.mymedia.clip.handler.FFmpegHandler;
import com.la.mymedia.clip.util.FFmpegUtil;

import java.io.File;

public class VideoProcessPresenter implements VideoProcess.Presenter {

    private Context context;
    private Activity activity;
    private VideoProcess.View view;

    public VideoProcessPresenter(Context context, Activity activity, VideoProcess.View view){
        this.context =context;
        this.activity = activity;
        this.view = view;
    }


    // 内存泄漏风险
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FFmpegHandler.MSG_BEGIN:
                    view.processBegin();
                    break;
                case FFmpegHandler.MSG_PROGRESS:
                    Log.e("dd", String.valueOf( msg.arg1));
                    view.processContinued(msg.arg1);
                    break;
                case FFmpegHandler.MSG_FINISH:
                    view.processFinished();
                    break;
            }
        }
    };
    private FFmpegHandler fFmpegHandler = new FFmpegHandler(handler);

    @Override
    public void onFunctionItemClick(int position) {
        if (position == 0){
            String srcFile= "/storage/4925-382B/1234.mp4";
            String transformVideo = Environment.getExternalStorageDirectory().getPath() + "/transformVideo.flv";
            String[] commandLines = FFmpegUtil.transformVideo(srcFile, transformVideo);
            fFmpegHandler.executeFFmpegCmd(commandLines);
        }else if (position == 1){
            String srcFile= "/storage/4925-382B/1234.mp4";
            String cutVideo = Environment.getExternalStorageDirectory().getPath() + "/cut.mp4";
            String[] commandLines = FFmpegUtil.cutVideo(srcFile, 0, 100, cutVideo);
            fFmpegHandler.executeFFmpegCmd(commandLines);

        }else if (position == 2){

        }else if (position == 3){
            String srcFile= "/storage/4925-382B/1234.mp4";
            String[] commandLines = FFmpegUtil.screenShot(srcFile, 200, Environment.getExternalStorageDirectory().getPath()+"/ddddg.png");
            fFmpegHandler.executeFFmpegCmd(commandLines);

        }else if (position == 4){
            String srcFile = "/storage/4925-382B/1234.mp4";

            //add watermark to video
            // the unit of bitRate is kb
            int bitRate = 500;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(srcFile);
            String mBitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            if (mBitRate != null && !mBitRate.isEmpty()) {
                int probeBitrate = Integer.valueOf(mBitRate);
                bitRate = (probeBitrate / 1000 / 100) * 100;
            }

            // 视频水印
            String photo = Environment.getExternalStorageDirectory().getPath() + File.separator + "ddddg.png";
            String photoMark = Environment.getExternalStorageDirectory().getPath()+ File.separator + "photoMark.mp4";
            ////1:top left 2:top right 3:bottom left 4:bottom right
            int location = 2;
            int offsetXY = 5;
            String[] commandLines = FFmpegUtil.addWaterMarkImg(context, srcFile, photo, location, bitRate, offsetXY, photoMark);
            fFmpegHandler.executeFFmpegCmd(commandLines);

        }else if (position == 5){

            String Video2Gif = Environment.getExternalStorageDirectory().getPath() + File.separator + "Video2Gif.gif";
            int gifStart = 30;
            int gifDuration = 5;
            String resolution = "720x1280";//240x320、480x640、1080x1920
            int frameRate = 10;

            String srcFile = "/storage/4925-382B/1234.mp4";
            String[] commandLines = FFmpegUtil.generateGif(srcFile, gifStart, gifDuration,
                    resolution, frameRate, Video2Gif);
            fFmpegHandler.executeFFmpegCmd(commandLines);

        }else if (position == 6){
            String srcFile = "/storage/4925-382B/1234.mp4";
            String rVideo = Environment.getExternalStorageDirectory().getPath() + File.separator + "reverse.mp4";
            String[] commandLines = FFmpegUtil.reverseVideo(srcFile, rVideo);
            fFmpegHandler.executeFFmpegCmd(commandLines);
        }else if (position == 7){
            String srcFile = "/storage/4925-382B/1234.mp4";
            String rVideo = Environment.getExternalStorageDirectory().getPath() + File.separator + "denoise.mp4";
            String[] commandLines = FFmpegUtil.denoiseVideo(srcFile, rVideo);
            fFmpegHandler.executeFFmpegCmd(commandLines);
        }else if (position == 8){
            String srcFile = "/storage/4925-382B/1234.mp4";
            String speed = Environment.getExternalStorageDirectory().getPath() + File.separator + "speed.mp4";
            String[] commandLines = FFmpegUtil.changeSpeed(srcFile, speed, 2f, false);
            fFmpegHandler.executeFFmpegCmd(commandLines);
        }else if (position == 9){

        }





    }

    @Override
    public String selectFile() {

        return null;
    }
}
