package com.guo.qlzx.nongji.client.bean;

/**
 * Created by chenlipeng on 2018/6/8 0008.
 * describe :
 */

public class MachineInfo {
    /**
     * sn : 305419896
     * bale_num : 0
     * work_status : 3
     * is_repair : 2
     * estimate : 150
     */

    private String sn;
    private String name;
    private String bale_num;
    private String work_status;
    private String is_repair;
    private String estimate;
    private String machine_id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBale_num() {
        return bale_num;
    }

    public void setBale_num(String bale_num) {
        this.bale_num = bale_num;
    }

    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
    }

    public String getIs_repair() {
        return is_repair;
    }

    public void setIs_repair(String is_repair) {
        this.is_repair = is_repair;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }
}
