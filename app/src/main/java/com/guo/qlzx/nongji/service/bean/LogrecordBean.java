package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/1.
 * 日志
 */

public class LogrecordBean {

    /**
     * machine_id : 3
     * sn : S00002
     * service_name : 张三
     * user_name : 丁壮
     * operator_name : 刘旭芳
     * pic : /Uploads/Picture/2018-05-24/5b068af099aa3.png
     * last_time :
     */

    private String machine_id;
    private String sn;
    private String name;
    private String service_name;
    private String user_name;
    private String operator_name;
    private String pic;
    private String last_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }
}
