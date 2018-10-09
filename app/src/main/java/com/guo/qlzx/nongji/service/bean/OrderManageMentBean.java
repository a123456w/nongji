package com.guo.qlzx.nongji.service.bean;

/**
 * 管理系统展示数据
 * Created by Administrator on 2018/5/24.
 */

public class OrderManageMentBean {

    /**
     * id :
     * machine_id : 5
     * balenum : 0
     * operator_id : 3
     * url : /Uploads/Picture/2018-05-24/5b068af099aa3.png
     * machine_name : JQ001型打捆机
     * operator_name : 王五
     */

    private int id;
    private String machine_id;
    private String balenum;
    private String operator_id;
    private String url;
    private String machine_name;
    private String operator_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getBalenum() {
        return balenum;
    }

    public void setBalenum(String balenum) {
        this.balenum = balenum;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }
}
