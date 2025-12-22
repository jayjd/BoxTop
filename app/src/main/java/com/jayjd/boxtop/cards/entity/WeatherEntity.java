package com.jayjd.boxtop.cards.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class WeatherEntity implements Serializable {

    /**
     * code : 200
     * msg : 数据请求成功
     * data : {"city":"涿州市","data":[{"air_quality":"良","date":"周日","temperature":"-7-4℃","weather":"晴","wind":"南风1级"},{"air_quality":"良","date":"周一","temperature":"-6-4℃","weather":"多云","wind":"南风1级"},{"air_quality":"轻度","date":"周二","temperature":"-6-7℃","weather":"多云","wind":"北风1级"},{"air_quality":"轻度","date":"周三","temperature":"-6-5℃","weather":"晴","wind":"西南风1级"},{"air_quality":"良","date":"周四","temperature":"-7-3℃","weather":"晴","wind":"南风1级"},{"air_quality":"轻度","date":"周五","temperature":"-7-1℃","weather":"多云","wind":"东风1级"}]}
     * request_id : c2de47d31c1afd55001c9c5a
     */

    private int code;
    private String msg;
    private DataBeanX data;
    private String request_id;

    @Data
    public static class DataBeanX implements Serializable {
        /**
         * city : 涿州市
         * data : [{"air_quality":"良","date":"周日","temperature":"-7-4℃","weather":"晴","wind":"南风1级"},{"air_quality":"良","date":"周一","temperature":"-6-4℃","weather":"多云","wind":"南风1级"},{"air_quality":"轻度","date":"周二","temperature":"-6-7℃","weather":"多云","wind":"北风1级"},{"air_quality":"轻度","date":"周三","temperature":"-6-5℃","weather":"晴","wind":"西南风1级"},{"air_quality":"良","date":"周四","temperature":"-7-3℃","weather":"晴","wind":"南风1级"},{"air_quality":"轻度","date":"周五","temperature":"-7-1℃","weather":"多云","wind":"东风1级"}]
         */

        private String city;
        private List<DataBean> data;

        @Data
        public static class DataBean implements Serializable {
            /**
             * air_quality : 良
             * date : 周日
             * temperature : -7-4℃
             * weather : 晴
             * wind : 南风1级
             */

            private String air_quality;
            private String date;
            private String temperature;
            private String weather;
            private String wind;
        }
    }
}
