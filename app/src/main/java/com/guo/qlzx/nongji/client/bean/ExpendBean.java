package com.guo.qlzx.nongji.client.bean;

/**
 * Created by Administrator on 2018/6/4.
 * 随手记支出获取标签
 */

public class ExpendBean {

    /**
     * id : 5
     * name : 加油
     */

    private String id;
    private String name;
    private boolean isChecked=false;
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public ExpendBean(String id,String name){
        setId(id);
        setName(name);
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
