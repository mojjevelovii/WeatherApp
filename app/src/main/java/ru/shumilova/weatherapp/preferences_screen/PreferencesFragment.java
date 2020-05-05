package ru.shumilova.weatherapp.preferences_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.data.LocalRepository;

public class PreferencesFragment extends Fragment {
    private SwitchCompat swDarkTheme;
    private LocalRepository localRepository;

    public static PreferencesFragment newInstance(Bundle bundle) {
        PreferencesFragment fragment = new PreferencesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        localRepository = new LocalRepository(getContext());
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initButtons();
    }

    private void initView(View view) {
        swDarkTheme = view.findViewById(R.id.sw_dark_theme);
    }

    private void initButtons() {
        swDarkTheme.setChecked(localRepository.isDarkTheme());
        swDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localRepository.saveThemeCondition(swDarkTheme.isChecked());
                if (getActivity() != null) {
                    getActivity().recreate();
                }
            }
        });
    }
}
