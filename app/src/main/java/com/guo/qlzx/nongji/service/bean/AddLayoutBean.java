package com.guo.qlzx.nongji.service.bean;

/**
 * 登录
 */
public class AddLayoutBean {
    private String strBuwei;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id = 0; //当前时间戳
    public String getStrBuwei() {
        return strBuwei;
    }

    public void setStrBuwei(String strBuwei) {
        this.strBuwei = strBuwei;
    }

    public String getStrEdModel() {
        return strEdModel;
    }

    public void setStrEdModel(String strEdModel) {
        this.strEdModel = strEdModel;
    }

    public String getStrEdNum() {
        return strEdNum;
    }

    public void setStrEdNum(String strEdNum) {
        this.strEdNum = strEdNum;
    }

    private String strEdModel;
    private String strEdNum;

}
