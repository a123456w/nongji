
package com.github.mikephil.charting.renderer;

import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Abstract baseclass of all Renderers.
 * 
 * @author Philipp Jahoda
 */
public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     * 处理图表的绘图区域的组件及其偏移量
     */
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
