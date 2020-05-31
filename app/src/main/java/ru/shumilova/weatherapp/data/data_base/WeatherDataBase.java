package ru.shumilova.weatherapp.data.data_base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.shumilova.weatherapp.data.data_base.model.CityWeatherHistoryModel;

@Database(entities = {CityWeatherHistoryModel.class}, version = 1)
public abstract class WeatherDataBase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}
