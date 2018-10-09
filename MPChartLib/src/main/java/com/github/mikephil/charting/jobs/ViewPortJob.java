
package com.github.mikephil.charting.jobs;

import android.view.View;

import com.github.mikephil.charting.utils.ObjectPool;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Runnable that is used for viewport modifications since they cannot be
 * executed at any time. This can be used to delay the execution of viewport
 * modifications until the onSizeChanged(...) method of the chart-view is called.
 * This is especially important if viewport modifying methods are called on the chart
 * directly after initialization.
 * Runnable用于视口修改，
 * 因为它们不能在任何时候执行。
 * 这可以用来延迟视口的执行。
 * 修改，
 * 直到调用图表视图的OnsiZechange（…）方法。
 * 如果在初始化之后直接在视图上调用视口修改方法，这一点尤其重要。
 * @author Philipp Jahoda
 */
public abstract class ViewPortJob extends ObjectPool.Poolable implements Runnable {

    protected float[] pts = new float[2];

    protected ViewPortHandler mViewPortHandler;
    protected float xValue = 0f;
    protected float yValue = 0f;
    protected Transformer mTrans;
    protected View view;

    public ViewPortJob(ViewPortHandler viewPortHandler, float xValue, float yValue,
                       Transformer trans, View v) {

        this.mViewPortHandler = viewPortHandler;
        this.xValue = xValue;
        this.yValue = yValue;
        this.mTrans = trans;
        this.view = v;

    }

    public float getXValue() {
        return xValue;
    }

    public float getYValue() {
        return yValue;
    }
}
