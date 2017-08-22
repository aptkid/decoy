package com.match.android.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.match.android.Fragment.ThingsAround;
import com.match.android.Object.Card;
import com.match.android.R;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int image_fabulous = 0;
    private List<Card> cardList;
    //private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    //Looper类用来为一个线程开启一个消息循环。
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public CardAdapter(List<Card> cardList, ThingsAround comtext, boolean hasMore) {
        this.cardList = cardList;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == normalType) {
            NormalHolder normalHolder = new NormalHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, null));

            // 设置点赞的功能
            setFabulous(normalHolder);
            // 设置转发的功能
            setForward(normalHolder,parent.getContext());

            // 进入界面
            enterInterface(normalHolder,parent.getContext());
            return normalHolder;
        } else {
            return new FootHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footview, null));
        }


    }


    public int getItemCount() {
        return cardList.size() + 1;
    }

    public int getRealLastPosition() {
        return cardList.size();
    }


    public void updateList(List<Card> newCardList, boolean hasMore) {
        if (newCardList != null) {
            cardList.addAll(newCardList);

        }
        this.hasMore = hasMore;
//notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容。
        notifyDataSetChanged();
    }


    private void enterInterface(NormalHolder normalHolder, final Context context) {

        normalHolder.recycler_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "进入界面...", Toast.LENGTH_SHORT).show();
            }
        });
        normalHolder.recycler_iamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "进入界面...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).recycler_title.setText(cardList.get(position).getText());
            ((NormalHolder) holder).recycler_iamge.setImageResource(cardList.get(position).getIamge());


        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (cardList.size() > 0) {
                    ((FootHolder) holder).tips.setText("正在加载...");


                }

            } else {
                if (cardList.size() > 0) {
                    ((FootHolder) holder).tips.setText("没有更多的数据了...");

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }


        }


    }

    public void setFabulous(final NormalHolder fabulous) {
        fabulous.recycler_fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.recyclerView_item_fabulous:
                        if(image_fabulous == 0){
                            fabulous.recycler_fabulous.setImageResource(R.drawable.fabulous_02);
                            image_fabulous = 1;

                        }else {
                            fabulous.recycler_fabulous.setImageResource(R.drawable.fabulous_01);
                            image_fabulous = 0;
                        }
                        break;
                }
            }
        });
    }

    public void setForward(NormalHolder forward, final Context context) {
        forward.recycler_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "已经转发了....", Toast.LENGTH_SHORT).show();

            }
        });
    }


    class NormalHolder extends RecyclerView.ViewHolder {


        private final TextView recycler_title;
        private final ImageView recycler_fabulous;
        private final ImageView recycler_iamge;
        private final ImageView recycler_forward;

        public NormalHolder(View itemView) {
            super(itemView);
            recycler_title = (TextView) itemView.findViewById(R.id.recyclerView_item_title);

            recycler_fabulous = (ImageView) itemView.findViewById(R.id.recyclerView_item_fabulous);

            recycler_iamge = (ImageView) itemView.findViewById(R.id.recyclerView_item_iamge);

            recycler_forward = (ImageView) itemView.findViewById(R.id.recyclerView_item_forward);

        }
    }

    class FootHolder extends RecyclerView.ViewHolder {

        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
        cardList = new ArrayList<>();
    }

    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }

    }
}
