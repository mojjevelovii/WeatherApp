package ru.shumilova.weatherapp.ui.main_screen;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MainParams implements Serializable {
    private String cityName;
    private LatLng latLngPosition;

    public MainParams(String cityName, LatLng latLngPosition) {
        this.cityName = cityName;
        this.latLngPosition = latLngPosition;
    }

    public LatLng getLatLngPosition() {
        return latLngPosition;
    }

    public String getCityName() {
        return cityName;
    }
}
