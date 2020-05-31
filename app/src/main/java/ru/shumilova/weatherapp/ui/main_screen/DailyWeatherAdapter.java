package ru.shumilova.weatherapp.ui.main_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.data.models.WeatherResponse;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {

    private List<WeatherResponse> dailyWeatherDataSource;

    public void setDailyWeatherDataSource(List<WeatherResponse> dailyWeatherDataSource) {
        this.dailyWeatherDataSource = dailyWeatherDataSource;
        notifyDataSetChanged();
    }

    public DailyWeatherAdapter() {
        this.dailyWeatherDataSource = new ArrayList<WeatherResponse>();
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
        private AppCompatImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDailyWeatherDate = itemView.findViewById(R.id.tv_daily_weather_date);
            tvDayOfWeekWeather = itemView.findViewById(R.id.tv_day_of_week_weather);
            tvDailyWeatherTemperature = itemView.findViewById(R.id.tv_daily_weather_t);
            weatherIcon = itemView.findViewById(R.id.iv_windy);
        }

        public void bind(WeatherResponse weatherData) {
            bindTemperature(weatherData);
            bindIcon(weatherData);
            bindDate(weatherData);
        }

        private void bindTemperature(WeatherResponse weatherData) {
            String temperature;

            int intTemperature = (int) weatherData.getMain().getTemp();
            if (intTemperature > 0) {
                temperature = "+" + intTemperature + "°";
            } else if (intTemperature < 0) {
                temperature = intTemperature + "°";
            } else {
                temperature = "0°";
            }

            tvDailyWeatherTemperature.setText(temperature);
        }

        private void bindIcon(WeatherResponse weatherData) {
            weatherIcon.setImageResource(weatherData.getWeather().get(0).getIcon().getIconRes());
        }

        private void bindDate(WeatherResponse weatherData) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            long time = weatherData.getDt() * 1000;
            String date = simpleDateFormat.format(time);
            simpleDateFormat.applyPattern("EEEE");
            String dayOfWeek = simpleDateFormat.format(time);
            tvDayOfWeekWeather.setText(dayOfWeek);
            tvDailyWeatherDate.setText(date);
        }

    }
}
