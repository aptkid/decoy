package com.match.android.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.match.android.R;
import com.match.android.Utils.PagerSlidingTabStrip;

import java.lang.reflect.Field;

/**
 *
 *  这是首页的界面
 */

public class HomePage extends Fragment {

    /**
     * PagerSlidingTabStrip的实例
     */
    private PagerSlidingTabStrip tabs;

    private ThingsAround thingsAround;
    private MyBusiness myBusiness;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics dm;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_homepage, container, false);
        setOverflowShowingAlways();
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(0); // 设置ViewPager的缓存界面数，默认为2
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tabs.setViewPager(pager);
        setTabsValue();
        return view;
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#000000"));//#d83737   #d83737(绿)
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#000000"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }

    private void setOverflowShowingAlways() {

        try {
            ViewConfiguration config = ViewConfiguration.get(getParentFragment().getActivity());
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = {"周围事", "我的事"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    if (thingsAround == null) {
                        thingsAround = new ThingsAround();
                    }
                    return thingsAround;
                case 1:
                    if (myBusiness == null) {
                        myBusiness = new MyBusiness();
                    }
                    return myBusiness;
                default:
                    return null;

            }

        }
    }
}
