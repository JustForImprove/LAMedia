package com.la.mymedia.customview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.la.mymedia.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Listener listener = new Listener();
        findViewById(R.id.smart_camera).setOnClickListener(listener);
        findViewById(R.id.smart_recycler).setOnClickListener(listener);

    }

    class Listener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            int id = v.getId();
            if (id == R.id.smart_camera) {
                intent.setClass(MainActivity.this, SmartCameraTestActivity.class);
            } else if (id == R.id.smart_recycler) {
                intent.setClass(MainActivity.this, LayoutManagerTestActivity.class);
            }
            startActivity(intent);
        }
    }
}
