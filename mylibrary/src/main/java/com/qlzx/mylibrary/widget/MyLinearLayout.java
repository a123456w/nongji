package com.qlzx.mylibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by chenlipeng on 2017/8/28.
 * 作用解决scrollview嵌套百度地图。滑动事件冲突的问题
 */

public class MyLinearLayout extends LinearLayout {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

//            要求不允许，拦截
            getParent().requestDisallowInterceptTouchEvent(true);//拦截父类事件

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
