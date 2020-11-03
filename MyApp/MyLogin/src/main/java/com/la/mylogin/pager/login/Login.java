package com.la.mylogin.pager.login;

import android.os.Bundle;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public interface Login {
    interface LoginCallback{
        void loginSucceeded(Bundle bundle);

    }

    interface View{
        // 登录动作
        void logining();
        void loginSucceeded();
        void loginFailed();

    }

    interface Presenter{
        void onLogin(String username, String password);
    }

    interface Model{
        void login(Observer<String> observer, String username, String password);
    }


}
