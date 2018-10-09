package com.guo.qlzx.nongji.service.bean;

/**
 * 替换展示数据类
 * Created by Administrator on 2018/5/25.
 */

public class ReplaceListBean {


    /**
     * id : 3
     * name : JQ001型打捆机
     */

    private boolean isChecked=false;
    private String id;
    private String name;

    private String sn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

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
}
