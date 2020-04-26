package ru.shumilova.weatherapp.domain;

import java.util.Date;

public class WeatherData {
    private int temperature;
    private WeatherCondition weatherCondition;
    private int temperatureFeelsLike;
    private Date date;

    public WeatherData(int temperature, WeatherCondition weatherCondition, int temperatureFeelsLike, Date date) {
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.temperatureFeelsLike = temperatureFeelsLike;
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    public int getTemperatureFeelsLike() {
        return temperatureFeelsLike;
    }

    public Date getDate() {
        return date;
    }
}
