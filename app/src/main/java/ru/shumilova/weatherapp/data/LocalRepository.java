package ru.shumilova.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.shumilova.weatherapp.data.models.WeatherResponse;

public class LocalRepository {
    private SharedPreferences sharedPreferences;
    private Map<String, WeatherResponse> cityMap = new HashMap<>();
    private static final String SELECTED_CITY = "SELECTED_CITY";
    private static final String LOCAL_REPOSITORY = "LOCAL_REPOSITORY";
    private static final String CITIES = "CITIES";
    private static final String DARK_THEME = "DARK_THEME";

    private Gson gson = new Gson();


    public LocalRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(LOCAL_REPOSITORY, Context.MODE_PRIVATE);
        restoreCityHistory();
    }

    private void restoreCityHistory() {
        String cities = sharedPreferences.getString(CITIES, null);
        if (cities != null) {
            Type cityMapType = new TypeToken<Map<String, WeatherResponse>>() {
            }.getType();
            cityMap = gson.fromJson(cities, cityMapType);
        } else {
            cityMap = new HashMap<>();
        }
    }

    public void saveCity(WeatherResponse weatherResponse) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        cityMap.put(weatherResponse.getName(), weatherResponse);
        String json = gson.toJson(cityMap);

        spEditor.putString(CITIES, json);
        spEditor.apply();
    }

    public List<WeatherResponse> loadCityList() {
        List<WeatherResponse> cityList = new ArrayList<>();
        for (Map.Entry<String, WeatherResponse> e : cityMap.entrySet()) {
            cityList.add(e.getValue());
        }
        return cityList;
    }

    public boolean isDarkTheme() {
        return sharedPreferences.getBoolean(DARK_THEME, false);
    }

    public void saveThemeCondition(Boolean condition) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean(DARK_THEME, condition);
        spEditor.apply();
    }

    public void saveSelectedCity(String cityName) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putString(SELECTED_CITY, cityName).apply();
    }

    public String getSelectedCity() {
        return sharedPreferences.getString(SELECTED_CITY, "Moscow,RU");
    }
}
