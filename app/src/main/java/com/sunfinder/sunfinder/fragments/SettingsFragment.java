package com.sunfinder.sunfinder.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.sunfinder.sunfinder.R;

public class SettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener   {
    SharedPreferences mSharedPreferences;
    Resources resources = getActivity().getResources();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        ListPreference measurementListPreference = (ListPreference) findPreference("measurements_list_preference");
        String currentValue = measurementListPreference.getValue();
        measurementListPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundColor(getActivity().getResources().getColor(R.color.color_white));
        getView().setClickable(true);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (((String) newValue).equals(resources.getString(R.string.list_preference_item_1))) {
            updateSharedPreferencesWithSelectedMeasurement(0);
        } else {
            updateSharedPreferencesWithSelectedMeasurement(0);
        }

        return true;
    }

    private void updateSharedPreferencesWithSelectedMeasurement(int position) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(getString(R.string.shared_preference_settings_measurements), getResources().getStringArray(R.array.pref_measurements_values)[position]);
        editor.commit();

    }
}