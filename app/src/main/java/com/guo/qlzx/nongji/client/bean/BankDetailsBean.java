package com.guo.qlzx.nongji.client.bean;

/**
 * Created by 李 on 2018/6/5.
 */

public class BankDetailsBean {
    /**
     * id : 2
     * bank : 工商银行
     * card_number : 3247
     * card_type : 银联借记卡
     * single_limit : 10000
     * day_limit : 100000
     */

    private String id;
    private String bank;
    private String card_number;
    private String card_type;
    private String single_limit;
    private String day_limit;
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getSingle_limit() {
        return single_limit;
    }

    public void setSingle_limit(String single_limit) {
        this.single_limit = single_limit;
    }

    public String getDay_limit() {
        return day_limit;
    }

    public void setDay_limit(String day_limit) {
        this.day_limit = day_limit;
    }
}
