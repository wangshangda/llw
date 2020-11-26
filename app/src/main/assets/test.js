auto();
var appName="抖音短视频";
launchApp(appName); // 启动抖音短视频
sleep(5000) // 等待5s
while(true) {
    swipe(device.width/2+25,device.height/2,device.width/2-20, 0,100); // 滑屏
    sleep(10000); // 等待10s
}