package com.example.alude;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    /*
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
         */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Get the username Preference
        Preference namePreference = (Preference) getPreferenceManager().findPreference(SettingsActivity.KEY_NAME);
        Preference phone1Preference = (Preference) getPreferenceManager().findPreference(SettingsActivity.KEY_PHONE_1);
        Preference phone2Preference = (Preference) getPreferenceManager().findPreference(SettingsActivity.KEY_PHONE_2);
        Preference phone3Preference = (Preference) getPreferenceManager().findPreference(SettingsActivity.KEY_PHONE_3);
        Preference phone4Preference = (Preference) getPreferenceManager().findPreference(SettingsActivity.KEY_PHONE_4);

        // Attach a listener to update summary when username changes
        SharedPreferences.OnSharedPreferenceChangeListener preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                //namePreference.setSummary(sharedPreferences.getString(USERNAME, ""));

                Preference pref = findPreference(key);
                if (pref instanceof EditTextPreference) {
                    EditTextPreference listPref = (EditTextPreference) pref;
                    pref.setSummary(listPref.getText());
                }
            }
        };

        // Get SharedPreferences object managed by the PreferenceManager for this Fragment
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

        // Register a listener on the SharedPreferences object
        prefs.registerOnSharedPreferenceChangeListener(preferenceListener);

        // Invoke callback manually to display the current username
        preferenceListener.onSharedPreferenceChanged(prefs, SettingsActivity.KEY_NAME);
        preferenceListener.onSharedPreferenceChanged(prefs, SettingsActivity.KEY_PHONE_1);
        preferenceListener.onSharedPreferenceChanged(prefs, SettingsActivity.KEY_PHONE_2);
        preferenceListener.onSharedPreferenceChanged(prefs, SettingsActivity.KEY_PHONE_3);
        preferenceListener.onSharedPreferenceChanged(prefs, SettingsActivity.KEY_PHONE_4);

    }
}
