package com.guo.qlzx.nongji.client.bean;

/**
 * Created by 李 on 2018/5/31.
 */

public class DeclareRecordListBean {
    /**
     * id : 1
     * content : 第一条测试信息
     * is_check : 0
     * machine_sn : S00001
     */

    private String id;
    private String position_name;
    private String is_check;
    private String machine_sn;
    private String machine_name;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return position_name;
    }

    public void setContent(String content) {
        this.position_name = content;
    }

    public String getIs_check() {
        return is_check;
    }

    public void setIs_check(String is_check) {
        this.is_check = is_check;
    }

    public String getMachine_sn() {
        return machine_sn;
    }

    public void setMachine_sn(String machine_sn) {
        this.machine_sn = machine_sn;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }
}
