package ru.shumilova.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.shumilova.weatherapp.App;
import ru.shumilova.weatherapp.data.data_base.model.CityWeatherHistoryModel;
import ru.shumilova.weatherapp.data.models.Main;
import ru.shumilova.weatherapp.data.models.WeatherResponse;

public class LocalRepository {
    private SharedPreferences sharedPreferences;
    private static final String SELECTED_CITY = "SELECTED_CITY";
    private static final String LOCAL_REPOSITORY = "LOCAL_REPOSITORY";
    private static final String DARK_THEME = "DARK_THEME";

    public LocalRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(LOCAL_REPOSITORY, Context.MODE_PRIVATE);
    }

    public void saveCity(WeatherResponse weatherResponse) {
        CityWeatherHistoryModel cityWeatherHistoryModel = new CityWeatherHistoryModel(
                weatherResponse.getName(),
                weatherResponse.getMain().getTemp(),
                weatherResponse.getWeather().get(0).getIcon().name(),
                weatherResponse.getDt());
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getWeatherDao().insertWeather(cityWeatherHistoryModel);
            }
        }).start();
    }

    public LiveData<List<CityWeatherHistoryModel>> loadCityList() {
        return App.getInstance().getWeatherDao().getAllWeather();
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
