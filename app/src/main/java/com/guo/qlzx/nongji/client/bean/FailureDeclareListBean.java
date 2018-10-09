package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by 李 on 2018/5/31.
 */

public class FailureDeclareListBean  {

    /**
     * id : 1
     * name : JQ001型打捆机
     * sn : S00001
     * balenum : 0
     * cate_id : 1
     * position : [{"id":"4","name":"轮胎"},{"id":"5","name":"发动机"},{"id":"6","name":"轴承"}]
     */

    private String id;
    private String name;
    private String sn;
    private String balenum;
    private String cate_id;
    private List<PositionBean> position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getBalenum() {
        return balenum;
    }

    public void setBalenum(String balenum) {
        this.balenum = balenum;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public List<PositionBean> getPosition() {
        return position;
    }

    public void setPosition(List<PositionBean> position) {
        this.position = position;
    }

    public static class PositionBean {
        /**
         * id : 4
         * name : 轮胎
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
