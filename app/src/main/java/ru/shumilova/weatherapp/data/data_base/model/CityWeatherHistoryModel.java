package ru.shumilova.weatherapp.data.data_base.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"cityName"}, unique = true)})
public class CityWeatherHistoryModel {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "cityName")
    private String cityName;
    @ColumnInfo(name = "temperature")
    private double temperature;
    @ColumnInfo(name = "weather")
    private String weather;
    @ColumnInfo(name = "date")
    private long date;

    public CityWeatherHistoryModel(String cityName, double temperature, String weather, long date) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.weather = weather;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
