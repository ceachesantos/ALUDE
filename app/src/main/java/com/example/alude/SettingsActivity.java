package com.example.alude;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE_1 = "phone1";
    public static final String KEY_PHONE_2 = "phone2";
    public static final String KEY_PHONE_3 = "phone3";
    public static final String KEY_PHONE_4 = "phone4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Change the title of our action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        //Check if frame layout is empty or not
        if (findViewById(R.id.idFrameLayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            //Inflate our fragment
            getFragmentManager().beginTransaction().add(R.id.idFrameLayout, new SettingsFragment()).commit();
        }
    }
}