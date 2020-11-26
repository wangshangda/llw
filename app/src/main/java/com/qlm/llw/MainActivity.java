package com.qlm.llw;

import android.Manifest;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.qlm.llw.service.ClickService;
import com.qlm.llw.service.ScreenOffAdminReceiver;
import com.qlm.llw.util.AudioUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    protected int mGestureDownVolume;
    protected AudioManager mAudioManager;

    @BindView(R.id.btn_open)
    Button btnOpen;
    @BindView(R.id.webview)
    WebView webview;

    private DevicePolicyManager policyManager;
    private ComponentName adminReceiver;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private int deltaV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }


        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName("com.manlian.garden.interestgarden", "com.manlian.garden.interestgarden.ui.story.AudioPlayActivity");
                intent.setComponent(cn);
                startActivity(intent);
            }
        });

        adminReceiver = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        policyManager = (DevicePolicyManager) MainActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        checkAndTurnOnDeviceManager(null);

        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);


        initService();
        initWebView();
    }

    private void initService() {
        Intent intenetClick = new Intent(this, ClickService.class);
        startService(intenetClick);

        if (!ClickService.isStart()) {
            try {
                Intent intenet = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intenet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intenet);
            } catch (Exception e) {
                Intent intenet = new Intent(Settings.ACTION_SETTINGS);
                intenet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intenet);
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    checkScreenOn(null);
                    break;
                case 2:

                    break;
            }
        }
    };


    /**
     * @param view 检测屏幕状态
     */
    public void checkScreen(View view) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {//如果灭屏
            //相关操作
            showToast("屏幕是息屏");
        } else {
            showToast("屏幕是亮屏");

        }
    }


    /**
     * @param view 亮屏
     */
    public void checkScreenOn(View view) {
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }

    /**
     * @param view 熄屏
     */
    public void checkScreenOff(View view) {
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            showToast("没有设备管理权限");
        }
    }

    public void voiceOff(View view) {
        mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        deltaV = (int) (max / 10);
        AudioUtil.getInstance(this).setMediaVolume(mGestureDownVolume - deltaV);
    }

    public void voiceOn(View view) {
        mGestureDownVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        deltaV = (int) (max / 10);
        AudioUtil.getInstance(this).setMediaVolume(mGestureDownVolume + deltaV);
    }

    public void voiceClear(View view) {
        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }


    /**
     * @param view 熄屏并延时亮屏
     */
    public void checkScreenOffAndDelayOn(View view) {
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
            handler.sendEmptyMessageDelayed(1, 3000);
        } else {
            showToast("没有设备管理权限");
        }
    }

    /**
     * @param view 检测并去激活设备管理器权限
     */
    public void checkAndTurnOnDeviceManager(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...");//显示位置见图二
        startActivityForResult(intent, 0);
    }


    /**
     * 初始化webview
     */
    protected void initWebView() {
        webview = new WebView(this);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new JsToJava(), "stub");  //JsToJava是内部类，代码在后面。stub是接口名字。
        webview.loadUrl("file:///android_asset/test.js"); //js文件路径
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                String urlinit = "javascript:footballHelper.Es.jclq.setContent()";
//                mWebView.loadUrl(urlinit);
            }
        });
    }
    /**
     * js方法回调
     */
    private class JsToJava {
        @JavascriptInterface
        public void jsCallbackMethod(String result) {
            Log.e("444", "result==" + result);
        }
    }






    private void showToast(String Str) {
        Toast.makeText(this, Str, Toast.LENGTH_SHORT).show();
    }


//    public void openAccessibilityService(){
//        try{
//	/* Get a list of all available accessibility services */
//            Set<ComponentName> enabledServices = getEnabledServices(this);
//
//	/* If the obtained service collection is empty, create a new collection */
//            if (enabledServices == (Set<?>) Collections.emptySet()) {
//                enabledServices = new HashSet<ComponentName>();
//            }
//
//            // 将需要设置的服务到集合里
//            ComponentName toggledService = ComponentName.unflattenFromString("com.qlm.llw/MainActivity");//添加自己服务的包名和类名
//            enabledServices.add(toggledService);
//
//            StringBuilder enabledServicesBuilder = new StringBuilder();
//            // 将所有的服务变成字符串,串到一起
//            for (ComponentName enabledService : enabledServices) {
//                enabledServicesBuilder.append(enabledService.flattenToString());
//                //每次添加一个服务的时候后面加上分隔符
//                enabledServicesBuilder.append(':');
//            }
//
//            final int enabledServicesBuilderLength = enabledServicesBuilder.length();
//            if (enabledServicesBuilderLength > 0) {
//                // 添加最后一个服务肯定会多出一个分隔符,这里删掉多余的分隔符
//                enabledServicesBuilder.deleteCharAt(enabledServicesBuilderLength - 1);
//            }
//            // 将服务的字符串重新存储
//            android.provider.Settings.Secure.putString(this.getContentResolver(),
//                    android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, enabledServicesBuilder.toString());
//
//            // Update accessibility enabled.
//            android.provider.Settings.Secure.putInt(this.getContentResolver(),
//                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED, 1);
//        } catch(Exception e) {};
//
//    }
//
//
//    public Set<ComponentName> getEnabledServices(Context context) {
//        final String enabledServicesSetting = Settings.Secure.getString(
//                context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//
//        if (enabledServicesSetting == null) {
//            return Collections.emptySet();
//        }
//
//        final Set<ComponentName> enabledServices = new HashSet<>();
//        final TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
//        colonSplitter.setString(enabledServicesSetting);
//
//        while (colonSplitter.hasNext()) {
//            final String componentNameString = colonSplitter.next();
//            final ComponentName enabledService = ComponentName.unflattenFromString(
//                    componentNameString);
//            if (enabledService != null) {
//                enabledServices.add(enabledService);
//            }
//        }
//        return enabledServices;
//    }


}
