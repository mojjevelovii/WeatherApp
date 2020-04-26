package ru.shumilova.weatherapp.main_screen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.R;

public class MainFragment extends Fragment {

    private static final String CITY_NAME_KEY = "CITY_NAME_KEY";
    private static final String YANDEX_URL = "https://yandex.ru/pogoda/";
    private static final String PARAMS = MainFragment.class.getName() + "PARAMS";

    private TextView tvCity;
    private AppCompatImageView bExtra;
    private AppCompatImageView bSettings;
    private Navigable navigator;


    public static MainFragment newInstance(Bundle bundle) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Bundle createParams(String cityName) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS, new MainParams(cityName));
        return bundle;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigable) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initButtons();
        if (getArguments() != null) {
            Serializable params = getArguments().getSerializable(PARAMS);
            if (params != null) {
                tvCity.setText(((MainParams) params).getCityName());
            }
        }
        onRestoreState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tvCity != null) {
            outState.putString(CITY_NAME_KEY, tvCity.getText().toString());
        }
    }

    private void onRestoreState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(CITY_NAME_KEY) != null) {
                tvCity.setText(savedInstanceState.getString(CITY_NAME_KEY));
            }
        }
    }

    private void initButtons() {
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigateTo(FragmentType.CITY_SELECTION, null, true);
            }
        });

        bExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = YANDEX_URL + tvCity.getText();
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
            }
        });

        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigator.navigateTo(FragmentType.PREFERENCES, null, true);
            }
        });


    }

    private void initView(View view) {
        tvCity = view.findViewById(R.id.tv_city);
        bExtra = view.findViewById(R.id.iv_extra);
        bSettings = view.findViewById(R.id.iv_settings);

    }

}
