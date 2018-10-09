package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2018/6/11 0011.
 * describe :
 */

public class HomeIndexBean extends Object{


    /**
     * order : {"id":"21","allbalenum":"100","surplus":5,"score":"2","today_bale_num":58}
     * map : [{"bale_num":0,"sn":"1","status":3,"last_coordinate":{"lng":"116.24837692319","lat":"39.907866067107"}},{"bale_num":0,"sn":"2","status":3,"last_coordinate":"暂时没有位置信息"},{"bale_num":58,"sn":"5","status":2,"last_coordinate":{"lng":"116.24836043243","lat":"39.907852237574"}},{"bale_num":0,"sn":"3","status":3,"last_coordinate":"暂时没有位置信息"},{"bale_num":0,"sn":"4","status":3,"last_coordinate":"暂时没有位置信息"}]
     * day_work : [{"sn":"1","work_hour":0,"bale_num":0,"efficiency":0},{"sn":"2","work_hour":0,"bale_num":0,"efficiency":0},{"sn":"5","work_hour":1,"bale_num":58,"efficiency":58},{"sn":"3","work_hour":0,"bale_num":0,"efficiency":0},{"sn":"4","work_hour":0,"bale_num":0,"efficiency":0}]
     * total_work : [{"sn":"1","bale_num":"383","work_efficiency":383,"hour_average":4},{"sn":"2","bale_num":"0","work_efficiency":0,"hour_average":0},{"sn":"5","bale_num":"12","work_efficiency":12,"hour_average":1},{"sn":"3","bale_num":"0","work_efficiency":0,"hour_average":0},{"sn":"4","bale_num":"0","work_efficiency":0,"hour_average":0}]
     */

    private OrderBean order;
    private List<DayWorkBean> day_work;
    private List<TotalWorkBean> total_work;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

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

    public static class OrderBean {
        /**
         * id : 21
         * allbalenum : 100
         * surplus : 5
         * score : 2
         * today_bale_num : 58
         */

        private String id;
        private String allbalenum;
        private String surplus;
        private String score;
        private String today_bale_num;
        private String money;

        public String getSurplus() {
            return surplus;
        }

        public String getToday_bale_num() {
            return today_bale_num;
        }

        public void setSurplus(String surplus) {
            this.surplus = surplus;
        }

        public void setToday_bale_num(String today_bale_num) {
            this.today_bale_num = today_bale_num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAllbalenum() {
            return allbalenum;
        }

        public void setAllbalenum(String allbalenum) {
            this.allbalenum = allbalenum;
        }


        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }


    public static class DayWorkBean {
        /**
         * sn : 1
         * work_hour : 0
         * bale_num : 0
         * efficiency : 0
         */

        private String sn;
        private int work_hour;
        private int bale_num;
        private int efficiency;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getWork_hour() {
            return work_hour;
        }

        public void setWork_hour(int work_hour) {
            this.work_hour = work_hour;
        }

        public int getBale_num() {
            return bale_num;
        }

        public void setBale_num(int bale_num) {
            this.bale_num = bale_num;
        }

        public int getEfficiency() {
            return efficiency;
        }

        public void setEfficiency(int efficiency) {
            this.efficiency = efficiency;
        }
    }

    public static class TotalWorkBean {
        /**
         * sn : 1
         * bale_num : 383
         * work_efficiency : 383
         * hour_average : 4
         */

        private String sn;
        private String bale_num;
        private int work_efficiency;
        private int hour_average;

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

        public int getWork_efficiency() {
            return work_efficiency;
        }

        public void setWork_efficiency(int work_efficiency) {
            this.work_efficiency = work_efficiency;
        }

        public int getHour_average() {
            return hour_average;
        }

        public void setHour_average(int hour_average) {
            this.hour_average = hour_average;
        }
    }
}
