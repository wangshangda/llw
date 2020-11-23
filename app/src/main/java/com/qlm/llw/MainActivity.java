package com.qlm.llw;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btn_open)
    Button btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,123);
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
