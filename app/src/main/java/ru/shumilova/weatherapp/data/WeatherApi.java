package ru.shumilova.weatherapp.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.data.models.WeatherWeeklyResponse;

public interface WeatherApi {
    @GET("weather")
    Call<WeatherResponse> getCityWeather(@Query("q") String cityName, @Query("units") String units,
                                         @Query("lang") String language, @Header("x-api-key") String token);

    @GET("forecast")
    Call<WeatherWeeklyResponse> getWeatherWeek(@Query("q") String cityName, @Query("units") String units,
                                               @Query("lang") String language, @Header("x-api-key") String token);

}
