package ru.shumilova.weatherapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalRepository {
    private SharedPreferences sharedPreferences;
    private Set<String> citySet = new HashSet<>();
    private static final String LOCAL_REPOSITORY = "LOCAL_REPOSITORY";
    private static final String CITIES = "CITIES";

    public LocalRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(LOCAL_REPOSITORY, Context.MODE_PRIVATE);
    }

    public void saveCity(String cityName) {
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        citySet.add(cityName);
        StringBuilder sb = new StringBuilder();
        for (Object o : citySet.toArray()) {
            sb.append((String) o).append(";");
        }
        spEditor.putString(CITIES, sb.toString());
        spEditor.apply();
    }

    public List<String> loadCityList() {
        String cities = sharedPreferences.getString(CITIES, null);
        if (cities != null) {
            List<String> cityList = Arrays.asList(cities.split(";"));
            citySet = new HashSet<>(cityList);
            return cityList;
        } else {
            citySet = new HashSet<>();
            return new ArrayList<>();
        }
    }
}
