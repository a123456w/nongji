package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/4.
 * 钱包-随手记
 */

public class NotewithhandListBean {

    /**
     * expenditure : 2550
     * income : 18000
     * profit : 15450
     * list : [{"id":"8","type":"1","money":"450.00","content":"今天租车运捆花了四百五","create_time":"1525708800","label":"运费"},{"id":"9","type":"1","money":"600.00","content":"给工人发工资六百","create_time":"1525708800","label":"工资"},{"id":"10","type":"2","money":"8000.00","content":"卖捆八千块","create_time":"1525708800","label":"卖捆"},{"id":"5","type":"1","money":"500.00","content":"今天加油花了500块钱","create_time":"1525622400","label":"加油"},{"id":"6","type":"2","money":"10000.00","content":"今天把捆卖了一万","create_time":"1525622400","label":"卖捆"},{"id":"7","type":"1","money":"1000.00","content":"今天租金花了一千","create_time":"1525622400","label":"租金"}]
     */

    private int expenditure;
    private int income;
    private int profit;
    private List<ListBean> list;

    public int getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(int expenditure) {
        this.expenditure = expenditure;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public class ListBean {
        /**
         * id : 8
         * type : 1
         * money : 450.00
         * content : 今天租车运捆花了四百五
         * create_time : 1525708800
         * label : 运费
         */

        private String id;
        private String type;
        private String money;
        private String content;
        private String create_time;
        private String label;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
