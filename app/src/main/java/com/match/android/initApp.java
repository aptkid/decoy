package com.match.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.match.android.Utils.HttpUtil;
import com.match.android.Utils.OSSToken;
import com.match.android.Utils.SystemUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Brant on 2017/8/21 20:10.
 */

public class initApp extends Application{

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.d("amap",aMapLocation.getProvince()+aMapLocation.getCity()+aMapLocation.getDistrict());
            SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
            editor.putString("Province",aMapLocation.getProvince());
            editor.putString("City",aMapLocation.getCity());
            editor.putString("District",aMapLocation.getDistrict());
            editor.putString("longitude",aMapLocation.getLongitude()+"");
            editor.putString("latitude",aMapLocation.getLatitude()+"");
            editor.apply();
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "9da27d3d6b", false);
        initOSS();

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setMockEnable(false);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }
    public void initOSS(){
        HttpUtil.sendGETRequest("http://php.systemsec.top/", new Callback() {
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




    private void initUiKit() {

    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }
}
