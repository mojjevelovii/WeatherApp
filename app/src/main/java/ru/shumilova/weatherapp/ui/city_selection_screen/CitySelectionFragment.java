package ru.shumilova.weatherapp.ui.city_selection_screen;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ru.shumilova.weatherapp.data.LocalRepository;
import ru.shumilova.weatherapp.navigation.FragmentType;
import ru.shumilova.weatherapp.navigation.Navigable;
import ru.shumilova.weatherapp.R;
import ru.shumilova.weatherapp.ui.main_screen.MainFragment;

public class CitySelectionFragment extends Fragment implements CityListAdapter.OnCitySelectListener {

    private EditText etCitySearch;
    private AppCompatImageView ivSearch;
    private Navigable navigator;
    private RecyclerView rvCityList;
    private LocalRepository localRepository;


    public static CitySelectionFragment newInstance(Bundle bundle) {
        CitySelectionFragment fragment = new CitySelectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigable) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_city_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localRepository = new LocalRepository(getContext());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.city_selection_toolbar));
        initView(view);
        initButtons();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvCityList.setLayoutManager(layoutManager);
        rvCityList.setAdapter(new CityListAdapter(localRepository.loadCityList(), getViewLifecycleOwner(), this));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
        itemDecoration.setDrawable(getContext().getDrawable(R.drawable.separator));
        rvCityList.addItemDecoration(itemDecoration);
    }

    private void initButtons() {

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMain(etCitySearch.getText().toString());
            }
        });
    }

    private void navigateToMain(String cityName) {
        Bundle mainBundle = MainFragment.createParams(cityName, null);
        navigator.navigateTo(FragmentType.MAIN, mainBundle, true);
    }

    private void initView(View view) {
        etCitySearch = view.findViewById(R.id.et_city_search);
        ivSearch = view.findViewById(R.id.iv_search);
        rvCityList = view.findViewById(R.id.rv_city_list);
    }

    @Override
    public void onCitySelect(String cityName) {
        navigateToMain(cityName);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.city_selection_toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.item_action_search);
        MenuItem weatherMap = menu.findItem(R.id.item_weather_map);
        weatherMap.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigator.navigateTo(FragmentType.WEATHER_MAP, null, true);
                return true;
            }
        });
        final SearchView searchCity = (SearchView) search.getActionView();
        searchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((CityListAdapter) rvCityList.getAdapter()).filterCity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((CityListAdapter) rvCityList.getAdapter()).filterCity(newText);
                return true;
            }

        });
    }
}
