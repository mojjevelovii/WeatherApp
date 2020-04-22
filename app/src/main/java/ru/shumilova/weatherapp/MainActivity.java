package ru.shumilova.weatherapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static ru.shumilova.weatherapp.CitySelectionActivity.CITY_NAME;
import static ru.shumilova.weatherapp.CitySelectionActivity.SELECT_CITY_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    private static final String CITY_NAME_KEY = "CITY_NAME_KEY";
    private static final String YANDEX_URL = "https://yandex.ru/pogoda/";

    private TextView tvCity;
    private AppCompatImageView bExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initButtons();
    }

    private void initButtons() {
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CitySelectionActivity.class);
                startActivityForResult(intent, SELECT_CITY_REQUEST_CODE);
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
    }

    private void initView() {
        tvCity = findViewById(R.id.tv_city);
        bExtra = findViewById(R.id.iv_extra);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_CITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            tvCity.setText(data.getStringExtra(CITY_NAME));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CITY_NAME_KEY, tvCity.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvCity.setText(savedInstanceState.getString(CITY_NAME_KEY));
    }
}
