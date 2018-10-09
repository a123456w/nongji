package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.components.AxisBase;

/**
 * Created by Philipp Jahoda on 20/09/15.
 * Custom formatter interface that allows formatting of
 * axis labels before they are being drawn.
 * 允许格式化的自定义格式化程序接口轴标签在被绘制之前。
 */
public interface IAxisValueFormatter
{

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     * 当一个轴的值被格式化时调用
     * 在被画之前。出于性能原因，避免过度计算。
     * 内存和内存分配。
     *
     * @param value the value to be formatted  要格式化的值
     * @param axis  the axis the value belongs to 值所属的轴
     * @return
     */
    String getFormattedValue(float value, AxisBase axis);
}
