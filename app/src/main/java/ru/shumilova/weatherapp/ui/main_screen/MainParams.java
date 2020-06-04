package ru.shumilova.weatherapp.ui.main_screen;

import java.io.Serializable;

public class MainParams implements Serializable {
    private String cityName;

    public MainParams(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
