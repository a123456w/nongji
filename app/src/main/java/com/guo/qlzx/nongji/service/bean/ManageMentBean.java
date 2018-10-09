package com.guo.qlzx.nongji.service.bean;

import java.util.List;

/**
 * 管理系统
 * Created by Administrator on 2018/5/24.
 */

public class ManageMentBean {


    /**
     * id : 15
     * user_name : 丁壮
     * order_mac : [{"machine_name":"SN620型打捆机","operator_name":"刘旭芳"},{"machine_name":"打捆机","operator_name":"王五"},{"machine_name":"JQ001型打捆机","operator_name":"王五"},{"machine_name":"JQ001型打捆机","operator_name":"王五"},{"machine_name":"JQ001型打捆机","operator_name":"王五"},{"machine_name":"SN620型打捆机","operator_name":"王五"},{"machine_name":"SN620型打捆机","operator_name":"王五"},{"machine_name":"SN620型打捆机","operator_name":"王五"},{"machine_name":"SN620型打捆机","operator_name":"王五"},{"machine_name":"JQ001型打捆机","operator_name":"王五"},{"machine_name":"SN620型打捆机","operator_name":"王五"}]
     */

    private String id;
    private String user_name;
    private List<OrderMacBean> order_mac;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<OrderMacBean> getOrder_mac() {
        return order_mac;
    }

    public void setOrder_mac(List<OrderMacBean> order_mac) {
        this.order_mac = order_mac;
    }

    public static class OrderMacBean {
        /**
         * machine_name : SN620型打捆机
         * operator_name : 刘旭芳
         */

        private String machine_name;
        private String operator_name;

        public String getMachine_name() {
            return machine_name;
        }

        public void setMachine_name(String machine_name) {
            this.machine_name = machine_name;
        }

        public String getOperator_name() {
            return operator_name;
        }

        public void setOperator_name(String operator_name) {
            this.operator_name = operator_name;
        }
    }
}
