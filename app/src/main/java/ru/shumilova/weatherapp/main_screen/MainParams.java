package ru.shumilova.weatherapp.main_screen;

import java.io.Serializable;

public class MainParams implements Serializable {
    private String cityName;

    public MainParams (String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
