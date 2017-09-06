package com.match.android.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.match.android.R;
import com.match.android.Utils.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class activity_login extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private TextView forget_password;
    private TextView sign;
    private EditText account_edit;
    private EditText password_edit;
    private String account;
    private String passWord;
    private Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        forget_password = (TextView) findViewById(R.id.forget_password);
        sign = (TextView) findViewById(R.id.sign);
        login.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        sign.setOnClickListener(this);

        account_edit = (EditText) findViewById(R.id.login_account);
        password_edit = (EditText) findViewById(R.id.login_password);
        //从本地读取用户信息
        restoreData();

//        download = (Button) findViewById(R.id.download);
//        download.setOnClickListener(this);

        //初始化OSS


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                login();
                break;
            case R.id.forget_password:
                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign:
                Intent intent  = new Intent(this,activity_sign.class);
                startActivity(intent);
                break;
//            case R.id.download:
//                NimUIKit.startP2PSession(NimUIKit.getContext(),"测试");
//
//
//                break;
            default:
                break;
        }
    }

    private void login() {
        account = account_edit.getText().toString();
        passWord = password_edit.getText().toString();
        String address = "http://192.168.1.106:8080/fellow_townsman/land_check/api/user_land.php";
        if (account == null || account.equals("")){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (passWord == null || passWord.equals("")){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //构造post请求信息
        RequestBody requestBody = new FormBody.Builder().add("user_name", account).add("user_password", passWord).build();

        HttpUtil.sendPOSTRequest(address,requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity_login.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String state = response.body().string();
                Log.d("state",state);
                if (state.equals("success")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_login.this, "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    intentMainActivity();
                } else if (state.equals("error")){
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(activity_login.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
                       }
                   });

                    Log.d("state","密码或用户名错误");
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity_login.this, "迷之错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
        editor.putString("account", account);
        editor.putString("password", passWord);
        editor.apply();
    }

    private void restoreData() {
        SharedPreferences sharePer = getSharedPreferences("userData",MODE_PRIVATE);
        String account = sharePer.getString("account", "");
        String password = sharePer.getString("password", "");
        account_edit.setText(account);
        password_edit.setText(password);
    }

    private void intentMainActivity(){
        Intent intent = new Intent(activity_login.this,activity_sign.class);
        intent.putExtra("username",account);
        startActivity(intent);
    }

}