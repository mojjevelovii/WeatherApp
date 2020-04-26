package ru.shumilova.weatherapp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.shumilova.weatherapp.domain.WeatherCondition;
import ru.shumilova.weatherapp.domain.WeatherData;

public class DataHolder {
    private List<WeatherData> weatherDataList;

    public DataHolder() {
        this.weatherDataList = new ArrayList<>();

        Date date = new Date();
        date.setTime(1588013868000L);
        weatherDataList.add(new WeatherData(25, WeatherCondition.CLOUDY, 21, date));

        Date date1 = new Date();
        date1.setTime(1588100268000L);
        weatherDataList.add(new WeatherData(-2, WeatherCondition.RAINY, -8, date1));

        Date date2 = new Date();
        date2.setTime(1588186668000L);
        weatherDataList.add(new WeatherData(28, WeatherCondition.SUNNY, 34, date2));

        Date date3 = new Date();
        date3.setTime(1588273068000L);
        weatherDataList.add(new WeatherData(34, WeatherCondition.SUNNY, 36, date3));

        Date date4 = new Date();
        date4.setTime(1588359468000L);
        weatherDataList.add(new WeatherData(0, WeatherCondition.RAINY, -1, date4));

        Date date5 = new Date();
        date5.setTime(1588445868000L);
        weatherDataList.add(new WeatherData(22, WeatherCondition.CLOUDY, 26, date5));

        Date date6 = new Date();
        date6.setTime(1588532268000L);
        weatherDataList.add(new WeatherData(26, WeatherCondition.SUNNY, 29, date6));
    }

    public List<WeatherData> getWeatherDataList() {
        return weatherDataList;
    }
}
