package ru.shumilova.weatherapp.data;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ru.shumilova.weatherapp.data.models.ErrorType;
import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.data.models.WeatherWeeklyResponse;
import ru.shumilova.weatherapp.domain.WeatherState;


public class WeatherRepository {
    private Gson gson = new Gson();
    private MutableLiveData<WeatherState> weatherMutableData = new MutableLiveData<>();

    public LiveData<WeatherState> getWeatherData() {
        return weatherMutableData;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCityWeather(final String cityName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric" + "&lang=" + Locale.getDefault().getLanguage();

                HttpURLConnection urlConnection = null;
                URL url;

                try {
                    url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("x-api-key", "cfdb77e65d320ca11996d5fee7f947bd");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = getLines(in);
                    WeatherResponse weatherResponse = gson.fromJson(result, WeatherResponse.class);
                    weatherMutableData.postValue(new WeatherState(weatherResponse, null, null));

                } catch (IOException e) {
                    e.printStackTrace();
                    ErrorType errorType;
                    if (e instanceof FileNotFoundException) {
                        errorType = ErrorType.CITY_NOT_FOUND;
                    } else {
                        errorType = ErrorType.CONNECTION_ERROR;
                    }
                    weatherMutableData.postValue(new WeatherState(null,null, errorType));
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }

        }).start();


    }

    public void getWeatherWeek (final String cityName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = "https://api.openweathermap.org/data/2.5/forecast/?q=" + cityName + "&units=metric" + "&lang=" + Locale.getDefault().getLanguage();

                HttpURLConnection urlConnection = null;
                URL url;

                try {
                    url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("x-api-key", "cfdb77e65d320ca11996d5fee7f947bd");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String result = getLines(in);
                    WeatherWeeklyResponse weatherWeeklyResponse = gson.fromJson(result, WeatherWeeklyResponse.class);
                    List<WeatherResponse> filteredList = filterDailyWeather(weatherWeeklyResponse);
                    weatherWeeklyResponse.setList(filteredList);
                    weatherMutableData.postValue(new WeatherState(null, weatherWeeklyResponse, null));

                } catch (IOException e) {
                    e.printStackTrace();
                    ErrorType errorType;
                    if (e instanceof FileNotFoundException) {
                        errorType = ErrorType.CITY_NOT_FOUND;
                    } else {
                        errorType = ErrorType.CONNECTION_ERROR;
                    }
                    weatherMutableData.postValue(new WeatherState(null,null, errorType));
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }

        }).start();


    }

    private List<WeatherResponse> filterDailyWeather(WeatherWeeklyResponse weatherWeeklyResponse) {
        List<WeatherResponse> filteredList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", Locale.getDefault());
        for (int i = 0; i < weatherWeeklyResponse.getList().size(); i++) {
            WeatherResponse weatherResponse = weatherWeeklyResponse.getList().get(i);
            String time = simpleDateFormat.format(weatherResponse.getDt() * 1000);
            if (time.equals("15")) {
                filteredList.add(weatherResponse);
            }
        }
        return filteredList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }


}
