package ru.shumilova.weatherapp.data;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import ru.shumilova.weatherapp.domain.WeatherState;

public class WeatherService extends Service {
    private final IBinder iBinder = new ServiceBinder();
    public static final String BROADCAST_ACTION_WEATHER = "ru.shumilova.weatherapp.data.service.weather";
    public static final String EXTRA_RESULT = "ru.shumilova.weatherapp.data.service.weather.result";
    private WeatherRepository weatherRepository = new WeatherRepository();
    private Observer<WeatherState> observer = new Observer<WeatherState>() {
        @Override
        public void onChanged(WeatherState weatherState) {
            sendWeatherState(weatherState);
        }
    };

    private void sendWeatherState(WeatherState weatherState) {
        Intent broadcastIntent = new Intent(BROADCAST_ACTION_WEATHER);
        broadcastIntent.putExtra(EXTRA_RESULT, weatherState);
        sendBroadcast(broadcastIntent);
    }

    public void getWeather(String city) {
        weatherRepository.getWeatherWeek(city);
        weatherRepository.getCityWeather(city);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        weatherRepository.getWeatherData().observeForever(observer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherRepository.getWeatherData().removeObserver(observer);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class ServiceBinder extends Binder {
        WeatherService getService() {
            return WeatherService.this;
        }

        public void getWeather(String city) {
            getService().getWeather(city);
        }
    }
}
