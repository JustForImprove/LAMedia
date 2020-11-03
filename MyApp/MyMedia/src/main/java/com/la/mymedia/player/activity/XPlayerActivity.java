package com.la.mymedia.player.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.la.mymedia.R;
import com.la.mymedia.player.XPlay;

public class XPlayerActivity extends AppCompatActivity implements Runnable, SeekBar.OnSeekBarChangeListener  {


    private Button bt;
    private SeekBar seek;
    private Thread th;
    private XPlay xPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        //去掉标题栏
        supportRequestWindowFeature( Window.FEATURE_NO_TITLE);
        //全屏，隐藏状态
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //屏幕为横屏
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );

        setContentView( R.layout.xplayer_activity);
        bt = findViewById( R.id.open_button );
        seek = findViewById( R.id.aplayseek );
        xPlay = findViewById(R.id.x_play);
        seek.setMax(1000);
        seek.setOnSeekBarChangeListener( this );

        bt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xPlay.Open("/storage/4925-382B/1234.mp4");
            }
        } );
        //启动播放进度线程
        th = new Thread(this);
        th.start();
    }

    //播放进度显示
    @Override
    public void run() {
        for(;;)
        {
            seek.setProgress((int)(xPlay.PlayPos()*1000));
            try {
                Thread.sleep( 40 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        xPlay.Seek( (double)seekBar.getProgress()/(double)seekBar.getMax() );
    }}
