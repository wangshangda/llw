package com.qlm.llw.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Administrator on 2020/11/25.
 */

public class ClickService extends AccessibilityService {
    private final String TAG = getClass().getName();

    public static ClickService mService;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onInterrupt() {
        mService = null;
    }

    //初始化
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mService = null;
    }

    public static boolean isStart() {
        return mService != null;
    }

    public static void clickBack(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
           boolean ff=mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
           String s="";
        }
    };
}
