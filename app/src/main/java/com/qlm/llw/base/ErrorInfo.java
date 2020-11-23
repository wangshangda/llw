package com.qlm.llw.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.coder.zzq.smartshow.toast.SmartToast;
import com.google.gson.JsonSyntaxException;
import com.qlm.llw.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import rxhttp.wrapper.exception.HttpStatusCodeException;
import rxhttp.wrapper.exception.ParseException;

/**
 * Created by Administrator on 2020/5/20.
 */

public class ErrorInfo {

    private int errorCode;  //仅指服务器返回的错误码
    private String errorMsg; //错误文案，网络错误、请求失败错误、服务器返回的错误等文案
    private Throwable throwable; //异常信息

    public ErrorInfo(Throwable throwable) {
        this.throwable = throwable;
        String errorMsg =handleNetworkException(throwable); //网络异常
        if (throwable instanceof HttpStatusCodeException) { //请求失败异常
            String code = throwable.getLocalizedMessage();
            if ("416".equals(code)) {
                errorMsg = "请求范围不符合要求";
            }
        } else if (throwable instanceof JsonSyntaxException) { //请求成功，但Json语法异常,导致解析失败
            errorMsg = "数据解析失败,请稍后再试";
        } else if (throwable instanceof ParseException) { // ParseException异常表明请求成功，但是数据不正确
            String errorCode = throwable.getLocalizedMessage();
            this.errorCode = Integer.valueOf(errorCode);
            errorMsg = throwable.getMessage();
            if (TextUtils.isEmpty(errorMsg)) errorMsg = errorCode;//errorMsg为空，显示errorCode
        }
        this.errorMsg = errorMsg;
    }


    //处理网络异常
    public static <T> String handleNetworkException(T throwable) {
        int stringId = -1;
        if (throwable instanceof UnknownHostException) {
            if (!isNetworkConnected(BaseApplication.getApplication())) {
                stringId = R.string.network_error;
            } else {
                stringId = R.string.notify_no_network;
            }
        } else if (throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
            //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
            stringId = R.string.time_out_please_try_again_later;
        } else if (throwable instanceof ConnectException) {
            stringId = R.string.esky_service_exception;
        }
        return stringId == -1 ? null : BaseApplication.getApplication().getString(stringId);
    }

    @SuppressWarnings("deprecation")
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public boolean show() {
        SmartToast.show(TextUtils.isEmpty(errorMsg) ? throwable.getMessage() : errorMsg);
        return true;
    }

    /**
     * @param standbyMsg 备用的提示文案
     */
    public boolean show(String standbyMsg) {
        SmartToast.show(TextUtils.isEmpty(errorMsg) ? standbyMsg : errorMsg);
        return true;
    }

    /**
     * @param standbyMsg 备用的提示文案
     */
    public boolean show(int standbyMsg) {
        SmartToast.show(TextUtils.isEmpty(errorMsg) ? BaseApplication.getApplication().getString(standbyMsg) : errorMsg);
        return true;
    }
}
