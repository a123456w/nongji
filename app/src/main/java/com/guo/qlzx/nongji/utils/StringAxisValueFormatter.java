package com.guo.qlzx.nongji.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by chenlipeng on 2018/6/9 0009.
 * describe :
 */

public class StringAxisValueFormatter implements IAxisValueFormatter {

    //区域值
    private List<String> mStrs;

    /**
     * 对字符串类型的坐标轴标记进行格式化
     *
     * @param strs
     */
    public StringAxisValueFormatter(List<String> strs) {
        this.mStrs = strs;
    }

    @Override
    public String getFormattedValue(float v, AxisBase axisBase) {
        return v>=mStrs.size()?"": mStrs.get((int) v) ;
    }
}
