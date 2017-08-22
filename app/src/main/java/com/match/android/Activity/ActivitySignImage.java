package com.match.android.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.match.android.R;
import com.match.android.Utils.ACache;
import com.match.android.Utils.UtilFileDB;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;

public class ActivitySignImage extends AppCompatActivity implements View.OnClickListener{

    ZQRoundOvalImageView zqRoundOvalImageView;
    ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_image);
        zqRoundOvalImageView = (ZQRoundOvalImageView) findViewById(R.id.my_sign_sub_img);
        zqRoundOvalImageView.setOnClickListener(this);
        findViewById(R.id.my_sign_sub_txt).setOnClickListener(this);
        aCache = ACache.get(ActivitySignImage.this);
        initData();

    }

    private void initData() {
        if (UtilFileDB.SELETEFile(aCache, "stscimage") != null) {
            if (aCache.getAsBitmap("myimg") == null) {
                getImage(UtilFileDB.LOGINIMGURL(aCache));
            } else {
                zqRoundOvalImageView.setImageBitmap(aCache.getAsBitmap("myimg"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_sign_sub_img:
            case R.id.my_sign_sub_txt:
                Intent intents = new Intent(ActivitySignImage.this, SettingActivity.class);
                Log.d("TAG","3");
//                startActivity(intents);
                startActivityForResult(intents, 1);
                Log.d("TAG","4");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 3) {
                getImage(data.getStringExtra("urlimg"));
            } else {
                zqRoundOvalImageView.setImageResource(R.mipmap.zhangwo_hometop1);
            }
        }
    }

    public void getImage(String url) {
        UtilFileDB.DELETEFile(aCache, "myimg");
        OkHttpUtils.get().url(url).tag(this).build().connTimeOut(20000)
                .readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        zqRoundOvalImageView.setImageBitmap(bitmap);
                        aCache.put("myimg", bitmap);
                    }
                });
    }
}
