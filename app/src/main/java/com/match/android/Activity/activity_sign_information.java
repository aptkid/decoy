package com.match.android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.match.android.R;
import com.match.android.Utils.HttpUtil;
import com.match.android.wheel.OnWheelChangedListener;
import com.match.android.wheel.WheelView;
import com.match.android.wheel.adapters.ArrayWheelAdapter;
import com.zaaach.citypicker.CityPickerActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class activity_sign_information extends BaseActivity implements OnWheelChangedListener {

    private EditText post_username;
    private EditText post_password;
    private Button post_user_information;
    private String username;
    private String address;
    private boolean man;
    private boolean woman;
    private String sex;
    private Button nextStep;

    //选择家乡
    private static final int REQUEST_CODE_PICK_CITY = 0;
    private ImageButton hometown;
    private String city_hometown;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView hometownLocation;
    private CheckBox cb_man;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_information);
        //初始化地址
        setUpViews();
        setUpListener();
        setUpData();
        cb_man = (CheckBox) findViewById(R.id.check_man);
        post_username = (EditText) findViewById(R.id.post_user_username);
        hometownLocation = (TextView) findViewById(R.id.hometownLocation);
//        hometown = (ImageButton) findViewById(R.id.hometown);
//        hometown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //启动
//                startActivityForResult(new Intent(activity_sign_information.this, CityPickerActivity.class), REQUEST_CODE_PICK_CITY);
//            }
//        });
        nextStep = (Button) findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man = cb_man.isChecked();
                if (man){
                    sex = "男";
                } else {
                    sex = "女";
                }
                SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
                editor.putString("username",post_username.getText().toString());
                editor.putString("sex",sex);
                editor.putString("hometown_ProvinceName",mCurrentProviceName);
                editor.putString("hometown_CityName",mCurrentCityName);
                editor.putString("hometown_DistrictName",mCurrentDistrictName);
                editor.apply();
                Intent intent = new Intent(activity_sign_information.this,SettingActivity.class);

//                startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
    }

    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                city_hometown = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                Toast.makeText(this, "当前选择" + city_hometown, Toast.LENGTH_SHORT).show();
            }
        }
    }



    //选择家乡城市
    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(activity_sign_information.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
            hometownLocation.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
        } else if (wheel == mViewCity) {
            updateAreas();
            hometownLocation.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
        } else if (wheel == mViewDistrict) {
            hometownLocation.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    private void post_user_infor() {
        //保存用户输入的数据
        SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
        editor.putString("username",username);
        editor.putString("sex",sex);
        editor.putString("hometown_ProvinceName",mCurrentProviceName);
        editor.putString("hometown_CityName",mCurrentCityName);
        editor.putString("hometown_DistrictName",mCurrentDistrictName);
        editor.apply();
        RequestBody requestBody = new FormBody.Builder().add("user_name", username).add("register_1", "2").build();
        HttpUtil.sendPOSTRequest(address, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity_sign_information.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String state = response.body().string();
                if (state.equals("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_sign_information.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(activity_sign_information.this, activity_login.class);
                    startActivity(intent);
                } else if (state.equals("error")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_sign_information.this, "注册失败，检查服务器", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_sign_information.this, "迷之错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }

}


