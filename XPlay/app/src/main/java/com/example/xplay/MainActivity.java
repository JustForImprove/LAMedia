package com.example.xplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File[] files = getExternalFilesDirs(Environment.MEDIA_MOUNTED);
        setContentView(R.layout.activity_main);
        Log.e("mainActivity","ddddddddddddddd");
        //Open("/storage/4925-382B/a.mp4");
    }

}
