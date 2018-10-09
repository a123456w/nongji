package com.guo.qlzx.nongji.client.bean;

/**
 * Created by dell on 2018/5/23.
 */

public class ConcessionExchangeBean {

    /**
     * code : 0
     * message : 恭喜您，兑换了一张面额为5000元的优惠券
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
