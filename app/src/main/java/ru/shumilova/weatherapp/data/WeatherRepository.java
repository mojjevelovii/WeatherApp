package ru.shumilova.weatherapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.shumilova.weatherapp.BuildConfig;
import ru.shumilova.weatherapp.data.models.ErrorType;
import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.data.models.WeatherWeeklyResponse;
import ru.shumilova.weatherapp.domain.WeatherState;


public class WeatherRepository {
    private MutableLiveData<WeatherState> weatherMutableData = new MutableLiveData<>();
    private WeatherApi weatherApi;
    private static final long RETRY_TIME_OUT = 3000L;

    public LiveData<WeatherState> getWeatherData() {
        return weatherMutableData;
    }

    public WeatherRepository() {
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(WeatherApi.class);
    }

    public void getCityWeather(final String cityName) {
        weatherApi.getCityWeather(cityName, "metric", Locale.getDefault().getLanguage(),
                BuildConfig.WEATHER_API).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse result = response.body();
                    weatherMutableData.postValue(new WeatherState(result, null, null));
                } else if (response.errorBody() != null) {
                    errorHandle(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherMutableData.postValue(new WeatherState(null, null, ErrorType.CONNECTION_ERROR));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(RETRY_TIME_OUT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getCityWeather(cityName);
                    }
                }).start();
            }
        });
    }

    private void errorHandle(ResponseBody responseBody) {
        try {
            if (responseBody.string().contains("city not found")) {
                weatherMutableData.postValue(new WeatherState(null, null, ErrorType.CITY_NOT_FOUND));
            } else {
                weatherMutableData.postValue(new WeatherState(null, null, ErrorType.UNKNOWN_ERROR));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getWeatherWeek(final String cityName) {
        weatherApi.getWeatherWeek(cityName, "metric", Locale.getDefault().getLanguage(),
                BuildConfig.WEATHER_API).enqueue(new Callback<WeatherWeeklyResponse>() {
            @Override
            public void onResponse(Call<WeatherWeeklyResponse> call, Response<WeatherWeeklyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherWeeklyResponse result = response.body();
                    List<WeatherResponse> filteredList = filterDailyWeather(result);
                    result.setList(filteredList);
                    weatherMutableData.postValue(new WeatherState(null, result, null));
                } else if (response.errorBody() != null) {
                    errorHandle(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<WeatherWeeklyResponse> call, Throwable t) {
                weatherMutableData.postValue(new WeatherState(null, null, ErrorType.CONNECTION_ERROR));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(RETRY_TIME_OUT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getWeatherWeek(cityName);
                    }
                }).start();
            }
        });
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
}
