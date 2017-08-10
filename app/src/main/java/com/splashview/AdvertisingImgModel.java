package com.splashview;

import java.io.Serializable;

/**
 * 广告的实体类，实际项目中修改相关参数
 */
public class AdvertisingImgModel implements Serializable{

    //id
    private int adid;
    //照片url
    private String imgurl;

    public AdvertisingImgModel() {
    }

    public AdvertisingImgModel(int adid, String imgurl) {
        this.adid = adid;
        this.imgurl = imgurl;
    }

    public int getAdid() {
        return adid;
    }

    public void setAdid(int adid) {
        this.adid = adid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
