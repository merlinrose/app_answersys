package com.tao.answersys.bean;

/**
 * Created by LiangTao on 2017/5/21.
 */

public class ImgViewItem {
    private String url;
    private String fileUrl;

    public ImgViewItem(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
