package ru.shumilova.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CitySelectionActivity extends AppCompatActivity {
    static final int SELECT_CITY_REQUEST_CODE = 12;
    static final String CITY_NAME = "CITY_NAME";

    private EditText etCitySearch;
    private AppCompatImageView ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        initView();
        initButtons();
    }

    private void initButtons() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentResult = new Intent();
                intentResult.putExtra(CITY_NAME, etCitySearch.getText().toString());
                setResult(RESULT_OK, intentResult);
                finish();
            }
        });
    }

    private void initView() {
        etCitySearch = findViewById(R.id.et_city_search);
        ivSearch = findViewById(R.id.iv_search);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
