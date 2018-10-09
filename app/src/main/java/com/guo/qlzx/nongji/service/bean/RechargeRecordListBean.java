package com.guo.qlzx.nongji.service.bean;

/**
 * Created by 李 on 2018/6/4.
 */

public class RechargeRecordListBean {
    /**
     * id : 35
     * money : 5000.00
     * type : 7
     * pay_type : 0
     * create_time : 1527062532
     */

    private String id;
    private String money;
    private String type;
    private String pay_type;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
