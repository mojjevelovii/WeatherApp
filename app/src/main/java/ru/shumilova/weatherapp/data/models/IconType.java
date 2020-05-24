package ru.shumilova.weatherapp.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ru.shumilova.weatherapp.R;

public enum IconType implements Serializable {

    @SerializedName("01d")
    SUNNY_DAY(R.drawable.ic_sunny),
    @SerializedName("01n")
    MOON_NIGHT(R.drawable.ic_moon),

    @SerializedName(value = "02d", alternate = {"03d", "04d"})
    CLOUDY_DAY(R.drawable.ic_clouds_1),
    @SerializedName(value = "02n", alternate = {"03n", "04n"})
    CLOUDY_NIGHT(R.drawable.ic_cloudy_night),

    @SerializedName(value = "09d", alternate = {"10d"})
    RAINY_DAY(R.drawable.ic_morning_rain),
    @SerializedName(value = "09n", alternate = {"10n"})
    RAINY_NIGHT(R.drawable.ic_night_rain),

    @SerializedName("11d")
    STORM_DAY(R.drawable.ic_storm_morning),
    @SerializedName("11n")
    STORM_NIGHT(R.drawable.ic_storm_night),

    @SerializedName("13d")
    SNOWING_DAY(R.drawable.ic_snowing_day),
    @SerializedName("13n")
    SNOWING_NIGHT(R.drawable.ic_snow_night),

    @SerializedName(value = "50d", alternate = {"50n"})
    MIST(R.drawable.ic_mist);

    public int getIconRes() {
        return iconRes;
    }

    private int iconRes;

    IconType(int iconRes) {
        this.iconRes = iconRes;
    }
}
