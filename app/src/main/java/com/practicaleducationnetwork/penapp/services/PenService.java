package com.practicaleducationnetwork.penapp.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.practicaleducationnetwork.penapp.models.Tutor;

public class PenService extends Service {

    public Tutor tutor;
    private IBinder myBinder = new MyBinder();
    private Activity activity;

    public PenService() {
        tutor = new Tutor();
    }

//    public PenService(Tutor tutor) {
//        this.tutor = tutor;
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder{
        public PenService getService(){return PenService.this;}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        tutor = new Tutor();
        return START_STICKY;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public Tutor getTutor(){return tutor;}
}
