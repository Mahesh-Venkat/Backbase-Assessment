package com.sunfinder.sunfinder.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.adapter.CitiesAdapter;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CitiesFragment extends ListFragment {
    OnCitySelectedListener mCallback;

    SharedPreferences mSharedPreferences;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnCitySelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onCitySelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(new CitiesAdapter(getContext(), getCities()));
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cities_action_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_add_place) {
            addMapFragment();
        }
        return true;
    }

    private void addMapFragment() {
        // Create an instance of ExampleFragment
        AddPlaceFragment addPlaceFragment = new AddPlaceFragment();

        // Add the fragment to the 'fragment_container' FrameLayout
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, addPlaceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private List<String> getCities() {
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(getString(R.string.shared_preference_key),"");
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();

        List<CityInfoTO> cityInfoTOList = gson.fromJson(json, type);

        List<String> citiesList = new ArrayList<>();

        for (CityInfoTO cityInfoTO: cityInfoTOList) {
            citiesList.add(cityInfoTO.getCityName());
        }

        return citiesList;
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnCitySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onCitySelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter(new CitiesAdapter(getContext(), getCities()));
    }
}