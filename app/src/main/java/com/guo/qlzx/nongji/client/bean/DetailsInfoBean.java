package com.guo.qlzx.nongji.client.bean;

import java.util.List;

/**
 * Created by chenlipeng on 2018/6/11 0011.
 * describe :
 */

public class DetailsInfoBean  extends Object{


    /**
     * grade : [{"start":"1528042959","bale_grade":"2","end":"1528090511"},{"start":"1528090612","bale_grade":"1","end":"1528090612"},{"start":"1528092823","bale_grade":"2","end":"1528094091"}]
     * work_hours : 15
     * efficiency : 4
     * speed : 1.99
     * trajectory : [{"lng":"116.80842100607","lat":"33.760671222157"},{"lng":"116.80742922297","lat":"33.761443713473"},{"lng":"116.80788499185","lat":"33.760867819234"},{"lng":"116.80793085338","lat":"33.760142219929"},{"lng":"116.80749393485","lat":"33.761176871449"},{"lng":"116.80766298117","lat":"33.76079666286"},{"lng":"116.80897137712","lat":"33.761004902131"},{"lng":"116.80902424039","lat":"33.760417493412"},{"lng":"116.80877570891","lat":"33.761383991835"},{"lng":"116.80919714091","lat":"33.760370694584"}]
     * journal : {"content":"坏了坏了","pic":["/Uploads/2018-06-01/5b10bbd7098a6.jpg","/Uploads/2018-06-01/5b10bbd7192cb.jpg","/Uploads/2018-06-01/5b10bbd71a18c.jpg"],"reason":"地域","fault_pic":["/Uploads/2018-06-05/5b15f7e3d1811.png","/Uploads/2018-06-05/5b15f7e3e259d.png"],"solve_pic":["/Uploads/2018-06-05/5b15f7e3e259d.png"],"detailed":[{"name":"保险杠","num":1,"model":180},{"name":"螺丝","num":4,"model":150}]}
     */

    private float work_hours;
    private float efficiency;
    private double speed;
    private JournalBean journal;
    private List<GradeBean> grade;
    private List<TrajectoryBean> trajectory;
    private int time;
    public float getWork_hours() {
        return work_hours;
    }

    public void setWork_hours(float work_hours) {
        this.work_hours = work_hours;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public JournalBean getJournal() {
        return journal;
    }

    public void setJournal(JournalBean journal) {
        this.journal = journal;
    }

    public List<GradeBean> getGrade() {
        return grade;
    }

    public void setGrade(List<GradeBean> grade) {
        this.grade = grade;
    }

    public List<TrajectoryBean> getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(List<TrajectoryBean> trajectory) {
        this.trajectory = trajectory;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static class JournalBean {
        /**
         * content : 坏了坏了
         * pic : ["/Uploads/2018-06-01/5b10bbd7098a6.jpg","/Uploads/2018-06-01/5b10bbd7192cb.jpg","/Uploads/2018-06-01/5b10bbd71a18c.jpg"]
         * reason : 地域
         * fault_pic : ["/Uploads/2018-06-05/5b15f7e3d1811.png","/Uploads/2018-06-05/5b15f7e3e259d.png"]
         * solve_pic : ["/Uploads/2018-06-05/5b15f7e3e259d.png"]
         * detailed : [{"name":"保险杠","num":1,"model":180},{"name":"螺丝","num":4,"model":150}]
         */

        private String content;
        private String reason;
        private List<String> pic;
        private List<String> fault_pic;
        private List<String> solve_pic;
        private List<DetailedBean> detailed;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<String> getPic() {
            return pic;
        }

        public void setPic(List<String> pic) {
            this.pic = pic;
        }

        public List<String> getFault_pic() {
            return fault_pic;
        }

        public void setFault_pic(List<String> fault_pic) {
            this.fault_pic = fault_pic;
        }

        public List<String> getSolve_pic() {
            return solve_pic;
        }

        public void setSolve_pic(List<String> solve_pic) {
            this.solve_pic = solve_pic;
        }

        public List<DetailedBean> getDetailed() {
            return detailed;
        }

        public void setDetailed(List<DetailedBean> detailed) {
            this.detailed = detailed;
        }

        public static class DetailedBean {
            /**
             * name : 保险杠
             * num : 1
             * model : 180
             */

            private String name;
            private int num;
            private int model;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getModel() {
                return model;
            }

            public void setModel(int model) {
                this.model = model;
            }
        }
    }

    public static class GradeBean {
        /**
         * start : 1528042959
         * bale_grade : 2
         * end : 1528090511
         */

        private long start;
        private String bale_grade;
        private Long end;
        private List<String> time;
        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public String getBale_grade() {
            return bale_grade;
        }

        public void setBale_grade(String bale_grade) {
            this.bale_grade = bale_grade;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }
    }

    public static class TrajectoryBean  extends Object{
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
}
