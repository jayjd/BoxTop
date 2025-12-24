package com.jayjd.boxtop.cards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jayjd.boxtop.R;
import com.jayjd.boxtop.cards.entity.WeatherApiEntity;
import com.jayjd.boxtop.cards.entity.WeatherEntity;
import com.jayjd.boxtop.utils.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.MessageFormat;


public class CardWeather extends BaseCardFragment {

    TextView tvCity;
    TextView tvTemperature;
    TextView tvWeatherDesc;
    TextView tvAirQuality, aboutInfo;
    ImageView ivWeatherIcon;

    @NonNull
    private static WeatherEntity getWeatherEntity(WeatherApiEntity weatherApiEntity, WeatherApiEntity.LocationBean location) {
        WeatherEntity weatherEntity = new WeatherEntity();
        WeatherApiEntity.CurrentBean current = weatherApiEntity.getCurrent();
        WeatherApiEntity.CurrentBean.ConditionBean condition = current.getCondition();
        WeatherApiEntity.CurrentBean.AirQualityBean airQuality = current.getAir_quality();
        weatherEntity.setCityName(location.getName());
        weatherEntity.setLocaltime(location.getLocaltime());
        weatherEntity.setTemp_c(current.getTemp_c());
        weatherEntity.setIcon("https:" + condition.getIcon());
        weatherEntity.setText(condition.getText());
        weatherEntity.setFeelsLike_c(current.getFeelslike_c());
        weatherEntity.setUsEpaIndex(airQuality.getUsEpaIndex());
        weatherEntity.setPm2_5(airQuality.getPm2_5());
        return weatherEntity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_card_weather, container, false);
        initView(rootView);
        initLocalData();
        return rootView;
    }

    private void initLocalData() {
        String weatherData = (String) SPUtils.get(appContext, "weather_data", "");
        if (!weatherData.isEmpty()) {
            WeatherEntity weatherEntity = new Gson().fromJson(weatherData, WeatherEntity.class);
            if (weatherEntity != null) {
                initViewData(weatherEntity);
            }
        }
    }

    private void initView(View rootView) {
        ivWeatherIcon = rootView.findViewById(R.id.iv_weather_icon);
        tvCity = rootView.findViewById(R.id.tv_city);
        tvTemperature = rootView.findViewById(R.id.tv_temperature);
        tvWeatherDesc = rootView.findViewById(R.id.tv_weather_desc);
        tvAirQuality = rootView.findViewById(R.id.tv_air_quality);
        aboutInfo = rootView.findViewById(R.id.about_info);
    }

    public void initData() {
        String city = (String) SPUtils.get(appContext, "city", "");
        if (city.isEmpty()) {
            city = "长春";
        }
        String format = MessageFormat.format("https://api.weatherapi.com/v1/forecast.json?key=a3889a64f7d74c5590b12941252212&q={0}&days=1&aqi=yes&alerts=yes&lang=zh", city);
        OkGo.<String>get(format).execute(new StringCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body();
                        if (!body.isEmpty()) {
                            WeatherApiEntity weatherApiEntity = new Gson().fromJson(body, WeatherApiEntity.class);
                            if (weatherApiEntity != null) {
                                WeatherApiEntity.LocationBean location = weatherApiEntity.getLocation();
                                WeatherEntity weatherEntity = getWeatherEntity(weatherApiEntity, location);
                                initViewData(weatherEntity);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onSuccess: ", e);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e("TAG", "onError: ", response.getException());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initViewData(WeatherEntity weatherEntity) {
        SPUtils.put(appContext, "weather_data", new Gson().toJson(weatherEntity));
        tvCity.setText(weatherEntity.getCityName());
        aboutInfo.setText("* 刷新时间：" + weatherEntity.getLocaltime());
        tvTemperature.setText(Math.round(weatherEntity.getTemp_c()) + "°");
        Glide.with(appContext).load(weatherEntity.getIcon()).into(ivWeatherIcon);
        tvWeatherDesc.setText(weatherEntity.getText() + " · 体感温度：" + Math.round(weatherEntity.getFeelsLike_c()) + "°");
        tvAirQuality.setText("空气质量：" + getAqiText(weatherEntity.getUsEpaIndex()) + " · PM2.5 " + weatherEntity.getPm2_5());
    }

    private String getAqiText(int index) {
        return switch (index) {
            case 1 -> "优";
            case 2 -> "良";
            case 3 -> "中";
            case 4 -> "差";
            case 5 -> "很差";
            case 6 -> "危险";
            default -> "未知";
        };
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        Log.d("CardWeather", "onFragmentVisible() called");
        initData();
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        Log.d("CardWeather", "onFragmentInvisible() called");
    }
    @Override
    protected void onProStateChanged(boolean isPro) {
        Log.d("TAG", "onProStateChanged: " + isPro);
    }
}