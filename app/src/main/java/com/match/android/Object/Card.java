package com.match.android.Object;

/**
 * Created by Administrator on 2017/7/10.
 */

public class Card {

    private int iamge;
    private String text;

    public int getIamge() {
        return iamge;
    }

    public void setIamge(int iamge) {
        this.iamge = iamge;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Card(int iamge, String text) {

        this.iamge = iamge;
        this.text = text;
    }
}
