package ru.shumilova.weatherapp.ui.map_screen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.ui.main_screen.MainFragment;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private Marker currentMarker;
    private Navigable navigator;

    private Button btnShowWeather;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigable) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        initView(view);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
            }
        });
    }

    private void addMarker(LatLng location) {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(getString(R.string.weather_is_here))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_weather_location)));
        btnShowWeather.setVisibility(View.VISIBLE);
    }

    private void initView(View view) {
        btnShowWeather = view.findViewById(R.id.btn_show_Weather);
        btnShowWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMarker != null) {
                    LatLng latLngPosition = currentMarker.getPosition();
                    Bundle bundle = MainFragment.createParams(null, latLngPosition);
                    navigator.navigateTo(FragmentType.MAIN, bundle, true);
                } else {
                    btnShowWeather.setVisibility(View.GONE);
                }
            }
        });
    }
}