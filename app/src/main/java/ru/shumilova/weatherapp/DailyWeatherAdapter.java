package ru.shumilova.weatherapp;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.shumilova.weatherapp.domain.WeatherData;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {

    private List<WeatherData> dailyWeatherDataSource;

    public DailyWeatherAdapter(List<WeatherData> dailyWeatherDataSource) {
        this.dailyWeatherDataSource = dailyWeatherDataSource;
    }

    @NonNull
    @Override
    public DailyWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherAdapter.ViewHolder holder, int position) {
        holder.bind(dailyWeatherDataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dailyWeatherDataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDailyWeatherDate;
        private TextView tvDayOfWeekWeather;
        private TextView tvDailyWeatherTemperature;
        private AppCompatImageView ivWindy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDailyWeatherDate = itemView.findViewById(R.id.tv_daily_weather_date);
            tvDayOfWeekWeather = itemView.findViewById(R.id.tv_day_of_week_weather);
            tvDailyWeatherTemperature = itemView.findViewById(R.id.tv_daily_weather_t);
            ivWindy = itemView.findViewById(R.id.iv_windy);
        }

        public void bind(WeatherData weatherData) {
            bindTemperature(weatherData);
            bindIcon(weatherData);
            bindDate(weatherData);
        }

        private void bindTemperature(WeatherData weatherData) {
            String temperature;

            if (weatherData.getTemperature() > 0) {
                temperature = "+" + weatherData.getTemperature() + "°";
            } else if (weatherData.getTemperature() < 0) {
                temperature = weatherData.getTemperature() + "°";
            } else {
                temperature = "0°";
            }

            tvDailyWeatherTemperature.setText(temperature);
        }

        private void bindIcon(WeatherData weatherData) {
            int icon;
            switch (weatherData.getWeatherCondition()) {
                case RAINY:
                    icon = R.drawable.ic_summer_rain;
                    break;
                case SUNNY:
                    icon = R.drawable.ic_sunny;
                    break;
                case CLOUDY:
                    icon = R.drawable.ic_clouds;
                    break;
                default:
                    icon = R.drawable.ic_stars_2;
                    break;
            }

            ivWindy.setImageResource(icon);
        }

        private void bindDate(WeatherData weatherData) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String date = simpleDateFormat.format(weatherData.getDate());
            simpleDateFormat.applyPattern("EEEE");
            String dayOfWeek = simpleDateFormat.format(weatherData.getDate());
            tvDayOfWeekWeather.setText(dayOfWeek);
            tvDailyWeatherDate.setText(date);
        }

    }
}
