/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import java.util.List;

/**
 * 省份、城市
 * 作者: 李苜菲
 * 日期: 2016/4/8 15:26.
 */
public class RAreaBO {
    public int provinceId;
    public String provinceName;
    public List<city> cityList;//城市集合


    public class city {
        public String pinyin;//--拼音
        public int cityId;
        public String cityName;//
        public List<area> areaList;//区域集合
        public String isHot = "0";//--是否为热门城市，0否，1是

        public city(String cityName, String pinyin) {
            this.cityName = cityName;
            this.pinyin = pinyin;
        }

        @Override
        public String toString() {
            return "city{" +
                    "pinyin='" + pinyin + '\'' +
                    ", cityId=" + cityId +
                    ", cityName='" + cityName + '\'' +
                    ", areaList=" + areaList +
                    ", isHot='" + isHot + '\'' +
                    '}';
        }
    }

    public class area {
        public String areaName;
        public int areaId;

        @Override
        public String toString() {
            return "area{" +
                    "areaName='" + areaName + '\'' +
                    ", areaId=" + areaId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RAreaBO{" +
                "provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityList=" + cityList +
                '}';
    }
}
