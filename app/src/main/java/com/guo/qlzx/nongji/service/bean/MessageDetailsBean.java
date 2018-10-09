package com.guo.qlzx.nongji.service.bean;

/**
 * Created by 李 on 2018/5/30.
 */

public class MessageDetailsBean {
    /**
     * id : 2
     * uid : 14
     * content : wotwowtotwo
     * is_read : 1
     * is_del : 0
     * create_time : 1234567890
     */

    private String id;
    private String uid;
    private String content;
    private String is_read;
    private String is_del;
    private String create_time;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
