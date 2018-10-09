package com.guo.qlzx.nongji.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {
    private static List<Integer> colorList=new ArrayList<>();
    public static Integer getColor(int pos){
        createColor();
        return colorList.get(pos);
    }
    private static void createColor(){
        colorList.add(0,Color.parseColor("#AFF2CB"));
        colorList.add(1,Color.parseColor("#F8DCAD"));
        colorList.add(2,Color.parseColor("#F6C6C2"));
        colorList.add(3,Color.parseColor("#C9D4F8"));
        colorList.add(4,Color.parseColor("#F2D2AF"));
        colorList.add(5,Color.parseColor("#F7B6EC"));
        colorList.add(6,Color.parseColor("#D1F2AF"));
        colorList.add(7,Color.parseColor("#AFDFF2"));
        colorList.add(8,Color.parseColor("#F2AFAF"));
        colorList.add(9,Color.parseColor("#C6F2AF"));

        colorList.add(10,Color.parseColor("#E0AFF2"));
        colorList.add(11,Color.parseColor("#AFB6F2"));
        colorList.add(12,Color.parseColor("#C3F2AF"));
        colorList.add(13,Color.parseColor("#E8F2AF"));
        colorList.add(14,Color.parseColor("#F2AFB6"));
        colorList.add(15,Color.parseColor("#F2AFD0"));
        colorList.add(16,Color.parseColor("#AFD8F2"));
        colorList.add(17,Color.parseColor("#BEAFF2"));
        colorList.add(18,Color.parseColor("#AFCBF2"));
        colorList.add(19,Color.parseColor("#AFF2EA"));

        colorList.add(20,Color.parseColor("#F2AFF1"));
        colorList.add(21,Color.parseColor("#CBAFF2"));
        colorList.add(22,Color.parseColor("#D6F2AF"));
        colorList.add(23,Color.parseColor("#E7AFF2"));
        colorList.add(24,Color.parseColor("#AFDCF2"));
        colorList.add(25,Color.parseColor("#AFF2B7"));
        colorList.add(26,Color.parseColor("#CDF2AF"));
        colorList.add(27,Color.parseColor("#B3C3ED"));
        colorList.add(28,Color.parseColor("#AFE5F2"));
        colorList.add(29,Color.parseColor("#B1AFF2"));


    }
}
