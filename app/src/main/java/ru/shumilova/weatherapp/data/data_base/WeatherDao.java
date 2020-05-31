package ru.shumilova.weatherapp.data.data_base;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.shumilova.weatherapp.data.data_base.model.CityWeatherHistoryModel;

@Dao
public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(CityWeatherHistoryModel cityWeatherHistoryModel);

    @Update
    void updateWeather(CityWeatherHistoryModel cityWeatherHistoryModel);

    @Delete
    void deleteWeather(CityWeatherHistoryModel cityWeatherHistoryModel);

    @Query("DELETE FROM cityWeatherHistoryModel WHERE id = :id")
    void deleteWeatherById(long id);

    @Query("SELECT * FROM cityWeatherHistoryModel")
    LiveData<List<CityWeatherHistoryModel>> getAllWeather();

    @Query("SELECT * FROM cityWeatherHistoryModel WHERE id = :id")
    CityWeatherHistoryModel getWeatherById(long id);
}
