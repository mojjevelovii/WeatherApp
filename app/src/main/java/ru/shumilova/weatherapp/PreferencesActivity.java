package ru.shumilova.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    private static final String LIFECYCLE = "LIFE_CYCLE";
    private static final String WIND_IS_CHECKED = "WIND_IS_CHECKED";
    private static final String PRESSURE_IS_CHECKED = "PRESSURE_IS_CHECKED";

    private CheckBox cbWindSpeed;
    private CheckBox cbPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        if (savedInstanceState == null) {
            makeToast("Первый запуск");
        } else {
            makeToast("Повторный запуск");
        }

        cbWindSpeed = findViewById(R.id.cb_wind_speed);
        cbPressure = findViewById(R.id.cb_atmosphere_pressure);
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeToast("On Start");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        makeToast("Повторный запуск onRestoreInstanceState");
        cbWindSpeed.setChecked(savedInstanceState.getBoolean(WIND_IS_CHECKED));
        cbPressure.setChecked(savedInstanceState.getBoolean(PRESSURE_IS_CHECKED));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        makeToast("Сохранение данных onSaveInstanceState");
        outState.putBoolean(WIND_IS_CHECKED, cbWindSpeed.isChecked());
        outState.putBoolean(PRESSURE_IS_CHECKED, cbPressure.isChecked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeToast("On Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        makeToast("On Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("On Stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("On Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeToast("On Destroy");
    }

    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Log.d(LIFECYCLE, msg);
    }
}
