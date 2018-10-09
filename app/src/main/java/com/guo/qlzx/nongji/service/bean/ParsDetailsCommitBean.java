package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/4.
 * 零件保养提交
 */

public class ParsDetailsCommitBean {

    /**
     * work_time : 0
     * next_maintime : 300
     * maintain_pic : /Uploads/Picture/2018-05-21/5b0241a9c32af.jpg
     * description : 这是一条测试数据说明
     */

    private int work_time;
    private String next_maintime;
    private String maintain_pic;
    private String description;

    public int getWork_time() {
        return work_time;
    }

    public void setWork_time(int work_time) {
        this.work_time = work_time;
    }

    public String getNext_maintime() {
        return next_maintime;
    }

    public void setNext_maintime(String next_maintime) {
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