package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/5.
 * 服务日志列表
 */

public class LogsSrrvicesBean {


    /**
     * id : 6
     * machine_id : 2
     * sn : 2
     * state : 3
     * pic : /Uploads/Picture/2018-06-13/5b20bd856a792.png
     * today_nums : 0
     * today_time : 0
     * total_nums : 5
     * total_days : 7
     * name : A52
     */

    private String id;
    private String machine_id;
    private String sn;
    private int state;
    private String pic;
    private int today_nums;
    private int today_time;
    private String total_nums;
    private int total_days;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getToday_nums() {
        return today_nums;
    }

    public void setToday_nums(int today_nums) {
        this.today_nums = today_nums;
    }

    public int getToday_time() {
        return today_time;
    }

    public void setToday_time(int today_time) {
        this.today_time = today_time;
    }

    public String getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(String total_nums) {
        this.total_nums = total_nums;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
