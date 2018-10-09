package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/1.
 * 零件
 */

public class PartsDetailsBean {

    /**
     * work_time : -10
     * next_maintime : 310
     * maintain_pic : /Uploads/Picture/2018-05-18/5afe83af979e5.jpg
     * description : 4546
     */

    private int work_time;
    private int next_maintime;
    private String maintain_pic;
    private String description;

    public int getWork_time() {
        return work_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public int getNext_maintime() {
        return next_maintime;
    }

    public void setNext_maintime(int next_maintime) {
        this.next_maintime = next_maintime;
    }

    public String getMaintain_pic() {
        return maintain_pic;
    }

    public void setMaintain_pic(String maintain_pic) {
        this.maintain_pic = maintain_pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
