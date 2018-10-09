package com.guo.qlzx.nongji.service.costom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.jar.Attributes;

/**
 * create by xuxx on 2018/6/22
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    OnScrollChanged mOnScrollChanged;
    public MyHorizontalScrollView(Context context) {
        super(context);
    }
    public MyHorizontalScrollView(Context context, AttributeSet attributes){
        super(context,attributes);
    }
    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChanged != null)
            mOnScrollChanged.onScroll(l, t, oldl, oldt,this);
    }
    public void setOnScrollChanged(OnScrollChanged onScrollChanged){
        this.mOnScrollChanged = onScrollChanged;
    }
    public interface  OnScrollChanged{
        void onScroll(int l, int t, int oldl, int oldt, View view);
    }
}
