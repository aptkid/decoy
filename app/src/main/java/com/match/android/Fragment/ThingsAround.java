package com.match.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.match.android.Adapter.CardAdapter;
import com.match.android.object.Card;
import com.match.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是周围事界面
 */
public class ThingsAround extends Fragment {
    private List<Card> cardList;
    private SwipeRefreshLayout swip;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Context context;
    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private CardAdapter adapter;
    private GridLayoutManager mGridLayoutManager;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_tab_01, container, false);

        initData();
        findView(view);

        // 设置刷新的颜色
        initRefreshLayout();

        initRecyclerView();

        initListener();

        // 这是下拉刷新的侦听
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip.setRefreshing(true);
                adapter.resetDatas();
                updateRecyclerView(0,PAGE_COUNT);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swip.setRefreshing(false);
                    }
                },1000);
            }
        });
        return view;
    }

    /**
     * 悬浮按钮的点击事件
     *
     */
    private void initListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击了", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initRecyclerView() {

        adapter = new CardAdapter(getDatas(0, PAGE_COUNT), this, getDatas(0, PAGE_COUNT).size() > 0 ? true : false);

        mGridLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);

                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();

            }
        });

    }

    private void initRefreshLayout() {
        // 设置刷新的颜色
        swip.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swip.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) context);
    }

    private void findView(View view) {


        swip = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void initData() {
        cardList = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            Card card = new Card(R.mipmap.photo_1, "哈哈" + i);
            cardList.add(card);
        }
    }


    private List<Card> getDatas(final int firstIndex, final int lastIndex) {
        List<Card> newCardList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < cardList.size()) {
                newCardList.add(cardList.get(i));

            }

        }
        return newCardList;

    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<Card> newCard = getDatas(fromIndex, toIndex);
        if (newCard.size() > 0) {
            adapter.updateList(newCard, true);

        } else {
            adapter.updateList(null, false);
        }
    }

//    @Override
//    public void onRefresh() {
//        swip.setRefreshing(true);
//        adapter.resetDatas();
//        updateRecyclerView(0,PAGE_COUNT);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context, "hhahaha ", Toast.LENGTH_SHORT).show();
//                swip.setRefreshing(false);
//            }
//        },1000);
//    }

}
