package com.guo.qlzx.nongji.client.bean;

/**
 * Created by 李 on 2018/5/31.
 */

public class JobRecordListBean {


    /**
     * sn : 5
     * balenum : 146
     * cate_id : 1
     * pic : /Uploads/Picture/2018-06-13/5b20bd856a792.png
     * hour : 4
     * efficiency : 37
     * name : A55
     */

    private String sn;
    private String balenum;
    private String cate_id;
    private String pic;
    private int hour;
    private int efficiency;
    private String name;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBalenum() {
        return balenum;
    }

    public void setBalenum(String balenum) {
        this.balenum = balenum;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
