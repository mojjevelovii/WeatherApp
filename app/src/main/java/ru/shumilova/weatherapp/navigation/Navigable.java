package ru.shumilova.weatherapp.navigation;

import android.os.Bundle;

public interface Navigable {
    void navigateTo(FragmentType fragmentType, Bundle bundle, Boolean addToBackStack);
}
