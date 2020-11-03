package com.banma.hotfix.robust;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppStateHelper {

    public interface OnAppStateListener {
        void onStateChanged(boolean isBackground);
    }

    private OnAppStateListener l;

    ActivityLifecycleCallbacks mLifeCycle = new ActivityLifecycleCallbacks() {

        private int mActivityCount = 0;
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            mActivityCount ++;
            if (mActivityCount == 1) {
                if (l != null) {
                    l.onStateChanged(false);
                }
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            mActivityCount --;
            if (mActivityCount == 0) {
                if (l != null) {
                    l.onStateChanged(true);
                }
            }
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    };

    public void register(Application application, OnAppStateListener listener) {
        this.l = listener;
        application.registerActivityLifecycleCallbacks(mLifeCycle);
    }

    public void unregister(Application application) {
        application.unregisterActivityLifecycleCallbacks(mLifeCycle);
    }
}
