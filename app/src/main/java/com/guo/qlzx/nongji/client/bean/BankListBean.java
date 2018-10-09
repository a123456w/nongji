package com.guo.qlzx.nongji.client.bean;

/**
 * Created by 李 on 2018/6/1.
 */

public class BankListBean {
    /**
     * id : 2
     * bank : 工商银行
     * card_number : ***************3247
     * card_type : 银联借记卡
     * pic : /Uploads/Picture/2018-05-31/5b0fa92895d8f.jpg
     */

    private String id;
    private String bank;
    private String card_number;
    private String card_type;
    private String pic;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
