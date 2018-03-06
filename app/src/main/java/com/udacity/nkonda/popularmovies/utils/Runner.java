package com.udacity.nkonda.popularmovies.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Runner {
    private static Runner instance;
    private final Executor sExecutor = Executors.newFixedThreadPool(4);

    private Runner() {
    }

    public static Runner getInstance() {
        if (instance == null) {
            instance = new Runner();
        }
        return instance;
    }

    public void runOnBackgroundThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    public void runOnUiThread(Runnable runnable) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
