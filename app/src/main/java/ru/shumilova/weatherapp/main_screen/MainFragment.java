package ru.shumilova.weatherapp.main_screen;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;

import ru.shumilova.weatherapp.data.LocalRepository;
import ru.shumilova.weatherapp.data.WeatherService;
import ru.shumilova.weatherapp.data.models.ErrorType;
import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.domain.WeatherState;
import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.R;

import static ru.shumilova.weatherapp.data.WeatherService.BROADCAST_ACTION_WEATHER;
import static ru.shumilova.weatherapp.data.WeatherService.EXTRA_RESULT;

public class MainFragment extends Fragment {

    private static final String CITY_NAME_KEY = "CITY_NAME_KEY";
    private static final String PREVIOUS_CITY_NAME_KEY = "PREVIOUS_CITY_NAME_KEY";
    private static final String YANDEX_URL = "https://yandex.ru/pogoda/";
    private static final String PARAMS = MainFragment.class.getName() + "PARAMS";

    private boolean isBound = false;
    private WeatherService.ServiceBinder boundService;
    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundService = (WeatherService.ServiceBinder) service;
            isBound = boundService != null;
            if (boundService != null) {
                boundService.getWeather(city);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            boundService = null;
        }
    };

    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WeatherState weatherState = (WeatherState) intent.getSerializableExtra(EXTRA_RESULT);
            if (weatherState != null) {
                onWeatherState(weatherState);
            }
        }
    };

    private TextView tvCity;
    private TextView tvTemperature;
    private TextView tvCondition;
    private TextView tvFeelingTemperature;
    private AppCompatImageView ivWeatherIcon;
    private AppCompatImageView bExtra;
    private RecyclerView rvDailyWeather;
    private Navigable navigator;
    private LinearLayout llMainContainer;
    private ProgressBar pbLoader;
    private ProgressBar pbLoaderRV;
    private SwipeRefreshLayout srlWeekWeather;
    private LocalRepository localRepository;
    private AlertDialog errorDialog;

    private String previousCity;
    private String city;

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

        localRepository = new LocalRepository(requireContext());

        initView(view);
        initButtons();
        initRecyclerView();

        Intent intent = new Intent(requireContext(), WeatherService.class);
        requireActivity().bindService(intent, boundServiceConnection, Context.BIND_AUTO_CREATE);


        if (getArguments() != null) {
            Serializable params = getArguments().getSerializable(PARAMS);
            if (params != null) {
                city = ((MainParams) params).getCityName();
            } else {
                city = previousCity;
            }
        } else {
            city = localRepository.getSelectedCity();
        }
        onRestoreState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().registerReceiver(weatherReceiver, new IntentFilter(BROADCAST_ACTION_WEATHER));
    }

    private void showError(WeatherState weatherState) {
        if (errorDialog == null || !errorDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(weatherState.getErrorType().getErrorMsg());
            builder.setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            errorDialog = builder.create();
            errorDialog.show();
        }
    }

    private void renderWeather(WeatherResponse weatherResponse) {
        int intTemperature = (int) weatherResponse.getMain().getTemp();

        String temperature = formatTemperature(intTemperature);
        tvTemperature.setText(temperature);

        String feelsTemperature;
        int intFeelsTemperature = (int) weatherResponse.getMain().getFeelsLike();

        feelsTemperature = formatTemperature(intFeelsTemperature);
        tvFeelingTemperature.setText(feelsTemperature);

        tvCondition.setText(weatherResponse.getWeather().get(0).getDescription());
        tvCity.setText(weatherResponse.getName());

        previousCity = weatherResponse.getName();

        ivWeatherIcon.setImageResource(weatherResponse.getWeather().get(0).getIcon().getIconRes());

        pbLoader.setVisibility(View.GONE);
        llMainContainer.setVisibility(View.VISIBLE);
    }

    private String formatTemperature(int intFeelsTemperature) {
        String feelsTemperature;
        if (intFeelsTemperature > 0) {
            feelsTemperature = "+" + intFeelsTemperature + "°";
        } else if (intFeelsTemperature < 0) {
            feelsTemperature = intFeelsTemperature + "°";
        } else {
            feelsTemperature = "0°";
        }
        return feelsTemperature;
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDailyWeather.setLayoutManager(layoutManager);

        rvDailyWeather.setAdapter(new DailyWeatherAdapter());

        Drawable decorationDrawable = requireContext().getDrawable(R.drawable.separator);
        if (decorationDrawable != null) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL);
            itemDecoration.setDrawable(decorationDrawable);
            rvDailyWeather.addItemDecoration(itemDecoration);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tvCity != null) {
            outState.putString(CITY_NAME_KEY, tvCity.getText().toString());
            localRepository.saveSelectedCity(tvCity.getText().toString());
            outState.putString(PREVIOUS_CITY_NAME_KEY, previousCity);
        }
    }


    private void onRestoreState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(CITY_NAME_KEY) != null) {
                tvCity.setText(savedInstanceState.getString(CITY_NAME_KEY));
                previousCity = savedInstanceState.getString(PREVIOUS_CITY_NAME_KEY);
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

        srlWeekWeather.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boundService.getWeather(tvCity.getText().toString());
            }
        });
    }

    private void initView(View view) {
        tvCity = view.findViewById(R.id.tv_city);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvCondition = view.findViewById(R.id.tv_condition);
        tvFeelingTemperature = view.findViewById(R.id.tv_feeling_temperature);
        ivWeatherIcon = view.findViewById(R.id.iv_weather_icon);
        bExtra = view.findViewById(R.id.iv_extra);
        rvDailyWeather = view.findViewById(R.id.rv_week_weather);
        pbLoader = view.findViewById(R.id.pb_loader);
        llMainContainer = view.findViewById(R.id.ll_main_container);
        pbLoaderRV = view.findViewById(R.id.pb_loader_rv);
        srlWeekWeather = view.findViewById(R.id.srl_week_weather);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            requireActivity().unbindService(boundServiceConnection);
        }
        requireActivity().unregisterReceiver(weatherReceiver);
    }

    private void onWeatherState(WeatherState weatherState) {
        if (weatherState.getErrorType() != null) {
            showError(weatherState);
            if (weatherState.getErrorType() == ErrorType.CITY_NOT_FOUND) {
                boundService.getWeather(previousCity);
            }
        } else if (weatherState.getWeatherResponse() != null) {
            localRepository.saveCity(weatherState.getWeatherResponse());
            renderWeather(weatherState.getWeatherResponse());
        } else if (weatherState.getWeatherWeeklyResponse() != null) {
            DailyWeatherAdapter adapter = (DailyWeatherAdapter) rvDailyWeather.getAdapter();
            if (adapter != null) {
                adapter.setDailyWeatherDataSource(weatherState.getWeatherWeeklyResponse().getList());

                pbLoaderRV.setVisibility(View.GONE);

                srlWeekWeather.setRefreshing(false);
            }
        }
    }
}
