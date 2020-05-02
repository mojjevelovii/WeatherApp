package ru.shumilova.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private List<String> cityNames;
    private OnCitySelectListener listener;

    public CityListAdapter(List<String> cityNames, OnCitySelectListener listener) {
        this.cityNames = cityNames;
        this.listener = listener;
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
        TextView cityName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.tv_city_name);
            cityName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCitySelect(cityName.getText().toString());
                }
            });
        }

        void bind(String cityName) {
            this.cityName.setText(cityName);
        }
    }

    public interface OnCitySelectListener {
        public void onCitySelect(String cityName);
    }
}
