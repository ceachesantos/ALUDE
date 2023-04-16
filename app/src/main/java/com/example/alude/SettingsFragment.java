package com.example.alude;

import android.content.SharedPreferences;
import android.os.Bundle;
//import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/*
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Attach a listener to update summary when username changes
        SharedPreferences.OnSharedPreferenceChangeListener preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}