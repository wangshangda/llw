package com.qlm.llw.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.qlm.llw.BuildConfig;
import com.qlm.llw.base.baseControl.ActivityControl;
import com.qlm.llw.service.UmengNotificationService;
import com.qlm.llw.util.FileUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import java.util.HashMap;

/**
 * Created by Administrator on 2019/3/19.
 */

public class BaseApplication extends Application{
    public static BaseApplication mApplication;
    public static boolean IS_DEBUG = BuildConfig.DEBUG ;
    public static boolean isDebug = true ;
    public static String APP_NAME  ;
    private static String TAG="BaseApplication";

    //内存泄露
    //Activity管理
    public ActivityControl mActivityControl;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mApplication=this;

    }


    @Override
    public void onCreate() {
        super.onCreate();
        initUM();
    }

    private void initUM(){
        UMConfigure.init(this, "5fbb48cd509a646ab93b3084", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "e59290ad4138037c86b2856b683aea43");
//获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setPushIntentServiceClass(UmengNotificationService.class);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG,"注册成功：deviceToken：-------->  " + deviceToken);
                SmartToast.show("注册成功：deviceToken：-------->  "+deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG,"注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        mPushAgent.setMessageHandler(messageHandler);

    }


    public static BaseApplication getApplication() {
        return mApplication;
    }

    UmengMessageHandler messageHandler = new UmengMessageHandler(){
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。

                    HashMap<String, String> hm = (HashMap<String, String>) msg.extra;
                    String type = "";
                    String operation = "";
                    String qlmcmd = "";
                    if (hm != null && hm.size() > 0) {
                        if (hm.containsKey("qlmcmd")) {
                            qlmcmd = hm.get("qlmcmd");
                        }
                        if (hm.containsKey("operation")) {
                            operation = hm.get("operation");
                        }
                    }
                    if (!TextUtils.isEmpty("qlmcmd")){
                        FileUtils.writeTxt("qlmcmd",qlmcmd);
                    }
                    if (!TextUtils.isEmpty("operation")){
                        FileUtils.writeTxt("operation",operation);
                    }
                }
            });
        }
    };


}
