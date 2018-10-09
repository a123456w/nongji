package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2018/6/8 0008.
 * describe :
 */

public class WorkIndexBean {

    private List<DayWorkBean> day_work;
    private List<TotalWorkBean> total_work;

    public List<DayWorkBean> getDay_work() {
        return day_work;
    }

    public void setDay_work(List<DayWorkBean> day_work) {
        this.day_work = day_work;
    }

    public List<TotalWorkBean> getTotal_work() {
        return total_work;
    }

    public void setTotal_work(List<TotalWorkBean> total_work) {
        this.total_work = total_work;
    }

    public static class DayWorkBean {
        /**
         * sn : SN001
         * work_hour : 0
         * bale_num : 0
         * efficiency : 0
         */

        private String sn;
        private String name;
        private Float work_hour;
        private Float bale_num;
        private Float efficiency;

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

        public Float getWork_hour() {
            return work_hour;
        }

        public void setWork_hour(Float work_hour) {
            this.work_hour = work_hour;
        }

        public Float getBale_num() {
            return bale_num;
        }

        public void setBale_num(Float bale_num) {
            this.bale_num = bale_num;
        }

        public Float getEfficiency() {
            return efficiency;
        }

        public void setEfficiency(Float efficiency) {
            this.efficiency = efficiency;
        }
    }

    public static class TotalWorkBean {
        /**
         * sn : SN001
         * bale_num : 0
         * work_efficiency : 0
         * hour_average : 0
         */

        private String sn;
        private Float bale_num;
        private Float work_efficiency;
        private Float hour_average;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public Float getBale_num() {
            return bale_num;
        }

        public void setBale_num(Float bale_num) {
            this.bale_num = bale_num;
        }

        public Float getWork_efficiency() {
            return work_efficiency;
        }

        public void setWork_efficiency(Float work_efficiency) {
            this.work_efficiency = work_efficiency;
        }

        public Float getHour_average() {
            return hour_average;
        }

        public void setHour_average(Float hour_average) {
            this.hour_average = hour_average;
        }
    }
}
