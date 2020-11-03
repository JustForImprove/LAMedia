package com.la.media.Player;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.la.mymedia.R;
import com.la.mymedia.player.XPlay;

public class PlayerFragment extends Fragment implements Player.View{
private Player.PlayerCallBack playerCallBack;
private Context context;
private Player.Presenter presenter;
private View view;
private XPlay xPlay;
private ImageButton back;
private ImageButton lock_or_unlock;
private ImageButton play_or_pause;
private SeekBar seekBar;
private ConstraintLayout floatingLayout;



private RecyclerView recyclerView;

    public PlayerFragment(Player.PlayerCallBack playerCallBack){
        this.playerCallBack = playerCallBack;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

        //  确认单击
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            show_or_hide_Screen();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // 0 2 2 1 四个事件
            if (e.getAction() == MotionEvent.ACTION_UP){
                play_or_pause();
            }
            return true;
        }
    });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.player_fragement, null);
        xPlay = view.findViewById(R.id.player_x_play);
        back = view.findViewById(R.id.player_back);
        lock_or_unlock = view.findViewById(R.id.player_lock_or_unlock_screen);
        play_or_pause = view.findViewById(R.id.player_btn_play_or_pause);
        seekBar = view.findViewById(R.id.player_seek);
        floatingLayout = view.findViewById(R.id.player_floating);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;)
                {
                    // 0-1
                    seekBar.setProgress((int)(xPlay.PlayPos()*100));
                    try {
                        Thread.sleep( 400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerCallBack.back();
            }
        });

        lock_or_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock_or_unlock_Screen();
            }
        });

        play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_or_pause();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 由于setProgress操作异步执行，会导致seek过程中，progress又跳回去，解决方法是
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                xPlay.Seek( (double)seekBar.getProgress()/(double)seekBar.getMax() );
            }
        });


        xPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


        return view;
    }


    @Override
    public void lock_or_unlock_Screen() {
        if (floatingLayout.getVisibility() == View.VISIBLE){
            floatingLayout.setVisibility(View.GONE);
        }else if (floatingLayout.getVisibility() == View.GONE){
            floatingLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show_or_hide_Screen() {
        if (lock_or_unlock.getVisibility() == View.VISIBLE){
            lock_or_unlock.setVisibility(View.GONE);
            floatingLayout.setVisibility(View.GONE);
        }else if (lock_or_unlock.getVisibility() == view.GONE){
            lock_or_unlock.setVisibility(View.VISIBLE);
            floatingLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPlayUrl(String url) {
        xPlay.setPlayUrl(url);
    }

    @Override
    public void play_or_pause() {
        xPlay.PlayOrPause();
    }



}
