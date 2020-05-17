package ru.shumilova.weatherapp.city_selection_screen;

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
import java.util.function.Consumer;

import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.data.models.WeatherResponse;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private List<WeatherResponse> cityNames;
    private List<WeatherResponse> cachedCityNames;
    private OnCitySelectListener listener;

    public CityListAdapter(List<WeatherResponse> cityNames, OnCitySelectListener listener) {
        this.cityNames = cityNames;
        this.cachedCityNames = cityNames;
        this.listener = listener;
    }

    public void filterCity(final String query) {
        final List<WeatherResponse> filteredList = new ArrayList<>();
        cachedCityNames.forEach(new Consumer<WeatherResponse>() {
            @Override
            public void accept(WeatherResponse weatherResponse) {
                if (weatherResponse.getName().contains(query)) {
                    filteredList.add(weatherResponse);
                }
            }
        });
        cityNames = filteredList;
        notifyDataSetChanged();
    }

    public void resetFilter() {
        cityNames = cachedCityNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.citylist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cityNames.get(position));
    }

    @Override
    public int getItemCount() {
        return cityNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName;
        private TextView cityDate;
        private TextView cityTemperature;
        private AppCompatImageView weatherIcon;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.tv_city_name);
            cityDate = itemView.findViewById(R.id.tv_day_of_city_weather);
            cityTemperature = itemView.findViewById(R.id.tv_daily_weather_t);
            weatherIcon = itemView.findViewById(R.id.iv_windy);

            cityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCitySelect(cityName.getText().toString());
                }
            });
        }

        void bind(WeatherResponse weatherResponse) {
            bindCityName(weatherResponse.getName());
            bindDate(weatherResponse);
            bindTemperature(weatherResponse);
            bindIcon(weatherResponse);
        }

        private void bindCityName(String name) {
            this.cityName.setText(name);
        }

        private void bindDate(WeatherResponse weatherData) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            long time = weatherData.getDt() * 1000;
            String date = simpleDateFormat.format(time);
            cityDate.setText(date);
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

            cityTemperature.setText(temperature);
        }

        private void bindIcon(WeatherResponse weatherData) {
            weatherIcon.setImageResource(weatherData.getWeather().get(0).getIcon().getIconRes());
        }
    }

    public interface OnCitySelectListener {
        void onCitySelect(String cityName);
    }
}
