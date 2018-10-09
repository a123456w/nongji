package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2018/6/11 0011.
 * describe :
 */

public class JournalInfoBean extends Object{


    /**
     * limit_time : {"start":"1527264000","end":1528214400}
     * show_time : {"start":"1527264000","end":1528214400}
     * position : {"lng":"116.80842100607","lat":"33.760671222157"}
     * total_nums : 330
     * total_days : 4
     * work : [{"nums":"4","time":1527350400},{"nums":"98","time":1527868800},{"nums":"173","time":1527955200},{"nums":"55","time":1528041600}]
     */

    private LimitTimeBean limit_time;
    private ShowTimeBean show_time;
    private PositionBean position;
    private int total_nums;
    private int total_days;
    private List<WorkBean> work;

    public LimitTimeBean getLimit_time() {
        return limit_time;
    }

    public void setLimit_time(LimitTimeBean limit_time) {
        this.limit_time = limit_time;
    }

    public ShowTimeBean getShow_time() {
        return show_time;
    }

    public void setShow_time(ShowTimeBean show_time) {
        this.show_time = show_time;
    }

    public PositionBean getPosition() {
        return position;
    }

    public void setPosition(PositionBean position) {
        this.position = position;
    }

    public int getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(int total_nums) {
        this.total_nums = total_nums;
    }

    public int getTotal_days() {
        return total_days;
    }

    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }

    public List<WorkBean> getWork() {
        return work;
    }

    public void setWork(List<WorkBean> work) {
        this.work = work;
    }

    public static class LimitTimeBean {
        /**
         * start : 1527264000
         * end : 1528214400
         */

        private String start;
        private int end;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    public static class ShowTimeBean {
        /**
         * start : 1527264000
         * end : 1528214400
         */

        private String start;
        private int end;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }

    public static class PositionBean {
        /**
         * lng : 116.80842100607
         * lat : 33.760671222157
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

    public static class WorkBean {
        /**
         * nums : 4
         * time : 1527350400
         */

        private float nums;
        private long time;

        public float getNums() {
            return nums;
        }

        public void setNums(float nums) {
            this.nums = nums;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
