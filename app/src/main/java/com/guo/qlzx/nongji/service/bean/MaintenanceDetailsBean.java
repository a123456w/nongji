package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/1.
 * 机器保养详情
 */

public class MaintenanceDetailsBean {

    /**
     * id : 4
     * name : 轮胎
     * pic : /Uploads/Picture/2018-05-21/5b027832a4ee7.jpg
     * model : RE500
     * maintain_time : 300
     * next_maintime : 310
     */

    private String id;
    private String name;
    private String pic;
    private String model;
    private String maintain_time;
    private int next_maintime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMaintain_time() {
        return maintain_time;
    }

    public void setMaintain_time(String maintain_time) {
        this.maintain_time = maintain_time;
    }

    public int getNext_maintime() {
        return next_maintime;
    }

    public void setNext_maintime(int next_maintime) {
        this.next_maintime = next_maintime;
    }
}
