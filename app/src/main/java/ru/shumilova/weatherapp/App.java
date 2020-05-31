package ru.shumilova.weatherapp;

import android.app.Application;

import androidx.room.Room;

import ru.shumilova.weatherapp.data.data_base.WeatherDao;
import ru.shumilova.weatherapp.data.data_base.WeatherDataBase;

public class App extends Application {
    private static App instance;
    private WeatherDataBase weatherDataBase;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        weatherDataBase = Room.databaseBuilder(
                getApplicationContext(),
                WeatherDataBase.class,
                "weather_database")
                .build();
    }

    public WeatherDao getWeatherDao() {
        return weatherDataBase.getWeatherDao();
    }
}
