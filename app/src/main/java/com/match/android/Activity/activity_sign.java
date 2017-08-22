package com.match.android.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.match.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class activity_sign extends AppCompatActivity {

    private EditText userPhoneNumber;
    private Button send_code_button;
    private static final int SEND_MSG = 1;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        userPhoneNumber = (EditText) findViewById(R.id.user_phone_number);
        send_code_button = (Button) findViewById(R.id.send_code);
        send_code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity_sign.this, "已向"+userPhoneNumber.getText().toString()+"发送验证码，请注意查收", Toast.LENGTH_SHORT).show();
                register();
            }
        });
        userPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11){
                    send_code_button.setClickable(true);
                    send_code_button.setBackgroundColor(Color.BLUE);
                    send_code_button.setTextColor(Color.WHITE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initSDK();//初始化发送短信验证码SDK
        waitPop();//等待一段时间弹出键盘
    }



    private void initSDK() {
        SMSSDK.initSDK(this, "1dc7746797f76", "5e04c7bfbae774980b867159915a4b0a");
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SEND_MSG;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_MSG:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE){
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            String phoneNumber = userPhoneNumber.getText().toString();
                            Intent intent = new Intent(activity_sign.this, activity_nextSign.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);
                        }
                        else{
                            ((Throwable)data).printStackTrace();
                        }
                    }
                    //如果返回错误结果
                    if (result == SMSSDK.RESULT_ERROR){
                        try {
                            Throwable throwable = (Throwable)data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            int status = object.optInt("status");
                            Log.d("Hello", "handleMessage: " + object);
                            if (status > 0 && des != null && des != ""){
                                Toast.makeText(activity_sign.this, des, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
            }
        }
    };

    //这个是等待一段时间弹出键盘
    private void waitPop() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)userPhoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(userPhoneNumber, 0);
            }
        }, 300);
    }

    private void register() {
        String phoneNum = userPhoneNumber.getText().toString();
        //如果没有权限就申请权限,如果有了权限就跳转到验证码界面
        if (match(phoneNum)){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }
            else{
                //如果手机号正确且拥有权限那么就发送短信
                //启动注册信息界面
                    sendSMS(phoneNum);
            }
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("请不要输入正确的手机号:");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userPhoneNumber.setText("");
                }
            });
            dialog.show();
        }
    }

    //发送短信
    private void sendSMS(String phone) {
        final String phoneNum = phone;
        new AlertDialog.Builder(this).setTitle("发送短信")
                .setMessage("我们将把验证码发送到以下号码:\n"+phoneNum)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SMSSDK.getVerificationCode("86", phoneNum);
                        buttonDisable();
                    }
                }).create().show();
    }

    //用于上一个数据的返回，如果短信倒计时未结束，就不能发送短信
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){

                }
            default:
        }
    }

    private void buttonDisable() {
        send_code_button.setClickable(false);
    }

    //检验手机号格式
    private boolean match(String phoneNum) {
        String regex = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";//检验手机号的正则表达式
        boolean flag = phoneNum.matches(regex);
        return  flag;
    }
}
