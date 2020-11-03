package com.la.mylogin.pager.login;

import android.content.Context;
import android.util.Log;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class LoginPresenter implements Login.Presenter {
    private Login.View loginView;
    private Context context;
    private Login.Model loginModel;
    public LoginPresenter(Context context, Login.View loginView){
        this.loginView = loginView;
        this.context = context;
        loginModel = new LoginModel(context);
    }


    @Override
    public void onLogin(String username, String password) {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("dd", String.valueOf(s.equals("succeeded")));
                Log.e("dd", String.valueOf(s == "succeeded"));

                if (s.equals("succeeded")){
                    loginView.loginSucceeded();
                }
                if (s.equals("failed")){
                    loginView.loginFailed();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        loginModel.login(observer, username, password);
        loginView.logining();
    }

}
