package com.jayjd.boxtop.cards.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class WeatherApiEntity implements Serializable {

    /**
     * location : {"name":"海淀","region":"Beijing","country":"中国","lat":39.9792,"lon":116.2992,"tz_id":"Asia/Shanghai","localtime_epoch":1766368368,"localtime":"2025-12-22 09:52"}
     * current : {"last_updated_epoch":1766367900,"last_updated":"2025-12-22 09:45","temp_c":-0.6,"temp_f":30.9,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":42,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"humidity":64,"cloud":0,"feelslike_c":-2.6,"feelslike_f":27.3,"windchill_c":-4.3,"windchill_f":24.3,"heatindex_c":-2.1,"heatindex_f":28.3,"dewpoint_c":-14.6,"dewpoint_f":5.7,"vis_km":10,"vis_miles":6,"uv":0.2,"gust_mph":4.8,"gust_kph":7.7,"air_quality":{"co":1852.85,"no2":56.75,"o3":0,"so2":108.75,"pm2_5":126.15,"pm10":129.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":93.32,"diff_rad":33.82,"dni":0,"gti":32.06}
     * forecast : {"forecastday":[{"date":"2025-12-22","date_epoch":1766361600,"day":{"maxtemp_c":0.5,"maxtemp_f":32.8,"mintemp_c":-2.7,"mintemp_f":27.2,"avgtemp_c":-1.1,"avgtemp_f":30.1,"maxwind_mph":4.5,"maxwind_kph":7.2,"totalprecip_mm":0,"totalprecip_in":0,"totalsnow_cm":0,"avgvis_km":10,"avgvis_miles":6,"avghumidity":36,"daily_will_it_rain":0,"daily_chance_of_rain":0,"daily_will_it_snow":0,"daily_chance_of_snow":0,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"uv":0.2,"air_quality":{"co":5527.086000000002,"no2":57.778,"o3":2.52,"so2":231.99399999999997,"pm2_5":251.27800000000002,"pm10":254.47800000000004,"us-epa-index":6,"gb-defra-index":10}},"astro":{"sunrise":"07:34 AM","sunset":"04:53 PM","moonrise":"09:26 AM","moonset":"06:41 PM","moon_phase":"Waxing Crescent","moon_illumination":4,"is_moon_up":0,"is_sun_up":0},"hour":[{"time_epoch":1766332800,"time":"2025-12-22 00:00","temp_c":-1,"temp_f":30.1,"is_day":0,"condition":{"text":"Partly Cloudy ","icon":"//cdn.weatherapi.com/weather/64x64/night/116.png","code":1003},"wind_mph":1.6,"wind_kph":2.5,"wind_degree":130,"wind_dir":"SE","pressure_mb":1029,"pressure_in":30.39,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":32,"cloud":25,"feelslike_c":-1.1,"feelslike_f":30.1,"windchill_c":-1.1,"windchill_f":30.1,"heatindex_c":-1.1,"heatindex_f":30.1,"dewpoint_c":-15.7,"dewpoint_f":3.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.3,"gust_kph":3.8,"uv":0,"air_quality":{"co":1050.85,"no2":55.25,"o3":7,"so2":65.65,"pm2_5":53.75,"pm10":56.25,"us-epa-index":3,"gb-defra-index":6},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766336400,"time":"2025-12-22 01:00","temp_c":-1.2,"temp_f":29.8,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":1.3,"wind_kph":2.2,"wind_degree":77,"wind_dir":"ENE","pressure_mb":1029,"pressure_in":30.38,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":33,"cloud":20,"feelslike_c":-1.2,"feelslike_f":29.8,"windchill_c":-1.2,"windchill_f":29.8,"heatindex_c":-1.2,"heatindex_f":29.8,"dewpoint_c":-15.4,"dewpoint_f":4.3,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2,"gust_kph":3.2,"uv":0,"air_quality":{"co":1320.85,"no2":59.35,"o3":2,"so2":72.75,"pm2_5":77.65,"pm10":80.05,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766340000,"time":"2025-12-22 02:00","temp_c":-1.5,"temp_f":29.4,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2,"wind_kph":3.2,"wind_degree":63,"wind_dir":"ENE","pressure_mb":1029,"pressure_in":30.37,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":16,"feelslike_c":-2.2,"feelslike_f":28,"windchill_c":-2.2,"windchill_f":28,"heatindex_c":-1.5,"heatindex_f":29.4,"dewpoint_c":-15.2,"dewpoint_f":4.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3,"gust_kph":4.8,"uv":0,"air_quality":{"co":1578.85,"no2":60.15,"o3":0,"so2":95.05,"pm2_5":116.15,"pm10":118.45,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766343600,"time":"2025-12-22 03:00","temp_c":-1.8,"temp_f":28.8,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.9,"wind_kph":4.7,"wind_degree":34,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":36,"cloud":18,"feelslike_c":-3.5,"feelslike_f":25.8,"windchill_c":-3.5,"windchill_f":25.8,"heatindex_c":-1.8,"heatindex_f":28.8,"dewpoint_c":-14.9,"dewpoint_f":5.2,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.3,"gust_kph":6.8,"uv":0,"air_quality":{"co":1849.85,"no2":59.25,"o3":0,"so2":125.45,"pm2_5":150.35,"pm10":153.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766347200,"time":"2025-12-22 04:00","temp_c":-2.1,"temp_f":28.2,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":4,"wind_kph":6.5,"wind_degree":36,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":15,"feelslike_c":-4.7,"feelslike_f":23.6,"windchill_c":-4.7,"windchill_f":23.6,"heatindex_c":-2.1,"heatindex_f":28.2,"dewpoint_c":-14.6,"dewpoint_f":5.6,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.8,"gust_kph":9.3,"uv":0,"air_quality":{"co":2041.85,"no2":58.45,"o3":0,"so2":146.05,"pm2_5":171.25,"pm10":174.05,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766350800,"time":"2025-12-22 05:00","temp_c":-2.4,"temp_f":27.7,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":4.5,"wind_kph":7.2,"wind_degree":30,"wind_dir":"NNE","pressure_mb":1028,"pressure_in":30.34,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":38,"cloud":20,"feelslike_c":-5.3,"feelslike_f":22.5,"windchill_c":-5.3,"windchill_f":22.5,"heatindex_c":-2.4,"heatindex_f":27.7,"dewpoint_c":-14.6,"dewpoint_f":5.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":6.4,"gust_kph":10.3,"uv":0,"air_quality":{"co":2108.85,"no2":58.35,"o3":0,"so2":148.15,"pm2_5":170.85,"pm10":173.15,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766354400,"time":"2025-12-22 06:00","temp_c":-2.6,"temp_f":27.4,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":3.8,"wind_kph":6.1,"wind_degree":33,"wind_dir":"NNE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":39,"cloud":19,"feelslike_c":-5,"feelslike_f":23,"windchill_c":-5,"windchill_f":23,"heatindex_c":-2.6,"heatindex_f":27.4,"dewpoint_c":-14.5,"dewpoint_f":5.9,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.5,"gust_kph":8.8,"uv":0,"air_quality":{"co":2097.85,"no2":58.25,"o3":0,"so2":140.45,"pm2_5":161.55,"pm10":164.25,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766358000,"time":"2025-12-22 07:00","temp_c":-2.7,"temp_f":27.2,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":31,"wind_dir":"NNE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":39,"cloud":16,"feelslike_c":-5,"feelslike_f":23,"windchill_c":-5,"windchill_f":23,"heatindex_c":-2.7,"heatindex_f":27.2,"dewpoint_c":-14.5,"dewpoint_f":5.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.2,"gust_kph":8.4,"uv":0,"air_quality":{"co":2060.85,"no2":58.05,"o3":0,"so2":131.05,"pm2_5":149.85,"pm10":152.65,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766361600,"time":"2025-12-22 08:00","temp_c":-2.7,"temp_f":27.2,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.4,"wind_kph":5.4,"wind_degree":42,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":39,"cloud":4,"feelslike_c":-4.8,"feelslike_f":23.3,"windchill_c":-4.8,"windchill_f":23.3,"heatindex_c":-2.7,"heatindex_f":27.2,"dewpoint_c":-14.6,"dewpoint_f":5.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.9,"gust_kph":7.8,"uv":0,"air_quality":{"co":1969.85,"no2":57.45,"o3":0,"so2":120.75,"pm2_5":134.05,"pm10":137.25,"us-epa-index":4,"gb-defra-index":10},"short_rad":0.7,"diff_rad":0.33,"dni":0,"gti":0.31},{"time_epoch":1766365200,"time":"2025-12-22 09:00","temp_c":-0.6,"temp_f":30.9,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":42,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":64,"cloud":0,"feelslike_c":-4.3,"feelslike_f":24.3,"windchill_c":-4.3,"windchill_f":24.3,"heatindex_c":-2.1,"heatindex_f":28.3,"dewpoint_c":-14.6,"dewpoint_f":5.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.8,"gust_kph":7.7,"uv":0.2,"air_quality":{"co":1852.85,"no2":56.75,"o3":0,"so2":108.75,"pm2_5":126.15,"pm10":129.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":93.32,"diff_rad":33.82,"dni":0,"gti":32.06},{"time_epoch":1766368800,"time":"2025-12-22 10:00","temp_c":-1.3,"temp_f":29.6,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.4,"wind_kph":5.4,"wind_degree":49,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":36,"cloud":7,"feelslike_c":-3.3,"feelslike_f":26.1,"windchill_c":-3.3,"windchill_f":26.1,"heatindex_c":-1.3,"heatindex_f":29.6,"dewpoint_c":-14.4,"dewpoint_f":6.1,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.2,"gust_kph":6.8,"uv":0.5,"air_quality":{"co":1847.85,"no2":55.95,"o3":0,"so2":100.85,"pm2_5":122.35,"pm10":126.55,"us-epa-index":4,"gb-defra-index":10},"short_rad":164.46,"diff_rad":48.61,"dni":0,"gti":46.78},{"time_epoch":1766372400,"time":"2025-12-22 11:00","temp_c":-0.7,"temp_f":30.7,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":41,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":35,"cloud":6,"feelslike_c":-2.8,"feelslike_f":27,"windchill_c":-2.8,"windchill_f":27,"heatindex_c":-0.7,"heatindex_f":30.7,"dewpoint_c":-14.2,"dewpoint_f":6.4,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.4,"gust_kph":7.1,"uv":0.8,"air_quality":{"co":2012.85,"no2":55.15,"o3":0,"so2":95.55,"pm2_5":118.45,"pm10":122.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":202.66,"diff_rad":73.82,"dni":0,"gti":69.94},{"time_epoch":1766376000,"time":"2025-12-22 12:00","temp_c":-0.1,"temp_f":31.8,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":2.5,"wind_kph":4,"wind_degree":30,"wind_dir":"NNE","pressure_mb":1027,"pressure_in":30.33,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":4,"feelslike_c":-1.2,"feelslike_f":29.9,"windchill_c":-1.2,"windchill_f":29.9,"heatindex_c":-0.1,"heatindex_f":31.8,"dewpoint_c":-14.1,"dewpoint_f":6.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3,"gust_kph":4.8,"uv":1,"air_quality":{"co":2289.85,"no2":54.25,"o3":0,"so2":94.35,"pm2_5":120.95,"pm10":125.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":245.36,"diff_rad":90.2,"dni":0,"gti":85.41},{"time_epoch":1766379600,"time":"2025-12-22 13:00","temp_c":0.2,"temp_f":32.3,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":2.2,"wind_kph":3.6,"wind_degree":46,"wind_dir":"NE","pressure_mb":1025,"pressure_in":30.28,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":4,"feelslike_c":-0.7,"feelslike_f":30.8,"windchill_c":-0.7,"windchill_f":30.8,"heatindex_c":0.2,"heatindex_f":32.3,"dewpoint_c":-13.8,"dewpoint_f":7.2,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.7,"gust_kph":4.4,"uv":0.9,"air_quality":{"co":2646.85,"no2":53.35,"o3":0,"so2":107.15,"pm2_5":131.25,"pm10":135.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":271.56,"diff_rad":100.68,"dni":0,"gti":95.27},{"time_epoch":1766383200,"time":"2025-12-22 14:00","temp_c":0.5,"temp_f":32.8,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":1.6,"wind_kph":2.5,"wind_degree":63,"wind_dir":"ENE","pressure_mb":1025,"pressure_in":30.26,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":10,"feelslike_c":0.5,"feelslike_f":32.8,"windchill_c":0.5,"windchill_f":32.8,"heatindex_c":0.5,"heatindex_f":32.8,"dewpoint_c":-13.4,"dewpoint_f":7.9,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":1.9,"gust_kph":3.1,"uv":0.7,"air_quality":{"co":3202.85,"no2":52.35,"o3":0,"so2":150.65,"pm2_5":150.35,"pm10":154.95,"us-epa-index":4,"gb-defra-index":10},"short_rad":291.04,"diff_rad":97.82,"dni":0,"gti":93.2},{"time_epoch":1766386800,"time":"2025-12-22 15:00","temp_c":0.4,"temp_f":32.6,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":0.4,"wind_kph":0.7,"wind_degree":62,"wind_dir":"ENE","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":35,"cloud":8,"feelslike_c":0.4,"feelslike_f":32.6,"windchill_c":0.4,"windchill_f":32.6,"heatindex_c":0.4,"heatindex_f":32.6,"dewpoint_c":-13.2,"dewpoint_f":8.3,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":0.6,"gust_kph":0.9,"uv":0.3,"air_quality":{"co":3837.85,"no2":51.35,"o3":0,"so2":208.15,"pm2_5":214.55,"pm10":219.25,"us-epa-index":5,"gb-defra-index":10},"short_rad":204.96,"diff_rad":97.82,"dni":0,"gti":91.19},{"time_epoch":1766390400,"time":"2025-12-22 16:00","temp_c":0.2,"temp_f":32.3,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":1.8,"wind_kph":2.9,"wind_degree":359,"wind_dir":"N","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":36,"cloud":9,"feelslike_c":0.2,"feelslike_f":32.3,"windchill_c":0.2,"windchill_f":32.3,"heatindex_c":0.2,"heatindex_f":32.3,"dewpoint_c":-13.1,"dewpoint_f":8.4,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.5,"gust_kph":4,"uv":0.1,"air_quality":{"co":4158.85,"no2":50.45,"o3":0,"so2":240.05,"pm2_5":304.45,"pm10":309.15,"us-epa-index":6,"gb-defra-index":10},"short_rad":176.42,"diff_rad":76.54,"dni":1663.51,"gti":0},{"time_epoch":1766394000,"time":"2025-12-22 17:00","temp_c":-0.4,"temp_f":31.3,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.7,"wind_kph":4.3,"wind_degree":355,"wind_dir":"N","pressure_mb":1025,"pressure_in":30.26,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":12,"feelslike_c":-1.7,"feelslike_f":29,"windchill_c":-1.7,"windchill_f":29,"heatindex_c":-0.4,"heatindex_f":31.3,"dewpoint_c":-13.4,"dewpoint_f":8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4,"gust_kph":6.5,"uv":0,"air_quality":{"co":3902.85,"no2":49.45,"o3":2,"so2":221.85,"pm2_5":310.95,"pm10":313.75,"us-epa-index":6,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766397600,"time":"2025-12-22 18:00","temp_c":-0.6,"temp_f":30.9,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":346,"wind_dir":"NNW","pressure_mb":1025,"pressure_in":30.26,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":8,"feelslike_c":-2.6,"feelslike_f":27.3,"windchill_c":-2.6,"windchill_f":27.3,"heatindex_c":-0.6,"heatindex_f":30.9,"dewpoint_c":-13.5,"dewpoint_f":7.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.5,"gust_kph":8.9,"uv":0,"air_quality":{"co":3331.85,"no2":48.55,"o3":5,"so2":177.95,"pm2_5":167.05,"pm10":171.75,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766401200,"time":"2025-12-22 19:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.5,"wind_kph":4,"wind_degree":349,"wind_dir":"N","pressure_mb":1024,"pressure_in":30.25,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":14,"feelslike_c":-1.9,"feelslike_f":28.5,"windchill_c":-1.9,"windchill_f":28.5,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.6,"dewpoint_f":7.6,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3.9,"gust_kph":6.2,"uv":0,"air_quality":{"co":2839.85,"no2":48.45,"o3":8,"so2":142.15,"pm2_5":121.45,"pm10":126.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766404800,"time":"2025-12-22 20:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.5,"wind_kph":4,"wind_degree":350,"wind_dir":"N","pressure_mb":1024,"pressure_in":30.25,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":14,"feelslike_c":-1.9,"feelslike_f":28.6,"windchill_c":-1.9,"windchill_f":28.6,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.4,"dewpoint_f":7.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3.8,"gust_kph":6.2,"uv":0,"air_quality":{"co":2435.85,"no2":49.25,"o3":10,"so2":124.85,"pm2_5":108.45,"pm10":112.35,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766408400,"time":"2025-12-22 21:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":1.6,"wind_kph":2.5,"wind_degree":18,"wind_dir":"NNE","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":19,"feelslike_c":-0.8,"feelslike_f":30.7,"windchill_c":-0.8,"windchill_f":30.7,"heatindex_c":-0.8,"heatindex_f":30.7,"dewpoint_c":-13.4,"dewpoint_f":7.9,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.4,"gust_kph":3.9,"uv":0,"air_quality":{"co":2109.85,"no2":50.85,"o3":12,"so2":115.65,"pm2_5":95.65,"pm10":99.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766412000,"time":"2025-12-22 22:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Partly Cloudy ","icon":"//cdn.weatherapi.com/weather/64x64/night/116.png","code":1003},"wind_mph":2.9,"wind_kph":4.7,"wind_degree":24,"wind_dir":"NNE","pressure_mb":1024,"pressure_in":30.25,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":38,"cloud":36,"feelslike_c":-2.3,"feelslike_f":27.8,"windchill_c":-2.3,"windchill_f":27.8,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.3,"dewpoint_f":8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.5,"gust_kph":7.2,"uv":0,"air_quality":{"co":2225.85,"no2":53.35,"o3":12,"so2":116.45,"pm2_5":87.25,"pm10":90.75,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766415600,"time":"2025-12-22 23:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Overcast ","icon":"//cdn.weatherapi.com/weather/64x64/night/122.png","code":1009},"wind_mph":2.2,"wind_kph":3.6,"wind_degree":17,"wind_dir":"NNE","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":38,"cloud":92,"feelslike_c":-1.7,"feelslike_f":28.9,"windchill_c":-1.7,"windchill_f":28.9,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.2,"dewpoint_f":8.3,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3.5,"gust_kph":5.6,"uv":0,"air_quality":{"co":2994.85,"no2":57.85,"o3":9,"so2":126.95,"pm2_5":84.65,"pm10":87.95,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0}]}]}
     * alerts : {"alert":[]}
     */

    private LocationBean location;
    private CurrentBean current;
    private ForecastBean forecast;
    private AlertsBean alerts;

    @Data
    public static class LocationBean implements Serializable {
        /**
         * name : 海淀
         * region : Beijing
         * country : 中国
         * lat : 39.9792
         * lon : 116.2992
         * tz_id : Asia/Shanghai
         * localtime_epoch : 1766368368
         * localtime : 2025-12-22 09:52
         */

        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        private String tz_id;
        private int localtime_epoch;
        private String localtime;
    }

    @Data
    public static class CurrentBean implements Serializable {
        /**
         * last_updated_epoch : 1766367900
         * last_updated : 2025-12-22 09:45
         * temp_c : -0.6
         * temp_f : 30.9
         * is_day : 1
         * condition : {"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000}
         * wind_mph : 3.6
         * wind_kph : 5.8
         * wind_degree : 42
         * wind_dir : NE
         * pressure_mb : 1028
         * pressure_in : 30.36
         * precip_mm : 0
         * precip_in : 0
         * humidity : 64
         * cloud : 0
         * feelslike_c : -2.6
         * feelslike_f : 27.3
         * windchill_c : -4.3
         * windchill_f : 24.3
         * heatindex_c : -2.1
         * heatindex_f : 28.3
         * dewpoint_c : -14.6
         * dewpoint_f : 5.7
         * vis_km : 10
         * vis_miles : 6
         * uv : 0.2
         * gust_mph : 4.8
         * gust_kph : 7.7
         * air_quality : {"co":1852.85,"no2":56.75,"o3":0,"so2":108.75,"pm2_5":126.15,"pm10":129.85,"us-epa-index":4,"gb-defra-index":10}
         * short_rad : 93.32
         * diff_rad : 33.82
         * dni : 0
         * gti : 32.06
         */

        private int last_updated_epoch;
        private String last_updated;
        private double temp_c;
        private double temp_f;
        private int is_day;
        private ConditionBean condition;
        private double wind_mph;
        private double wind_kph;
        private double wind_degree;
        private String wind_dir;
        private double pressure_mb;
        private double pressure_in;
        private double precip_mm;
        private double precip_in;
        private double humidity;
        private int cloud;
        private double feelslike_c;
        private double feelslike_f;
        private double windchill_c;
        private double windchill_f;
        private double heatindex_c;
        private double heatindex_f;
        private double dewpoint_c;
        private double dewpoint_f;
        private int vis_km;
        private int vis_miles;
        private double uv;
        private double gust_mph;
        private double gust_kph;
        private AirQualityBean air_quality;
        private double short_rad;
        private double diff_rad;
        private int dni;
        private double gti;

        @Data
        public static class ConditionBean implements Serializable {
            /**
             * text : Sunny
             * icon : //cdn.weatherapi.com/weather/64x64/day/113.png
             * code : 1000
             */

            private String text;
            private String icon;
            private int code;
        }

        @Data
        public static class AirQualityBean implements Serializable {
            /**
             * co : 1852.85
             * no2 : 56.75
             * o3 : 0
             * so2 : 108.75
             * pm2_5 : 126.15
             * pm10 : 129.85
             * us-epa-index : 4
             * gb-defra-index : 10
             */

            private double co;
            private double no2;
            private int o3;
            private double so2;
            private double pm2_5;
            private double pm10;
            @SerializedName("us-epa-index")
            private int usEpaIndex;
            @SerializedName("gb-defra-index")
            private int gbDefraIndex;
        }
    }

    @Data
    public static class ForecastBean implements Serializable {
        private List<ForecastdayBean> forecastday;

        @Data
        public static class ForecastdayBean implements Serializable {
            /**
             * date : 2025-12-22
             * date_epoch : 1766361600
             * day : {"maxtemp_c":0.5,"maxtemp_f":32.8,"mintemp_c":-2.7,"mintemp_f":27.2,"avgtemp_c":-1.1,"avgtemp_f":30.1,"maxwind_mph":4.5,"maxwind_kph":7.2,"totalprecip_mm":0,"totalprecip_in":0,"totalsnow_cm":0,"avgvis_km":10,"avgvis_miles":6,"avghumidity":36,"daily_will_it_rain":0,"daily_chance_of_rain":0,"daily_will_it_snow":0,"daily_chance_of_snow":0,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"uv":0.2,"air_quality":{"co":5527.086000000002,"no2":57.778,"o3":2.52,"so2":231.99399999999997,"pm2_5":251.27800000000002,"pm10":254.47800000000004,"us-epa-index":6,"gb-defra-index":10}}
             * astro : {"sunrise":"07:34 AM","sunset":"04:53 PM","moonrise":"09:26 AM","moonset":"06:41 PM","moon_phase":"Waxing Crescent","moon_illumination":4,"is_moon_up":0,"is_sun_up":0}
             * hour : [{"time_epoch":1766332800,"time":"2025-12-22 00:00","temp_c":-1,"temp_f":30.1,"is_day":0,"condition":{"text":"Partly Cloudy ","icon":"//cdn.weatherapi.com/weather/64x64/night/116.png","code":1003},"wind_mph":1.6,"wind_kph":2.5,"wind_degree":130,"wind_dir":"SE","pressure_mb":1029,"pressure_in":30.39,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":32,"cloud":25,"feelslike_c":-1.1,"feelslike_f":30.1,"windchill_c":-1.1,"windchill_f":30.1,"heatindex_c":-1.1,"heatindex_f":30.1,"dewpoint_c":-15.7,"dewpoint_f":3.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.3,"gust_kph":3.8,"uv":0,"air_quality":{"co":1050.85,"no2":55.25,"o3":7,"so2":65.65,"pm2_5":53.75,"pm10":56.25,"us-epa-index":3,"gb-defra-index":6},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766336400,"time":"2025-12-22 01:00","temp_c":-1.2,"temp_f":29.8,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":1.3,"wind_kph":2.2,"wind_degree":77,"wind_dir":"ENE","pressure_mb":1029,"pressure_in":30.38,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":33,"cloud":20,"feelslike_c":-1.2,"feelslike_f":29.8,"windchill_c":-1.2,"windchill_f":29.8,"heatindex_c":-1.2,"heatindex_f":29.8,"dewpoint_c":-15.4,"dewpoint_f":4.3,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2,"gust_kph":3.2,"uv":0,"air_quality":{"co":1320.85,"no2":59.35,"o3":2,"so2":72.75,"pm2_5":77.65,"pm10":80.05,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766340000,"time":"2025-12-22 02:00","temp_c":-1.5,"temp_f":29.4,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2,"wind_kph":3.2,"wind_degree":63,"wind_dir":"ENE","pressure_mb":1029,"pressure_in":30.37,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":16,"feelslike_c":-2.2,"feelslike_f":28,"windchill_c":-2.2,"windchill_f":28,"heatindex_c":-1.5,"heatindex_f":29.4,"dewpoint_c":-15.2,"dewpoint_f":4.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3,"gust_kph":4.8,"uv":0,"air_quality":{"co":1578.85,"no2":60.15,"o3":0,"so2":95.05,"pm2_5":116.15,"pm10":118.45,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766343600,"time":"2025-12-22 03:00","temp_c":-1.8,"temp_f":28.8,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.9,"wind_kph":4.7,"wind_degree":34,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":36,"cloud":18,"feelslike_c":-3.5,"feelslike_f":25.8,"windchill_c":-3.5,"windchill_f":25.8,"heatindex_c":-1.8,"heatindex_f":28.8,"dewpoint_c":-14.9,"dewpoint_f":5.2,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.3,"gust_kph":6.8,"uv":0,"air_quality":{"co":1849.85,"no2":59.25,"o3":0,"so2":125.45,"pm2_5":150.35,"pm10":153.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766347200,"time":"2025-12-22 04:00","temp_c":-2.1,"temp_f":28.2,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":4,"wind_kph":6.5,"wind_degree":36,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":15,"feelslike_c":-4.7,"feelslike_f":23.6,"windchill_c":-4.7,"windchill_f":23.6,"heatindex_c":-2.1,"heatindex_f":28.2,"dewpoint_c":-14.6,"dewpoint_f":5.6,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.8,"gust_kph":9.3,"uv":0,"air_quality":{"co":2041.85,"no2":58.45,"o3":0,"so2":146.05,"pm2_5":171.25,"pm10":174.05,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766350800,"time":"2025-12-22 05:00","temp_c":-2.4,"temp_f":27.7,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":4.5,"wind_kph":7.2,"wind_degree":30,"wind_dir":"NNE","pressure_mb":1028,"pressure_in":30.34,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":38,"cloud":20,"feelslike_c":-5.3,"feelslike_f":22.5,"windchill_c":-5.3,"windchill_f":22.5,"heatindex_c":-2.4,"heatindex_f":27.7,"dewpoint_c":-14.6,"dewpoint_f":5.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":6.4,"gust_kph":10.3,"uv":0,"air_quality":{"co":2108.85,"no2":58.35,"o3":0,"so2":148.15,"pm2_5":170.85,"pm10":173.15,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766354400,"time":"2025-12-22 06:00","temp_c":-2.6,"temp_f":27.4,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":3.8,"wind_kph":6.1,"wind_degree":33,"wind_dir":"NNE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":39,"cloud":19,"feelslike_c":-5,"feelslike_f":23,"windchill_c":-5,"windchill_f":23,"heatindex_c":-2.6,"heatindex_f":27.4,"dewpoint_c":-14.5,"dewpoint_f":5.9,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.5,"gust_kph":8.8,"uv":0,"air_quality":{"co":2097.85,"no2":58.25,"o3":0,"so2":140.45,"pm2_5":161.55,"pm10":164.25,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766358000,"time":"2025-12-22 07:00","temp_c":-2.7,"temp_f":27.2,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":31,"wind_dir":"NNE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":39,"cloud":16,"feelslike_c":-5,"feelslike_f":23,"windchill_c":-5,"windchill_f":23,"heatindex_c":-2.7,"heatindex_f":27.2,"dewpoint_c":-14.5,"dewpoint_f":5.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.2,"gust_kph":8.4,"uv":0,"air_quality":{"co":2060.85,"no2":58.05,"o3":0,"so2":131.05,"pm2_5":149.85,"pm10":152.65,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766361600,"time":"2025-12-22 08:00","temp_c":-2.7,"temp_f":27.2,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.4,"wind_kph":5.4,"wind_degree":42,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":39,"cloud":4,"feelslike_c":-4.8,"feelslike_f":23.3,"windchill_c":-4.8,"windchill_f":23.3,"heatindex_c":-2.7,"heatindex_f":27.2,"dewpoint_c":-14.6,"dewpoint_f":5.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.9,"gust_kph":7.8,"uv":0,"air_quality":{"co":1969.85,"no2":57.45,"o3":0,"so2":120.75,"pm2_5":134.05,"pm10":137.25,"us-epa-index":4,"gb-defra-index":10},"short_rad":0.7,"diff_rad":0.33,"dni":0,"gti":0.31},{"time_epoch":1766365200,"time":"2025-12-22 09:00","temp_c":-0.6,"temp_f":30.9,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":42,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":64,"cloud":0,"feelslike_c":-4.3,"feelslike_f":24.3,"windchill_c":-4.3,"windchill_f":24.3,"heatindex_c":-2.1,"heatindex_f":28.3,"dewpoint_c":-14.6,"dewpoint_f":5.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.8,"gust_kph":7.7,"uv":0.2,"air_quality":{"co":1852.85,"no2":56.75,"o3":0,"so2":108.75,"pm2_5":126.15,"pm10":129.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":93.32,"diff_rad":33.82,"dni":0,"gti":32.06},{"time_epoch":1766368800,"time":"2025-12-22 10:00","temp_c":-1.3,"temp_f":29.6,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.4,"wind_kph":5.4,"wind_degree":49,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.36,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":36,"cloud":7,"feelslike_c":-3.3,"feelslike_f":26.1,"windchill_c":-3.3,"windchill_f":26.1,"heatindex_c":-1.3,"heatindex_f":29.6,"dewpoint_c":-14.4,"dewpoint_f":6.1,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.2,"gust_kph":6.8,"uv":0.5,"air_quality":{"co":1847.85,"no2":55.95,"o3":0,"so2":100.85,"pm2_5":122.35,"pm10":126.55,"us-epa-index":4,"gb-defra-index":10},"short_rad":164.46,"diff_rad":48.61,"dni":0,"gti":46.78},{"time_epoch":1766372400,"time":"2025-12-22 11:00","temp_c":-0.7,"temp_f":30.7,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":41,"wind_dir":"NE","pressure_mb":1028,"pressure_in":30.35,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":35,"cloud":6,"feelslike_c":-2.8,"feelslike_f":27,"windchill_c":-2.8,"windchill_f":27,"heatindex_c":-0.7,"heatindex_f":30.7,"dewpoint_c":-14.2,"dewpoint_f":6.4,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.4,"gust_kph":7.1,"uv":0.8,"air_quality":{"co":2012.85,"no2":55.15,"o3":0,"so2":95.55,"pm2_5":118.45,"pm10":122.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":202.66,"diff_rad":73.82,"dni":0,"gti":69.94},{"time_epoch":1766376000,"time":"2025-12-22 12:00","temp_c":-0.1,"temp_f":31.8,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":2.5,"wind_kph":4,"wind_degree":30,"wind_dir":"NNE","pressure_mb":1027,"pressure_in":30.33,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":4,"feelslike_c":-1.2,"feelslike_f":29.9,"windchill_c":-1.2,"windchill_f":29.9,"heatindex_c":-0.1,"heatindex_f":31.8,"dewpoint_c":-14.1,"dewpoint_f":6.7,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3,"gust_kph":4.8,"uv":1,"air_quality":{"co":2289.85,"no2":54.25,"o3":0,"so2":94.35,"pm2_5":120.95,"pm10":125.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":245.36,"diff_rad":90.2,"dni":0,"gti":85.41},{"time_epoch":1766379600,"time":"2025-12-22 13:00","temp_c":0.2,"temp_f":32.3,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":2.2,"wind_kph":3.6,"wind_degree":46,"wind_dir":"NE","pressure_mb":1025,"pressure_in":30.28,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":4,"feelslike_c":-0.7,"feelslike_f":30.8,"windchill_c":-0.7,"windchill_f":30.8,"heatindex_c":0.2,"heatindex_f":32.3,"dewpoint_c":-13.8,"dewpoint_f":7.2,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.7,"gust_kph":4.4,"uv":0.9,"air_quality":{"co":2646.85,"no2":53.35,"o3":0,"so2":107.15,"pm2_5":131.25,"pm10":135.85,"us-epa-index":4,"gb-defra-index":10},"short_rad":271.56,"diff_rad":100.68,"dni":0,"gti":95.27},{"time_epoch":1766383200,"time":"2025-12-22 14:00","temp_c":0.5,"temp_f":32.8,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":1.6,"wind_kph":2.5,"wind_degree":63,"wind_dir":"ENE","pressure_mb":1025,"pressure_in":30.26,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":34,"cloud":10,"feelslike_c":0.5,"feelslike_f":32.8,"windchill_c":0.5,"windchill_f":32.8,"heatindex_c":0.5,"heatindex_f":32.8,"dewpoint_c":-13.4,"dewpoint_f":7.9,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":1.9,"gust_kph":3.1,"uv":0.7,"air_quality":{"co":3202.85,"no2":52.35,"o3":0,"so2":150.65,"pm2_5":150.35,"pm10":154.95,"us-epa-index":4,"gb-defra-index":10},"short_rad":291.04,"diff_rad":97.82,"dni":0,"gti":93.2},{"time_epoch":1766386800,"time":"2025-12-22 15:00","temp_c":0.4,"temp_f":32.6,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":0.4,"wind_kph":0.7,"wind_degree":62,"wind_dir":"ENE","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":35,"cloud":8,"feelslike_c":0.4,"feelslike_f":32.6,"windchill_c":0.4,"windchill_f":32.6,"heatindex_c":0.4,"heatindex_f":32.6,"dewpoint_c":-13.2,"dewpoint_f":8.3,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":0.6,"gust_kph":0.9,"uv":0.3,"air_quality":{"co":3837.85,"no2":51.35,"o3":0,"so2":208.15,"pm2_5":214.55,"pm10":219.25,"us-epa-index":5,"gb-defra-index":10},"short_rad":204.96,"diff_rad":97.82,"dni":0,"gti":91.19},{"time_epoch":1766390400,"time":"2025-12-22 16:00","temp_c":0.2,"temp_f":32.3,"is_day":1,"condition":{"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000},"wind_mph":1.8,"wind_kph":2.9,"wind_degree":359,"wind_dir":"N","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":36,"cloud":9,"feelslike_c":0.2,"feelslike_f":32.3,"windchill_c":0.2,"windchill_f":32.3,"heatindex_c":0.2,"heatindex_f":32.3,"dewpoint_c":-13.1,"dewpoint_f":8.4,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.5,"gust_kph":4,"uv":0.1,"air_quality":{"co":4158.85,"no2":50.45,"o3":0,"so2":240.05,"pm2_5":304.45,"pm10":309.15,"us-epa-index":6,"gb-defra-index":10},"short_rad":176.42,"diff_rad":76.54,"dni":1663.51,"gti":0},{"time_epoch":1766394000,"time":"2025-12-22 17:00","temp_c":-0.4,"temp_f":31.3,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.7,"wind_kph":4.3,"wind_degree":355,"wind_dir":"N","pressure_mb":1025,"pressure_in":30.26,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":12,"feelslike_c":-1.7,"feelslike_f":29,"windchill_c":-1.7,"windchill_f":29,"heatindex_c":-0.4,"heatindex_f":31.3,"dewpoint_c":-13.4,"dewpoint_f":8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4,"gust_kph":6.5,"uv":0,"air_quality":{"co":3902.85,"no2":49.45,"o3":2,"so2":221.85,"pm2_5":310.95,"pm10":313.75,"us-epa-index":6,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766397600,"time":"2025-12-22 18:00","temp_c":-0.6,"temp_f":30.9,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":3.6,"wind_kph":5.8,"wind_degree":346,"wind_dir":"NNW","pressure_mb":1025,"pressure_in":30.26,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":8,"feelslike_c":-2.6,"feelslike_f":27.3,"windchill_c":-2.6,"windchill_f":27.3,"heatindex_c":-0.6,"heatindex_f":30.9,"dewpoint_c":-13.5,"dewpoint_f":7.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":5.5,"gust_kph":8.9,"uv":0,"air_quality":{"co":3331.85,"no2":48.55,"o3":5,"so2":177.95,"pm2_5":167.05,"pm10":171.75,"us-epa-index":5,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766401200,"time":"2025-12-22 19:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.5,"wind_kph":4,"wind_degree":349,"wind_dir":"N","pressure_mb":1024,"pressure_in":30.25,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":14,"feelslike_c":-1.9,"feelslike_f":28.5,"windchill_c":-1.9,"windchill_f":28.5,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.6,"dewpoint_f":7.6,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3.9,"gust_kph":6.2,"uv":0,"air_quality":{"co":2839.85,"no2":48.45,"o3":8,"so2":142.15,"pm2_5":121.45,"pm10":126.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766404800,"time":"2025-12-22 20:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":2.5,"wind_kph":4,"wind_degree":350,"wind_dir":"N","pressure_mb":1024,"pressure_in":30.25,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":14,"feelslike_c":-1.9,"feelslike_f":28.6,"windchill_c":-1.9,"windchill_f":28.6,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.4,"dewpoint_f":7.8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3.8,"gust_kph":6.2,"uv":0,"air_quality":{"co":2435.85,"no2":49.25,"o3":10,"so2":124.85,"pm2_5":108.45,"pm10":112.35,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766408400,"time":"2025-12-22 21:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Clear ","icon":"//cdn.weatherapi.com/weather/64x64/night/113.png","code":1000},"wind_mph":1.6,"wind_kph":2.5,"wind_degree":18,"wind_dir":"NNE","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":37,"cloud":19,"feelslike_c":-0.8,"feelslike_f":30.7,"windchill_c":-0.8,"windchill_f":30.7,"heatindex_c":-0.8,"heatindex_f":30.7,"dewpoint_c":-13.4,"dewpoint_f":7.9,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":2.4,"gust_kph":3.9,"uv":0,"air_quality":{"co":2109.85,"no2":50.85,"o3":12,"so2":115.65,"pm2_5":95.65,"pm10":99.15,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766412000,"time":"2025-12-22 22:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Partly Cloudy ","icon":"//cdn.weatherapi.com/weather/64x64/night/116.png","code":1003},"wind_mph":2.9,"wind_kph":4.7,"wind_degree":24,"wind_dir":"NNE","pressure_mb":1024,"pressure_in":30.25,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":38,"cloud":36,"feelslike_c":-2.3,"feelslike_f":27.8,"windchill_c":-2.3,"windchill_f":27.8,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.3,"dewpoint_f":8,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":4.5,"gust_kph":7.2,"uv":0,"air_quality":{"co":2225.85,"no2":53.35,"o3":12,"so2":116.45,"pm2_5":87.25,"pm10":90.75,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0},{"time_epoch":1766415600,"time":"2025-12-22 23:00","temp_c":-0.8,"temp_f":30.6,"is_day":0,"condition":{"text":"Overcast ","icon":"//cdn.weatherapi.com/weather/64x64/night/122.png","code":1009},"wind_mph":2.2,"wind_kph":3.6,"wind_degree":17,"wind_dir":"NNE","pressure_mb":1024,"pressure_in":30.24,"precip_mm":0,"precip_in":0,"snow_cm":0,"humidity":38,"cloud":92,"feelslike_c":-1.7,"feelslike_f":28.9,"windchill_c":-1.7,"windchill_f":28.9,"heatindex_c":-0.8,"heatindex_f":30.6,"dewpoint_c":-13.2,"dewpoint_f":8.3,"will_it_rain":0,"chance_of_rain":0,"will_it_snow":0,"chance_of_snow":0,"vis_km":10,"vis_miles":6,"gust_mph":3.5,"gust_kph":5.6,"uv":0,"air_quality":{"co":2994.85,"no2":57.85,"o3":9,"so2":126.95,"pm2_5":84.65,"pm10":87.95,"us-epa-index":4,"gb-defra-index":10},"short_rad":0,"diff_rad":0,"dni":0,"gti":0}]
             */

            private String date;
            private int date_epoch;
            private DayBean day;
            private AstroBean astro;
            private List<HourBean> hour;

            @Data
            public static class DayBean implements Serializable {
                /**
                 * maxtemp_c : 0.5
                 * maxtemp_f : 32.8
                 * mintemp_c : -2.7
                 * mintemp_f : 27.2
                 * avgtemp_c : -1.1
                 * avgtemp_f : 30.1
                 * maxwind_mph : 4.5
                 * maxwind_kph : 7.2
                 * totalprecip_mm : 0
                 * totalprecip_in : 0
                 * totalsnow_cm : 0
                 * avgvis_km : 10
                 * avgvis_miles : 6
                 * avghumidity : 36
                 * daily_will_it_rain : 0
                 * daily_chance_of_rain : 0
                 * daily_will_it_snow : 0
                 * daily_chance_of_snow : 0
                 * condition : {"text":"Sunny","icon":"//cdn.weatherapi.com/weather/64x64/day/113.png","code":1000}
                 * uv : 0.2
                 * air_quality : {"co":5527.086000000002,"no2":57.778,"o3":2.52,"so2":231.99399999999997,"pm2_5":251.27800000000002,"pm10":254.47800000000004,"us-epa-index":6,"gb-defra-index":10}
                 */

                private double maxtemp_c;
                private double maxtemp_f;
                private double mintemp_c;
                private double mintemp_f;
                private double avgtemp_c;
                private double avgtemp_f;
                private double maxwind_mph;
                private double maxwind_kph;
                private double totalprecip_mm;
                private double totalprecip_in;
                private double totalsnow_cm;
                private double avgvis_km;
                private double avgvis_miles;
                private double avghumidity;
                private double daily_will_it_rain;
                private double daily_chance_of_rain;
                private double daily_will_it_snow;
                private double daily_chance_of_snow;
                private ConditionBeanX condition;
                private double uv;
                private AirQualityBeanX air_quality;

                @Data
                public static class ConditionBeanX implements Serializable {
                    /**
                     * text : Sunny
                     * icon : //cdn.weatherapi.com/weather/64x64/day/113.png
                     * code : 1000
                     */

                    private String text;
                    private String icon;
                    private int code;
                }

                @Data
                public static class AirQualityBeanX implements Serializable {
                    /**
                     * co : 5527.086000000002
                     * no2 : 57.778
                     * o3 : 2.52
                     * so2 : 231.99399999999997
                     * pm2_5 : 251.27800000000002
                     * pm10 : 254.47800000000004
                     * us-epa-index : 6
                     * gb-defra-index : 10
                     */

                    private double co;
                    private double no2;
                    private double o3;
                    private double so2;
                    private double pm2_5;
                    private double pm10;
                    @SerializedName("us-epa-index")
                    private int usEpaIndex;
                    @SerializedName("gb-defra-index")
                    private int gbDefraIndex;
                }
            }

            @Data
            public static class AstroBean implements Serializable {
                /**
                 * sunrise : 07:34 AM
                 * sunset : 04:53 PM
                 * moonrise : 09:26 AM
                 * moonset : 06:41 PM
                 * moon_phase : Waxing Crescent
                 * moon_illumination : 4
                 * is_moon_up : 0
                 * is_sun_up : 0
                 */

                private String sunrise;
                private String sunset;
                private String moonrise;
                private String moonset;
                private String moon_phase;
                private int moon_illumination;
                private int is_moon_up;
                private int is_sun_up;
            }

            @Data
            public static class HourBean implements Serializable {
                /**
                 * time_epoch : 1766332800
                 * time : 2025-12-22 00:00
                 * temp_c : -1
                 * temp_f : 30.1
                 * is_day : 0
                 * condition : {"text":"Partly Cloudy ","icon":"//cdn.weatherapi.com/weather/64x64/night/116.png","code":1003}
                 * wind_mph : 1.6
                 * wind_kph : 2.5
                 * wind_degree : 130
                 * wind_dir : SE
                 * pressure_mb : 1029
                 * pressure_in : 30.39
                 * precip_mm : 0
                 * precip_in : 0
                 * snow_cm : 0
                 * humidity : 32
                 * cloud : 25
                 * feelslike_c : -1.1
                 * feelslike_f : 30.1
                 * windchill_c : -1.1
                 * windchill_f : 30.1
                 * heatindex_c : -1.1
                 * heatindex_f : 30.1
                 * dewpoint_c : -15.7
                 * dewpoint_f : 3.8
                 * will_it_rain : 0
                 * chance_of_rain : 0
                 * will_it_snow : 0
                 * chance_of_snow : 0
                 * vis_km : 10
                 * vis_miles : 6
                 * gust_mph : 2.3
                 * gust_kph : 3.8
                 * uv : 0
                 * air_quality : {"co":1050.85,"no2":55.25,"o3":7,"so2":65.65,"pm2_5":53.75,"pm10":56.25,"us-epa-index":3,"gb-defra-index":6}
                 * short_rad : 0
                 * diff_rad : 0
                 * dni : 0
                 * gti : 0
                 */

                private int time_epoch;
                private String time;
                private double temp_c;
                private double temp_f;
                private int is_day;
                private ConditionBeanXX condition;
                private double wind_mph;
                private double wind_kph;
                private int wind_degree;
                private String wind_dir;
                private int pressure_mb;
                private double pressure_in;
                private double precip_mm;
                private double precip_in;
                private double snow_cm;
                private double humidity;
                private int cloud;
                private double feelslike_c;
                private double feelslike_f;
                private double windchill_c;
                private double windchill_f;
                private double heatindex_c;
                private double heatindex_f;
                private double dewpoint_c;
                private double dewpoint_f;
                private int will_it_rain;
                private int chance_of_rain;
                private int will_it_snow;
                private int chance_of_snow;
                private int vis_km;
                private int vis_miles;
                private double gust_mph;
                private double gust_kph;
                private double uv;
                private AirQualityBeanXX air_quality;
                private double short_rad;
                private double diff_rad;
                private double dni;
                private double gti;

                @Data
                public static class ConditionBeanXX implements Serializable {
                    /**
                     * text : Partly Cloudy
                     * icon : //cdn.weatherapi.com/weather/64x64/night/116.png
                     * code : 1003
                     */

                    private String text;
                    private String icon;
                    private int code;
                }

                @Data
                public static class AirQualityBeanXX implements Serializable {
                    /**
                     * co : 1050.85
                     * no2 : 55.25
                     * o3 : 7
                     * so2 : 65.65
                     * pm2_5 : 53.75
                     * pm10 : 56.25
                     * us-epa-index : 3
                     * gb-defra-index : 6
                     */

                    private double co;
                    private double no2;
                    private int o3;
                    private double so2;
                    private double pm2_5;
                    private double pm10;
                    @SerializedName("us-epa-index")
                    private int usEpaIndex;
                    @SerializedName("gb-defra-index")
                    private int gbDefraIndex;
                }
            }
        }
    }

    @Data
    public static class AlertsBean implements Serializable {
        private List<?> alert;
    }
}
