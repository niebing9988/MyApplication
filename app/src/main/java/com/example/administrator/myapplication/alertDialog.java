package com.example.administrator.myapplication;


import android.support.v7.app.AlertDialog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class alertDialog {
    private AlertDialog dialog;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public alertDialog(AlertDialog dialog){
        this.dialog = dialog;
    }

    public void show(long duration){
        //创建自动关闭任务
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        };
        //新建调度任务
        executor.schedule(runner, duration, TimeUnit.MILLISECONDS);
        dialog.show();
    }
}
