package com.la.myapp.view;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.la.myapp.R;
import com.la.myapp.view.main.Main;
import com.la.myapp.view.main.MainFragment;
import com.la.mylogin.pager.login.Login;
import com.la.mylogin.pager.login.LoginFragment;

import java.util.Stack;



public class MainActivity extends AppCompatActivity{

    private Stack<Fragment> fragmentsStack;
    private Main.MainCallback mainCallBack = new Main.MainCallback() {
        // 主页之外的跳转逻辑

    };

    private Login.LoginCallback loginCallback = new Login.LoginCallback() {
        @Override
        public void loginSucceeded(Bundle bundle) {
            MainFragment mainFragment =  new MainFragment(mainCallBack);
            fragmentsStack.pop();
            setFragment(mainFragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentsStack = new Stack<>();
        //MainFragment mainFragment = new MainFragment();
        LoginFragment loginFragment = new LoginFragment(loginCallback);
        setFragment(loginFragment);
    }


     public void setFragment(Fragment fragment) {
        fragmentsStack.push(fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public void backLastPager(){
        fragmentsStack.pop();
        Fragment fragment = fragmentsStack.peek();
        setFragment(fragment);
    }

    // 页面回退机制，采用栈实现
    @Override
    public void onBackPressed() {
        backLastPager();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

}