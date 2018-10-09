package com.guo.qlzx.nongji.service.bean;

/**
 * Created by Administrator on 2018/6/20.
 */

/**
 * 绑定手机号
 */
public class PartyLoginBean {

    /**
     * token : 37e48eba0ee1ada61225460783e40bf25272
     * registration_id : s5126s94
     * role : 1
     */

    private String token;
    private String registration_id;
    private String role;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
