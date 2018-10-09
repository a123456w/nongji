package com.guo.qlzx.nongji.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by chenlipeng on 2018/6/9 0009.
 * describe :
 */

public class MyValueFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return StringUtils .double2String(value, 2);
    }
}
