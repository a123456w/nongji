package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/1.
 * 保养列表
 */

public class MaintainingBean {


    /**
     * id : 24
     * machine_id : 1
     * cate_id : 1
     * name : JQ001型打捆机
     * pic : /Uploads/Picture/2018-05-18/5afe4aa6b7255.jpg
     * create_time : 1527677580
     */

    private String id;
    private String machine_id;
    private String cate_id;
    private String name;
    private String pic;
    private String create_time;

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

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
