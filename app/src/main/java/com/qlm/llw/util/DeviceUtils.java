package com.qlm.llw.util;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.qlm.llw.service.ScreenOffAdminReceiver;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by Administrator on 2020/11/23.
 */

public class DeviceUtils {


    public static void wakeUpAndUnlock(Context context){
        //屏锁管理器
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    /**
     *
     */
    public static void checkScreenOn(Context context) {
        PowerManager    mPowerManager = (PowerManager)context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }

    /**
     *
     */
    public static void checkScreenOff(Context context) {
        ComponentName   adminReceiver = new ComponentName(context, ScreenOffAdminReceiver.class);
        DevicePolicyManager   policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        PowerManager   mPowerManager = (PowerManager)context.getSystemService(POWER_SERVICE);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            SmartToast.show("没有设备管理权限");
        }
    }


}
