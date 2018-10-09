package com.guo.qlzx.nongji.service.bean;

import java.util.List;

/**
 * Created by 李 on 2018/5/30.
 */

public class OrderListBean {
    /**
     * id : 15
     * uid : 14
     * order_sn : ceshi2018525
     * order_aumont : 0.00
     * create_time : 1527238858
     * order_status : 1
     * start_time : 1527264000
     * end_time : 1528992000
     * user_name : 丁壮
     * total_days : 20
     * surplus_days : 16
     * machine : [{"num":"1","workunit":"6","pic":"/Uploads/Picture/2018-05-24/5b068af099aa3.png"},{"num":"1","workunit":"300","pic":"/Uploads/Picture/2018-05-24/5b068ae7b8727.png"}]
     */

    private String id;
    private String uid;
    private String order_sn;
    private String order_aumont;
    private String create_time;
    private String order_status;
    private String start_time;
    private String end_time;
    private String user_name;
    private int total_days;
    private int surplus_days;
    private List<OrderMacBean> machine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_aumont() {
        return order_aumont;
    }

    public void setOrder_aumont(String order_aumont) {
        this.order_aumont = order_aumont;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public int getSurplus_days() {
        return surplus_days;
    }

    public void setSurplus_days(int surplus_days) {
        this.surplus_days = surplus_days;
    }

    public List<OrderMacBean> getMachine() {
        return machine;
    }

    public void setMachine(List<OrderMacBean> machine) {
        this.machine = machine;
    }


}
