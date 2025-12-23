package com.jayjd.boxtop.cards.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class WeatherEntity implements Serializable {
    private String cityName;
    private double temp_c;
    private String icon;
    private String text;
    private double feelsLike_c;
    private int usEpaIndex;
    private double pm2_5;
    private String localtime;

}
