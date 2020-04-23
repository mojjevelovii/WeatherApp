package ru.shumilova.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import ru.shumilova.weatherapp.city_selection_screen.CitySelectionFragment;
import ru.shumilova.weatherapp.main_screen.MainFragment;
import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.preferences_screen.PreferencesFragment;

public class MainActivity extends AppCompatActivity implements Navigable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            navigateTo(FragmentType.MAIN, null, true);
        }

    }

    @Override
    public void navigateTo(FragmentType fragmentType, Bundle bundle, Boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getFragment(fragmentType, bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment, fragmentType.name());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack("");
        }
        fragmentTransaction.commit();
    }

    private Fragment getFragment(FragmentType fragmentType, Bundle bundle) {
        Fragment fragment;
        switch (fragmentType) {
            case MAIN:
                fragment = MainFragment.newInstance(bundle);
                break;
            case PREFERENCES:
                fragment = PreferencesFragment.newInstance(bundle);
                break;
            case CITY_SELECTION:
                fragment = CitySelectionFragment.newInstance(bundle);
                break;
            default:
                throw new IllegalArgumentException("Unknown fragment type!");
        }
        return fragment;
    }


}
