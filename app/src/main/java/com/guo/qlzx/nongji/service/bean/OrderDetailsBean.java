package com.guo.qlzx.nongji.service.bean;

import java.util.List;

/**
 * Created by 李 on 2018/5/31.
 */

public class OrderDetailsBean {
    /**
     * order : {"id":"15","order_sn":"ceshi2018525","order_aumont":"0.00","create_time":"1527238858","start_time":"1527264000","end_time":"1528992000","user_name":"丁壮","total_days":20,"surplus_days":15,"order_mac":[{"num":"3","workunit":"6","pic":"/Uploads/Picture/2018-05-24/5b068af099aa3.png","unit":"捆"}]}
     * mac : [{"sn":"S00001","pic":"/Uploads/Picture/2018-05-24/5b068af099aa3.png"},{"sn":"305419896","pic":"/Uploads/Picture/2018-05-24/5b068af099aa3.png"},{"sn":"S00002","pic":"/Uploads/Picture/2018-05-24/5b068af099aa3.png"}]
     * contribution : [{"sn":"S00001","money":78,"scale1":0.33,"scale2":"33%"},{"sn":"305419896","money":78,"scale1":0.33,"scale2":"33%"},{"sn":"S00002","money":78,"scale1":0.33,"scale2":"33%"}]
     * total : [{"nums":"5","time":1527436800},{"nums":"138","time":1527350400}]
     * total_work : [{"sn":"S00001","bale_num":"13","work_efficiency":0,"hour_average":0},{"sn":"305419896","bale_num":"13","work_efficiency":7,"hour_average":2},{"sn":"S00002","bale_num":"13","work_efficiency":0,"hour_average":0}]
     */

    private OrderBean order;
    private List<MacBean> mac;
    private List<ContributionBean> contribution;
    private List<TotalBean> total;
    private List<TotalWorkBean> total_work;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<MacBean> getMac() {
        return mac;
    }

    public void setMac(List<MacBean> mac) {
        this.mac = mac;
    }

    public List<ContributionBean> getContribution() {
        return contribution;
    }

    public void setContribution(List<ContributionBean> contribution) {
        this.contribution = contribution;
    }

    public List<TotalBean> getTotal() {
        return total;
    }

    public void setTotal(List<TotalBean> total) {
        this.total = total;
    }

    public List<TotalWorkBean> getTotal_work() {
        return total_work;
    }

    public void setTotal_work(List<TotalWorkBean> total_work) {
        this.total_work = total_work;
    }

    public static class OrderBean {
        /**
         * id : 15
         * order_sn : ceshi2018525
         * order_aumont : 0.00
         * create_time : 1527238858
         * start_time : 1527264000
         * end_time : 1528992000
         * user_name : 丁壮
         * total_days : 20
         * surplus_days : 15
         * order_mac : [{"num":"3","workunit":"6","pic":"/Uploads/Picture/2018-05-24/5b068af099aa3.png","unit":"捆"}]
         */

        private String id;
        private String order_sn;
        private String order_aumont;
        private String create_time;
        private String start_time;
        private String end_time;
        private String user_name;
        private int total_days;
        private int surplus_days;
        private List<OrderMacBean> order_mac;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getOrder_aumont() {
            return order_aumont;
        }

        public void setOrder_aumont(String order_aumont) {
            this.order_aumont = order_aumont;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getTotal_days() {
            return total_days;
        }

        public void setTotal_days(int total_days) {
            this.total_days = total_days;
        }

        public int getSurplus_days() {
            return surplus_days;
        }

        public void setSurplus_days(int surplus_days) {
            this.surplus_days = surplus_days;
        }

        public List<OrderMacBean> getOrder_mac() {
            return order_mac;
        }

        public void setOrder_mac(List<OrderMacBean> order_mac) {
            this.order_mac = order_mac;
        }

    }

    public static class MacBean {
        /**
         * sn : S00001
         * pic : /Uploads/Picture/2018-05-24/5b068af099aa3.png
         */

        private String sn;
        private String pic;
        private String name;
        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ContributionBean {
        /**
         * sn : S00001
         * money : 78
         * scale1 : 0.33
         * scale2 : 33%
         */

        private String sn;
        private String money;
        private float scale1;
        private String scale2;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ContributionBean(String sn, String money, String scale2){
            setSn(sn);
            setMoney(money);
            setScale1(scale1);
            setScale2(scale2);
        }
        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public float getScale1() {
            return scale1;
        }

        public void setScale1(float scale1) {
            this.scale1 = scale1;
        }

        public String getScale2() {
            return scale2;
        }

        public void setScale2(String scale2) {
            this.scale2 = scale2;
        }
    }

    public static class TotalBean {
        /**
         * nums : 5
         * time : 1527436800
         */

        private int nums;
        private String time;

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static class TotalWorkBean {
        /**
         * sn : S00001
         * bale_num : 13
         * work_efficiency : 0
         * hour_average : 0
         */

        private String sn;
        private Float bale_num;
        private Float work_efficiency;
        private Float hour_average;
        private String name;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
