package com.match.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.match.android.R;
import com.match.android.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class activity_sign_information extends AppCompatActivity {

    private EditText post_username;
    private EditText post_password;
    private Button post_user_information;
    private String username;
    private String password;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_information);
        address = "http://192.168.1.106:8080/fellow_townsman/land_check/api/user_register.php";
        post_username = (EditText) findViewById(R.id.post_user_username);
        post_password = (EditText) findViewById(R.id.post_user_password);
        post_user_information = (Button) findViewById(R.id.post_user_information);
        post_user_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((post_username != null || post_username.equals("")) && (post_password != null || post_password.equals(""))) {
                    username = post_username.getText().toString();
                    password = post_password.getText().toString();
                    //构造post请求信息
                    RequestBody requestBody = new FormBody.Builder().add("user_name", username).add("register_1", "1").build();
                    HttpUtil.sendOkHttpRequest(address, requestBody, new Callback() {
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
                            if (state.equals("error")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity_sign_information.this, "该账户已存在", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (state.equals("success")) {
                                post_user_infor();
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
                } else {
                    Toast.makeText(activity_sign_information.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void post_user_infor() {
        RequestBody requestBody = new FormBody.Builder().add("user_name", username
        ).add("user_password", password).add("register_1", "2").build();
        HttpUtil.sendOkHttpRequest(address, requestBody, new Callback() {
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


