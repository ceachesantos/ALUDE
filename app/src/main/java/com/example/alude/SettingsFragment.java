package com.example.alude;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Add preference fragment from our xml folder
        addPreferencesFromResource(R.xml.preferences);

        setHasOptionsMenu(true);

        onSharedPreferenceChanged(SettingsActivity.KEY_NAME);
        onSharedPreferenceChanged(SettingsActivity.KEY_PHONE_1);
        onSharedPreferenceChanged(SettingsActivity.KEY_PHONE_2);
        onSharedPreferenceChanged(SettingsActivity.KEY_PHONE_3);
        onSharedPreferenceChanged(SettingsActivity.KEY_PHONE_4);
    }
    public void onSharedPreferenceChanged(String key) {
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference listPref = (EditTextPreference) pref;
            pref.setSummary(listPref.getText());
        }
    }
}
