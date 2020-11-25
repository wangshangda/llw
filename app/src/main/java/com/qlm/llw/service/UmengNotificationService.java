package com.qlm.llw.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.qlm.llw.bean.BaseBody;
import com.qlm.llw.bean.OperationBean;
import com.qlm.llw.util.AudioUtil;
import com.qlm.llw.util.FileUtils;
import com.umeng.message.UmengMessageService;

import org.android.agoo.common.AgooConstants;

/**
 * Created by c4542 on 2020/11/22.
 */

public class UmengNotificationService extends UmengMessageService {
    protected int mGestureDownVolume;
    protected AudioManager mAudioManager;


    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Gson gson = new Gson();
        BaseBody baseBody = gson.fromJson(message, BaseBody.class);
        OperationBean op = baseBody.getExtra();
        String type = op.getType();
        String operation = op.getOperation();
        String qlmcmd = op.getQlmcmd();


        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                if (!TextUtils.isEmpty(op.getOperation())) {
                    FileUtils.writeTxt("operation", operation);
                }
                if (!TextUtils.isEmpty(op.getQlmcmd())) {
                    FileUtils.writeTxt("qlmcmd", qlmcmd);
                }

                mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int deltaV = (int) (max / 10);


                switch (type) {
                    case "1":
                        if (!ClickService.isStart()) {
                            try {
                                Intent intenet= new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                intenet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intenet);
                            } catch (Exception e) {
                                Intent intenet=new Intent(Settings.ACTION_SETTINGS);
                                intenet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intenet);
                                e.printStackTrace();
                            }
                        }
                        ClickService.clickBack();
                        break;
                    case "2":
                        AudioUtil.getInstance(context).setMediaVolume(mGestureDownVolume+deltaV);
//                        DeviceUtils.checkScreenOn(context);
                        break;
                    case "3":
                        if (mGestureDownVolume>deltaV)
                        AudioUtil.getInstance(context).setMediaVolume(mGestureDownVolume-deltaV);
//                        DeviceUtils.checkScreenOff(context);
                        break;
                    default:
                        break;
                }

            }
        });
    }


}
