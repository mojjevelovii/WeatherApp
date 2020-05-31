package ru.shumilova.weatherapp.ui.city_selection_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.data.data_base.model.CityWeatherHistoryModel;
import ru.shumilova.weatherapp.data.models.IconType;
import ru.shumilova.weatherapp.data.models.WeatherResponse;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private List<CityWeatherHistoryModel> cityNames = new ArrayList<>();
    private List<CityWeatherHistoryModel> cachedCityNames = new ArrayList<>();
    private OnCitySelectListener listener;

    public CityListAdapter(LiveData<List<CityWeatherHistoryModel>> cityHistories, LifecycleOwner lifecycleOwner, OnCitySelectListener listener) {
        cityHistories.observe(lifecycleOwner, new Observer<List<CityWeatherHistoryModel>>() {
            @Override
            public void onChanged(List<CityWeatherHistoryModel> cityWeatherHistoryModels) {
                cityNames = cityWeatherHistoryModels;
                cachedCityNames = cityWeatherHistoryModels;
                notifyDataSetChanged();
            }
        });

        this.listener = listener;
    }

    public void filterCity(final String query) {
        final List<CityWeatherHistoryModel> filteredList = new ArrayList<>();
        cachedCityNames.forEach(new Consumer<CityWeatherHistoryModel>() {
            @Override
            public void accept(CityWeatherHistoryModel cityWeatherHistoryModel) {
                if (cityWeatherHistoryModel.getCityName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(cityWeatherHistoryModel);
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

        void bind(CityWeatherHistoryModel cityWeatherHistoryModel) {
            bindCityName(cityWeatherHistoryModel.getCityName());
            bindDate(cityWeatherHistoryModel);
            bindTemperature(cityWeatherHistoryModel);
            bindIcon(cityWeatherHistoryModel);
        }

        private void bindCityName(String name) {
            this.cityName.setText(name);
        }

        private void bindDate(CityWeatherHistoryModel cityWeatherHistoryModel) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            long time = cityWeatherHistoryModel.getDate() * 1000;
            String date = simpleDateFormat.format(time);
            cityDate.setText(date);
        }

        private void bindTemperature(CityWeatherHistoryModel cityWeatherHistoryModel) {
            String temperature;

            int intTemperature = (int) cityWeatherHistoryModel.getTemperature();
            if (intTemperature > 0) {
                temperature = "+" + intTemperature + "°";
            } else if (intTemperature < 0) {
                temperature = intTemperature + "°";
            } else {
                temperature = "0°";
            }

            cityTemperature.setText(temperature);
        }

        private void bindIcon(CityWeatherHistoryModel cityWeatherHistoryModel) {
            IconType iconType = IconType.valueOf(cityWeatherHistoryModel.getWeather());
            weatherIcon.setImageResource(iconType.getIconRes());
        }
    }

    public interface OnCitySelectListener {
        void onCitySelect(String cityName);
    }
}
