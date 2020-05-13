package ru.shumilova.weatherapp.domain;

import ru.shumilova.weatherapp.data.models.ErrorType;
import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.data.models.WeatherWeeklyResponse;

public class WeatherState {
    private WeatherResponse weatherResponse;
    private WeatherWeeklyResponse weatherWeeklyResponse;
    private ErrorType errorType;

    public WeatherResponse getWeatherResponse() {
        return weatherResponse;
    }

    public WeatherWeeklyResponse getWeatherWeeklyResponse() {
        return weatherWeeklyResponse;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public WeatherState(WeatherResponse weatherResponse, WeatherWeeklyResponse weatherWeeklyResponse, ErrorType errorType) {
        this.weatherResponse = weatherResponse;
        this.weatherWeeklyResponse = weatherWeeklyResponse;
        this.errorType = errorType;
    }
}
