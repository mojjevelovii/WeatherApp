package ru.shumilova.weatherapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.ui.city_selection_screen.CitySelectionFragment;
import ru.shumilova.weatherapp.data.LocalRepository;
import ru.shumilova.weatherapp.ui.info_screen.InfoFragment;
import ru.shumilova.weatherapp.ui.main_screen.MainFragment;
import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.ui.map_screen.MapsFragment;
import ru.shumilova.weatherapp.ui.preferences_screen.PreferencesFragment;

public class MainActivity extends AppCompatActivity implements Navigable {
    private BottomNavigationView bnvNavigation;
    LocalRepository localRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(R.layout.activity_main);
        initNavigation();

        if (savedInstanceState == null) {
            navigateTo(FragmentType.MAIN, null, true);
        }
    }

    private void initTheme() {
        localRepository = new LocalRepository(this);
        int themeResource;

        if (localRepository.isDarkTheme()) {
            themeResource = R.style.DarkAppTheme;
        } else {
            themeResource = R.style.LightAppTheme;
        }
        setTheme(themeResource);
    }

    private void initNavigation() {
        bnvNavigation = findViewById(R.id.bn_navigation);
        bnvNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        navigateTo(FragmentType.MAIN, null, true);
                        break;
                    case R.id.navigation_info:
                        navigateTo(FragmentType.INFO, null, true);
                        break;
                    case R.id.navigation_settings:
                        navigateTo(FragmentType.PREFERENCES, null, true);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown menu item!");
                }
                return true;
            }
        });
    }

    @Override
    public void navigateTo(FragmentType fragmentType, Bundle bundle, Boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        boolean isNeedNewInstance = true;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentType.name());
            if (fragment != null) {
                fragment.setArguments(bundle);
                fragmentManager.popBackStackImmediate(fragmentType.name(), 0);
                isNeedNewInstance = false;
            }
        }

        if (isNeedNewInstance) {
            Fragment fragment = getFragment(fragmentType, bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_container, fragment, fragmentType.name());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentType.name());
            }
            fragmentTransaction.commit();
        }
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
            case INFO:
                fragment = InfoFragment.newInstance(bundle);
                break;
            case WEATHER_MAP:
                fragment = new MapsFragment();
                break;
            default:
                throw new IllegalArgumentException("Unknown fragment type!");
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        final BottomSheetDialog exitDialog = new BottomSheetDialog(this);
        exitDialog.setContentView(R.layout.exit_dialog_layout);
        exitDialog.findViewById(R.id.tv_yes_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        exitDialog.findViewById(R.id.tv_no_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.cancel();
            }
        });
        exitDialog.show();
    }
}
