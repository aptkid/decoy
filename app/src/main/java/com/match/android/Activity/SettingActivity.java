package com.match.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.match.android.R;
import com.match.android.Utils.ACache;
import com.match.android.Utils.HttpUtil;
import com.match.android.Utils.OSSToken;
import com.match.android.Utils.UploadFile;
import com.match.android.Utils.UtilFileDB;
import com.match.android.Utils.UtilImags;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by zq on 2016/6/11.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    String URL = "url";
    ZQRoundOvalImageView zqRoundOvalImageView;
    PopupWindow pop;
    LinearLayout ll_popup;
    String urlsf = "";
    int img = 1;
    ACache aCache;
    private String phoneNumber;
    private String filename;
    private Button complete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_setting);
        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        phoneNumber = sharedPreferences.getString("phoneNumber","");
        complete = (Button) findViewById(R.id.complete);
        complete.setOnClickListener(this);
        initView();
    }

    private void initView() {
        Log.d("SettingActivity","initView");
        aCache = ACache.get(SettingActivity.this);
        zqRoundOvalImageView = (ZQRoundOvalImageView) findViewById(R.id.my_setting_txlehs);
        zqRoundOvalImageView.setOnClickListener(this);
        initData();
    }

    private void initData() {
        Log.d("SettingActivity","initData");
        if (UtilFileDB.SELETEFile(aCache, "stscimage") != null) {
            if (aCache.getAsBitmap("myimg") == null) {
                getImage(UtilFileDB.LOGINIMGURL(aCache));
            } else {
                zqRoundOvalImageView.setImageBitmap(aCache.getAsBitmap("myimg"));
            }
        }
    }

    public void getImage(String url) {
        Log.d("SettingActivity","getImage");
        OkHttpUtils.get().url(url).tag(this).build().connTimeOut(20000)
                .readTimeOut(20000).writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SettingActivity","getImage_Error");
                    }
                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.d("SettingActivity","getImage_Success");
                        zqRoundOvalImageView.setImageBitmap(bitmap);
                        aCache.put("myimg", bitmap);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Log.d("SettingActivity","onClick");
        switch (v.getId()) {
            case R.id.my_setting_txlehs:
                showPopupWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        SettingActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.complete:
                final SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestBody requestBody = new FormBody.Builder().add("user_id",sharedPreferences.getString("phoneNumber",""))
                                .add("user_name",sharedPreferences.getString("username",""))
                                .add("user_sex",sharedPreferences.getString("sex",""))
                                .add("user_town_prov",sharedPreferences.getString("hometown_ProvinceName",""))
                                .add("user_town_city",sharedPreferences.getString("hometown_CityName",""))
                                .add("user_town_area",sharedPreferences.getString("hometown_DistrictName",""))
                                .add("user_now_prov",sharedPreferences.getString("Province",""))
                                .add("user_now_city",sharedPreferences.getString("City",""))
                                .add("user_now_area",sharedPreferences.getString("hometown_DistrictName",""))
                                .add("user_long",sharedPreferences.getString("longitude",""))
                                .add("user_lat",sharedPreferences.getString("latitude",""))
                                .add("user_head","http://oss.systemsec.top/"+phoneNumber+"/headImage.jpg")
                                .add("register_1","2").build();
                        HttpUtil.sendPOSTRequest("http://www.9sec.top/fellow_townsman/land_check/api/user_register.php", requestBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {


                            }
                        });
                    }
                }).start();
                Log.d("Shared","手机号;"+sharedPreferences.getString("phoneNumber","")+"\n用户名："+sharedPreferences.getString("username","")+"\n性别："+sharedPreferences.getString("sex","")
                        +"\n家乡地址  省："+sharedPreferences.getString("hometown_ProvinceName","") +" 市："+sharedPreferences.getString("hometown_CityName","")+" 区："+sharedPreferences.getString("hometown_DistrictName","")
                        +"\n当前地址  省："+sharedPreferences.getString("Province","")+" 市："+sharedPreferences.getString("City","")+" 区："+sharedPreferences.getString("District","")
                        +"经度："+sharedPreferences.getString("longitude","")+"纬度："+sharedPreferences.getString("latitude",""));
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /****
     * 头像提示框
     */
    public void showPopupWindow() {
        Log.d("SettingActivity","showPopupWindow");
        pop = new PopupWindow(SettingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 1);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent picture = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, 2);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("SettingActivity","onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK
                && null != data) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                return;
            }
            new DateFormat();
            String name = "headImage.jpg";
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bmp = (Bitmap) bundle.get("data");
            FileOutputStream fout = null;
            filename = null;
            try {
                filename = UtilImags.SHOWFILEURL(SettingActivity.this) + "/" + name;
            } catch (IOException e) {
            }
            try {
                fout = new FileOutputStream(filename);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                //上传头像
                String endpoint = "http://oss.systemsec.top";
                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(OSSToken.getId(),OSSToken.getSecret(),OSSToken.getToken());
                OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
                UploadFile uploadFile = new UploadFile(oss,"huadong2oss",phoneNumber+"/headImage.jpg", filename);
                uploadFile.uploadData(fout);
            } catch (FileNotFoundException e) {
                showToastShort("上传失败");
            } finally {
                try {
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    showToastShort("上传失败");
                }
            }
            //上传头像
            String endpoint = "http://oss.systemsec.top";
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(OSSToken.getId(),OSSToken.getSecret(),OSSToken.getToken());
            OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
            UploadFile uploadFile = new UploadFile(oss,"huadong2oss",phoneNumber+"/headImage.jpg", filename);
            uploadFile.resumableUpload();
            zqRoundOvalImageView.setImageBitmap(bmp);
            staffFileupload(new File(filename));
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectedImage,
                        filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Bitmap bmp = BitmapFactory.decodeFile(picturePath);
                // 获取图片并显示
                zqRoundOvalImageView.setImageBitmap(bmp);
                saveBitmapFile(UtilImags.compressScale(bmp), UtilImags.SHOWFILEURL(SettingActivity.this) + "/headImage.jpg");
//                staffFileupload(new File(UtilImags.SHOWFILEURL(SettingActivity.this) + "/headImage.jpg"));

                //上传头像
                String endpoint = "http://oss.systemsec.top";
                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(OSSToken.getId(),OSSToken.getSecret(),OSSToken.getToken());
                OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
                UploadFile uploadFile = new UploadFile(oss,"huadong2oss",phoneNumber+"/headImage.jpg",UtilImags.SHOWFILEURL(SettingActivity.this) + "/headImage.jpg");
                uploadFile.resumableUpload();

            } catch (Exception e) {
                showToastShort("上传失败");
            }
        }
    }

    public void saveBitmapFile(Bitmap bitmap, String path) {
        Log.d("SettingActivity","saveBitmapFile");
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void staffFileupload(File file) {
        Log.d("SettingActivity","staffFileupload");
        if (false) {
            showToastShort("网络未连接");
            return;
        }
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, URL, MYUPDATEIMG(file),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Log.d("SettingActivity","staffFileupload_onFailure");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        Log.d("SettingActivity","staffFileupload_success");
                        JSONObject jsonobj;
                        try {
                            jsonobj = new JSONObject(arg0.result.toString());
                            String errno = jsonobj.getString("errno");
                            String error = jsonobj.getString("error");
                            if (errno.equals("0") && error.equals("success")) {

                                JSONArray jsonarray = jsonobj.getJSONArray("data");
                                JSONObject jsonobjq = jsonarray.getJSONObject(0);
                                urlsf = jsonobjq.getString("url");
                                UtilFileDB.ADDFile(aCache, "stscimage", urlsf);
                                img = 3;
                                showToastShort("头像修改成功");

                            } else {
                                showToastShort("头像修改失败");
                            }
                        } catch (JSONException e) {
                            return;
                        }
                    }
                });
    }

    /***
     * 修改头像
     *
     * @return
     */
    public static final RequestParams MYUPDATEIMG(File file) {
        Log.d("SettingActivity","MyUpdateImg");
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "profile");
        params.addBodyParameter("a", "setavatar");
        params.addBodyParameter("uid", "");
        params.addBodyParameter("username", "");
        if (file != null) {
            params.addBodyParameter("filedata", file);
        }
        return params;
    }

    private void showToastShort(String string) {
        Toast.makeText(SettingActivity.this, string, Toast.LENGTH_LONG).show();
    }

}
