package ru.shumilova.weatherapp.preferences_screen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.shumilova.weatherapp.R;

public class PreferencesFragment extends Fragment {

    public static PreferencesFragment newInstance(Bundle bundle) {
        PreferencesFragment fragment = new PreferencesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }
}
