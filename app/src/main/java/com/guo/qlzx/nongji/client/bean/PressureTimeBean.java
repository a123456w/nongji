package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * create by xuxx on 2018/6/20
 */
public class PressureTimeBean {
    public DetailsInfoBean.GradeBean getBean() {
        return bean;
    }

    public void setBean(DetailsInfoBean.GradeBean bean) {
        this.bean = bean;
    }

    public List<Long> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Long> timeList) {
        this.timeList = timeList;
    }

    DetailsInfoBean.GradeBean bean;
    List<Long> timeList;
}
