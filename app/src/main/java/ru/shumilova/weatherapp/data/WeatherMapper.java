package ru.shumilova.weatherapp.data;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.data.models.WeatherWeeklyResponse;

public class WeatherMapper {
    private Gson gson = new Gson();

    public WeatherResponse mapCityWeather(String result) {
        return gson.fromJson(result, WeatherResponse.class);
    }

    public WeatherWeeklyResponse mapWeeklyWeather(String result) {
        WeatherWeeklyResponse weatherWeeklyResponse = gson.fromJson(result, WeatherWeeklyResponse.class);
        List<WeatherResponse> filteredList = filterDailyWeather(weatherWeeklyResponse);
        weatherWeeklyResponse.setList(filteredList);
        return weatherWeeklyResponse;
    }

    private List<WeatherResponse> filterDailyWeather(WeatherWeeklyResponse weatherWeeklyResponse) {
        List<WeatherResponse> filteredList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.getDefault());
        for (int i = 0; i < weatherWeeklyResponse.getList().size(); i++) {
            WeatherResponse weatherResponse = weatherWeeklyResponse.getList().get(i);
            String time = simpleDateFormat.format(weatherResponse.getDt() * 1000);
            if (time.equals("15")) {
                filteredList.add(weatherResponse);
            }
        }
        return filteredList;
    }
}
