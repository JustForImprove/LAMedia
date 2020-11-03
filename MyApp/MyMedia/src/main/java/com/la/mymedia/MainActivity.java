package com.la.mymedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.la.mymedia.clip.activity.ClipEnterActivity;
import com.la.mymedia.live.activity.LiveActivity;
import com.la.mymedia.live.activity.LiveEnterActivity;
import com.la.mymedia.player.activity.PlayerEnterActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_activity);
        Listener l = new Listener();
        findViewById(R.id.btn_clip_enter).setOnClickListener(l);
        findViewById(R.id.btn_live_enter).setOnClickListener(l);
        findViewById(R.id.btn_player_enter).setOnClickListener(l);


    }

    private class Listener implements View.OnClickListener{


        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            int id = v.getId();
            if (id == R.id.btn_clip_enter) {
                intent.setClass(MainActivity.this, ClipEnterActivity.class);
            } else if (id == R.id.btn_live_enter) {
                intent.setClass(MainActivity.this, LiveEnterActivity.class);
            } else if (id == R.id.btn_player_enter) {
                intent.setClass(MainActivity.this, PlayerEnterActivity.class);
            }
            startActivity(intent);
        }
    }
}
