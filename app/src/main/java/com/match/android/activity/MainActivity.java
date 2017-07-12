package com.match.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.match.android.Fragment.HomePage;
import com.match.android.Fragment.Message;
import com.match.android.Fragment.MyFragment;
import com.match.android.Fragment.Originality;
import com.match.android.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 初始化 FrameLayout
     */
    private FrameLayout mainFrameLayout;

    /**
     * 初始化 LinearLayout
     */
    private LinearLayout mainLinearLayout_01;
    private LinearLayout mainLinearLayout_02;
    private LinearLayout mainLinearLayout_03;
    private LinearLayout mainLinearLayout_04;

    /**
     * 初始化 ImageView
     */
    private ImageView mainImageView_01;
    private ImageView mainImageView_02;
    private ImageView mainImageView_03;
    private ImageView mainImageView_04;

    /**
     * 初始化 TextView
     */
    private TextView mainTextView_01;
    private TextView mainTextView_02;
    private TextView mainTextView_03;
    private TextView mainTextView_04;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    private HomePage homePage;
    private Message message;
    private Originality originality;
    private MyFragment myFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化
        initView();

        setOnClickEvent();

    }

    private void setOnClickEvent() {
        mainLinearLayout_01.setOnClickListener(this);
        mainLinearLayout_02.setOnClickListener(this);
        mainLinearLayout_03.setOnClickListener(this);
        mainLinearLayout_04.setOnClickListener(this);

    }

    /**
     * 初始化各种控件
     */
    private void initView() {


        // 获取FragmentManager对象
        fragmentManager = getSupportFragmentManager();


        // 初始化FrameLayout
        mainFrameLayout = (FrameLayout) findViewById(R.id.main_frameLayout);

        // 初始化LinearLayout
        mainLinearLayout_01 = (LinearLayout) findViewById(R.id.main_linearLayout_01);
        mainLinearLayout_02 = (LinearLayout) findViewById(R.id.main_linearLayout_02);
        mainLinearLayout_03 = (LinearLayout) findViewById(R.id.main_linearLayout_03);
        mainLinearLayout_04 = (LinearLayout) findViewById(R.id.main_linearLayout_04);

        // 初始化ImageView
        mainImageView_01 = (ImageView) findViewById(R.id.main_imageView_01);
        mainImageView_02 = (ImageView) findViewById(R.id.main_imageView_02);
        mainImageView_03 = (ImageView) findViewById(R.id.main_imageView_03);
        mainImageView_04 = (ImageView) findViewById(R.id.main_imageView_04);

        // 初始化TextView
        mainTextView_01 = (TextView) findViewById(R.id.main_textView_01);
        mainTextView_02 = (TextView) findViewById(R.id.main_textView_02);
        mainTextView_03 = (TextView) findViewById(R.id.main_textView_03);
        mainTextView_04 = (TextView) findViewById(R.id.main_textView_04);

        // 底部切换Fragment
        setTabSelection(0);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_linearLayout_01:
                setTabSelection(0);
                break;
            case R.id.main_linearLayout_02:
                setTabSelection(1);

                break;
            case R.id.main_linearLayout_03:
                setTabSelection(2);

                break;
            case R.id.main_linearLayout_04:
                setTabSelection(3);

                break;
        }

    }

    /**
     * 底部切换Fragment
     */
    public void setTabSelection(int index) {


        // 先清除所有图标的选中状态，再设置选中的状态
        clearSelection(index);

        // 开启一个事物
        fragmentTransaction = fragmentManager.beginTransaction();

        // 先隐藏掉所有的Fragment，以防止多个fragment显示在桌面上的情况
        hideFragments(fragmentTransaction);

        switch (index) {
            case 0:
                if (homePage == null) {
                    homePage = new HomePage();
                    fragmentTransaction.add(R.id.main_frameLayout, homePage);

                } else {
                    fragmentTransaction.show(homePage);
                }
                break;
            case 1:
                if (message == null) {
                    message = new Message();
                    fragmentTransaction.add(R.id.main_frameLayout, message);

                } else {
                    fragmentTransaction.show(message);
                }
                break;
            case 2:
                if (originality == null) {
                    originality = new Originality();
                    fragmentTransaction.add(R.id.main_frameLayout, originality);

                } else {
                    fragmentTransaction.show(originality);
                }
                break;
            case 3:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    fragmentTransaction.add(R.id.main_frameLayout, myFragment);

                } else {
                    fragmentTransaction.show(myFragment);
                }
                break;
        }
        // 提交事物
        fragmentTransaction.commit();
    }

    /**
     * 先隐藏掉所有的Fragment，以防止多个fragment显示在桌面上的情况
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {

        if (homePage != null) {
            fragmentTransaction.hide(homePage);
        }
        if (message != null) {
            fragmentTransaction.hide(message);
        }
        if (originality != null) {
            fragmentTransaction.hide(originality);
        }
        if (myFragment != null) {
            fragmentTransaction.hide(myFragment);
        }


    }

    /**
     * 先清除所有图标的选中状态，再设置选中的状态
     *
     * @param index
     */
    private void clearSelection(int index) {

        // 清除所有图标的选中状态
        mainImageView_01.setImageResource(R.drawable.house_01);
        mainImageView_02.setImageResource(R.drawable.house_01);
        mainImageView_03.setImageResource(R.drawable.house_01);
        mainImageView_04.setImageResource(R.drawable.house_01);

        // 根据index设置选中状态
        switch (index) {
            case 0:
                mainImageView_01.setImageResource(R.drawable.house_02);
                break;
            case 1:
                mainImageView_02.setImageResource(R.drawable.house_02);

                break;
            case 2:
                mainImageView_03.setImageResource(R.drawable.house_02);

                break;
            case 3:
                mainImageView_04.setImageResource(R.drawable.house_02);

                break;
        }


    }


    // 定义一个变量，来标识是否退出
    public static boolean isExit = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    /**
     * 处理返回键事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();

            // 利用handle延迟返送更改状态消息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
