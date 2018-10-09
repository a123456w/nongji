package com.guo.qlzx.nongji.service.bean;

/**
 * 添加下拉框类
 * Created by Administrator on 2018/5/28.
 */

public class CommitAddListBean {

    /**
     * machine_id : 3
     * machine_name : JQ001型打捆机
     * machine_sn : S00002
     */

    private String machine_id;
    private String machine_name;
    private String machine_sn;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getMachine_name() {
        return machine_name;
    }

    public void setMachine_name(String machine_name) {
        this.machine_name = machine_name;
    }

    public String getMachine_sn() {
        return machine_sn;
    }

    public void setMachine_sn(String machine_sn) {
        this.machine_sn = machine_sn;
    }
}
