package com.briang.lasergame;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private IBinder mBinder = new SocketServerBinder();
    private Timer mTimer;
    private boolean mRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (mRunning) {

                }
            }
        }, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        mRunning = true;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mRunning = false;
        return super.onUnbind(intent);
    }

    public class SocketServerBinder extends Binder {

        public MyService getService() {
            return MyService.this;
        }

    }

}
