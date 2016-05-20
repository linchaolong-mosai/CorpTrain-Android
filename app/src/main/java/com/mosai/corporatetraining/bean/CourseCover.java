package com.mosai.corporatetraining.bean;

import java.io.Serializable;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/18 0018 14:12
 * 邮箱：zhounianbin@mastercom.cn
 */
public class CourseCover implements Serializable{
    private String imgurl;
    private String name;
    private int focuse;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFocuse() {
        return focuse;
    }

    public void setFocuse(int focuse) {
        this.focuse = focuse;
    }
}
