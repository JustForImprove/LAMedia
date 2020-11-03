package com.la.mylogin.pager.login;

import android.content.Context;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;

public class LoginModel implements Login.Model {
private Context context;
    public LoginModel(Context context){
        this.context = context;
    }

    @Override
    public void login(Observer<String> observer, String username, String password) {

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                // 进行网络请求
                emitter.onNext("succeeded");
            }
        });
        observable.subscribe(observer);
    }

}
