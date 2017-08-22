package com.match.android;

import android.app.Application;
import android.util.Log;

import com.match.android.Utils.HttpUtil;
import com.match.android.Utils.OSSToken;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Brant on 2017/8/21 20:10.
 */

public class initApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "9da27d3d6b", false);
        initOSS();
    }
    public void initOSS(){
        HttpUtil.sendGETRequest("http://192.168.0.7/OSS/demo.php", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String[] OSSAccess = response.body().string().split(",");
                OSSToken ossToken = new OSSToken();
                Log.d("SettingActivity",OSSAccess[0]+"\n"+OSSAccess[1]+"\n"+OSSAccess[2]);
                ossToken.setId(OSSAccess[0]);
                ossToken.setSecret(OSSAccess[1]);
                ossToken.setToken(OSSAccess[2]);
            }
        });
    }
}
