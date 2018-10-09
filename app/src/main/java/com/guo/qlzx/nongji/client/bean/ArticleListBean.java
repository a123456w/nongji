package com.guo.qlzx.nongji.client.bean;

/**
 * create by xuxx on 2018/9/15
 */
public class ArticleListBean {

    /**
     * id : 9
     * title : 高考，你好
     * pic : /Uploads/Picture/2018-09-12/5b98837075498.jpg
     * describe :  有人说，没有参加过高考的人生是不完美的。相信，有过高考记忆的每一个人，对那三年暗无天日的紧张的日子依旧记忆犹新，那种感觉就像烙印般深深的印在我们心上。这一年，2016高考，在炎炎的夏日拉开帷幕，即将参加高考的你们，准备好了吗?
     */

    private String id;
    private String title;
    private String pic;
    private String describe;
    private String url;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
