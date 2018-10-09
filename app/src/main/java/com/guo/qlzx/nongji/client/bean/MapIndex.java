package com.guo.qlzx.nongji.client.bean;

/**
 * Created by chenlipeng on 2018/6/8 0008.
 * describe :
 */

public class MapIndex {
    /**
     * bale_num : 0
     * sn : 305419896
     * reason_id : 2
     * status : 3
     * last_coordinate : {"lng":"116.24171316667","lat":"39.906994"}
     */

    private String  bale_num;
    private String sn;
    private String reason_id;
    private int status;
    private String name;
    private String machine_id;
    private LastCoordinateBean last_coordinate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBale_num() {
        return bale_num;
    }

    public void setBale_num(String bale_num) {
        this.bale_num = bale_num;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LastCoordinateBean getLast_coordinate() {
        return last_coordinate;
    }

    public void setLast_coordinate(LastCoordinateBean last_coordinate) {
        this.last_coordinate = last_coordinate;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public static class LastCoordinateBean   extends Object{
        /**
         * lng : 116.24171316667
         * lat : 39.906994
         */

        private String lng;
        private String lat;

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }
}
