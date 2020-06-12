package ru.shumilova.weatherapp.ui.main_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.Serializable;

import ru.shumilova.weatherapp.AppConfig;
import ru.shumilova.weatherapp.data.LocalRepository;
import ru.shumilova.weatherapp.data.WeatherRepository;
import ru.shumilova.weatherapp.data.models.ErrorType;
import ru.shumilova.weatherapp.data.models.WeatherResponse;
import ru.shumilova.weatherapp.domain.WeatherState;
import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.utils.NetworkBroadcastReceiver;

public class MainFragment extends Fragment {

    private static final String CITY_NAME_KEY = "CITY_NAME_KEY";
    private static final String PREVIOUS_CITY_NAME_KEY = "PREVIOUS_CITY_NAME_KEY";
    private static final String YANDEX_URL = "https://yandex.ru/pogoda/";
    private static final String PARAMS = MainFragment.class.getName() + "PARAMS";
    private static final String PUSH_TOKEN = "PUSH_TOKEN";
    private static final int PERMISSION_REQUEST_CODE = 10;

    private WeatherRepository wr = new WeatherRepository();
    private LocationListener locationListener;

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
    private String previousCity;
    private AlertDialog errorDialog;
    private NetworkBroadcastReceiver networkBroadcastReceiver;
    private Snackbar snackbar;
    private AppCompatImageView ivWeatherGeo;

    public static MainFragment newInstance(Bundle bundle) {
        MainFragment fragment = new MainFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Bundle createParams(String cityName, LatLng latLngPosition) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAMS, new MainParams(cityName, latLngPosition));
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

    private void requestPermissions() {
        if (isCoarseLocationGranted() || isFineLocationGranted()) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private boolean isFineLocationGranted() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private boolean isCoarseLocationGranted() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestSingleUpdate(provider, locationListener, Looper.getMainLooper());
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                            grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                requestLocation();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localRepository = new LocalRepository(getContext());

        wr.getWeatherData().observe(getViewLifecycleOwner(), new Observer<WeatherState>() {
            @Override
            public void onChanged(WeatherState weatherState) {
                if (weatherState.getErrorType() != null) {
                    showError(weatherState);
                    if (weatherState.getErrorType() == ErrorType.CITY_NOT_FOUND) {
                        wr.getCityWeather(previousCity);
                        wr.getWeatherWeek(previousCity);
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
        });

        initView(view);
        initButtons();
        initRecyclerView();

        if (getArguments() != null) {
            Serializable params = getArguments().getSerializable(PARAMS);
            if (params != null) {
                String city = ((MainParams) params).getCityName();
                LatLng latLngPosition = ((MainParams) params).getLatLngPosition();
                if (city != null) {
                    wr.getCityWeather(city);
                    wr.getWeatherWeek(city);
                } else if (latLngPosition != null) {
                    wr.getWeatherWeekByGeo(latLngPosition.latitude, latLngPosition.longitude);
                    wr.getWeatherByGeo(latLngPosition.latitude, latLngPosition.longitude);
                }
            }
        } else {
            String cityName = localRepository.getSelectedCity();
            wr.getCityWeather(cityName);
            wr.getWeatherWeek(cityName);
        }
        onRestoreState(savedInstanceState);
        getFCMToken();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                wr.getWeatherByGeo(lat, lng);
                wr.getWeatherWeekByGeo(lat, lng);
                srlWeekWeather.setRefreshing(true);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void getFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(PUSH_TOKEN, task.getResult().getToken());
                        } else {
                            Log.d(PUSH_TOKEN, "FAIL");
                        }
                    }
                });
    }

    private void initBroadcastReceiver() {
        networkBroadcastReceiver = new NetworkBroadcastReceiver(new NetworkBroadcastReceiver.ConnectivityReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                showNetworkMessage(isConnected);
            }
        });

        requireActivity().registerReceiver(networkBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void showNetworkMessage(boolean isConnected) {
        if (isConnected) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        } else {
            snackbar = Snackbar.make(rvDailyWeather, R.string.error_msg_no_connection, Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
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

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        itemDecoration.setDrawable(getContext().getDrawable(R.drawable.separator));
        rvDailyWeather.addItemDecoration(itemDecoration);
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
                wr.getCityWeather(tvCity.getText().toString());
                wr.getWeatherWeek(tvCity.getText().toString());
            }
        });

        ivWeatherGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.IS_GEO_ENABLED) {
                    requestPermissions();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.pro_version);
                    builder.setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }

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
        ivWeatherGeo = view.findViewById(R.id.iv_weather_geo);
    }

    @Override
    public void onStart() {
        super.onStart();
        initBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (networkBroadcastReceiver != null) {
            requireActivity().unregisterReceiver(networkBroadcastReceiver);
        }
    }
}
