package com.match.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.match.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class activity_nextSign extends AppCompatActivity implements View.OnClickListener{

    private EditText code_1;
    private EditText code_2;
    private EditText code_3;
    private EditText code_4;
    private Button code_check;
    private EventHandler eventHandler;
    private static final int SEND_MSG = 1;
    private int textDisableColor = 0xffbbbbbb;
    private int backGroundDisableColor = 0xffdddddd;
    public static final int EDIT_NUM = 4;
    private EditText[] editTexts = new  EditText[EDIT_NUM];
    private int inputCount = 0;
    private String phoneNumber;
    private TextView resend;
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_sign);
        code_1 = (EditText) findViewById(R.id.code_1);
        code_2 = (EditText) findViewById(R.id.code_2);
        code_3 = (EditText) findViewById(R.id.code_3);
        code_4 = (EditText) findViewById(R.id.code_4);
        code_check = (Button) findViewById(R.id.code_check);
        resend = (TextView) findViewById(R.id.resend);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        code_check.setOnClickListener(this);
        resend.setOnClickListener(this);
        //初始化验证码输入框
        initEdit();
        //初始化SDK
        initSDK();
        //等待60S可重复发送
        watchTime();

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
                    Log.d("Hello", "handleMessage: ");
                    if (result == SMSSDK.RESULT_COMPLETE){
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                            SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
                            editor.putString("phoneNumber",phoneNumber);
                            editor.apply();
                            Intent intent = new Intent(activity_nextSign.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                            Toast.makeText(activity_nextSign.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            emptyCode();
                        }
                        else {
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
                                Toast.makeText(activity_nextSign.this, des, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
            }
        }
    };


    private void emptyCode() {
        //置空验证码输入框
        code_1.requestFocus();//将光标移动至第一个输入框
        code_1.setText("");
        code_2.setText("");
        code_3.setText("");
        code_4.setText("");
    }

    private void reSend(){
        resend.setText("重新发送");
        resend.setTextColor(0xffff88aa);
        resend.setClickable(true);
    }

    private void watchTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 60; i >= 0;i --){
                    final int I = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resend.setText(I+"s后可重新发送");
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //当60s过去后，可重新发送，改变文字的颜色，然按钮变的可以点击
                                reSend();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void initEdit() {

        editTexts[0] = (EditText)findViewById(R.id.code_1);
        editTexts[1] = (EditText)findViewById(R.id.code_2);
        editTexts[2] = (EditText)findViewById(R.id.code_3);
        editTexts[3] = (EditText)findViewById(R.id.code_4);

        for (int i = 0; i < EDIT_NUM; i++){
            final int finalI = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //因为是限制只能输入一位所以非0及1；
                    if (s.length()== 1){
                        if (finalI < EDIT_NUM - 1){
                            editTexts[finalI +1].requestFocus();
                        }
                        inputCount ++;

                        if (inputCount == EDIT_NUM){
                            check();
                        }
                    }
                    else{
                        //模拟删除
                        if (finalI > 0){
                            editTexts[finalI -1].requestFocus();
                        }
                        inputCount --;
                        //用于按钮禁用
                        btDisable();
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        waitPop();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (eventHandler != null){
            SMSSDK.unregisterEventHandler(eventHandler);
            Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(eventHandler);
        Log.d("验证码","onResume");
        Toast.makeText(this, "onResume  ", Toast.LENGTH_SHORT).show();
    }

    private void waitPop() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)editTexts[0].getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(code_1, 0);
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("验证码", "onDestroy: 验证成功");
        Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
    }

    private void btDisable() {
        if (code_check.isClickable()){
            code_check.setBackgroundColor(backGroundDisableColor);
            code_check.setTextColor(textDisableColor);
            code_check.setClickable(false);
        }
    }

    //该方法的目的是当输入完成后检查验证码是否匹配
    private void check() {
        String inputCode = new String();
        code_check.setTextColor(Color.WHITE);
        code_check.setBackgroundColor(0xffff88aa);
        for (int i = 0; i < EDIT_NUM; i++){
            inputCode += editTexts[i].getText().toString();
        }
        Toast.makeText(this, inputCode, Toast.LENGTH_SHORT).show();
        code = inputCode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.code_check:
                SMSSDK.submitVerificationCode("86", phoneNumber,code);
                break;
            case R.id.resend:
                //当重新发送按钮点击后，重新发送短信，倒计时重新运行，resend不能再被点击
                SMSSDK.getVerificationCode("86", phoneNumber);
                resend.setClickable(false);
                resend.setTextColor(0xff999999);
                watchTime();
                break;
        }
    }


}
