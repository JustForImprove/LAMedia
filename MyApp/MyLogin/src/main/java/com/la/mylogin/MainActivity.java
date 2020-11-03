package com.la.mylogin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.la.mylogin.pager.login.Login;
import com.la.mylogin.pager.login.LoginFragment;

public class MainActivity extends AppCompatActivity {
private FrameLayout fragmentFrame;


    Login.LoginCallback loginCallback = new Login.LoginCallback() {
        @Override
        public void loginSucceeded(Bundle bundle) {
            // 跳转逻辑

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        fragmentFrame = findViewById(R.id.login_fragment_frame);
        LoginFragment myFragment = new LoginFragment(loginCallback);
        setFragment(myFragment);
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.login_fragment_frame,fragment);
        fragmentTransaction.commit();

    }
}