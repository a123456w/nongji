package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2018/6/11 0011.
 * describe :
 */

public class StatisticsBean extends Object{


    /**
     * start_time : 1527436800
     * total_nums : 81
     * total_days : 3
     * work_hours : 2
     * efficiency : 17
     * data : [{"nums":"74","hours":3,"efficiency_hour":25,"time":1527350400},{"nums":"5","hours":1,"efficiency_hour":5,"time":1527436800},{"nums":"2","hours":1,"efficiency_hour":2,"time":1527523200}]
     */

    private String start_time;
    private String total_nums;
    private String total_days;
    private String work_hours;
    private String efficiency;
    private List<DataBean> data;

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(String total_nums) {
        this.total_nums = total_nums;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
    }

    public String getWork_hours() {
        return work_hours;
    }

    public void setWork_hours(String work_hours) {
        this.work_hours = work_hours;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nums : 74
         * hours : 3
         * efficiency_hour : 25
         * time : 1527350400
         */

        private float nums;
        private float hours;
        private float efficiency_hour;
        private long time;

        public float getNums() {
            return nums;
        }

        public void setNums(float nums) {
            this.nums = nums;
        }

        public float getHours() {
            return hours;
        }

        public void setHours(float hours) {
            this.hours = hours;
        }

        public float getEfficiency_hour() {
            return efficiency_hour;
        }

        public void setEfficiency_hour(int efficiency_hour) {
            this.efficiency_hour = efficiency_hour;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
