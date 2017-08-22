package com.match.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.match.android.R;
import com.zaaach.citypicker.CityPickerActivity;

public class activity_sign_city extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_CITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_city);
        //启动
        startActivityForResult(new Intent(activity_sign_city.this, CityPickerActivity.class), REQUEST_CODE_PICK_CITY);
    }
    //重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
//                resultTV.setText("当前选择：" + city);
                Toast.makeText(this, "当前选择" + city, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
