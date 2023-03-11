package com.example.alude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mShowConnection;
    private Button bEmergencia;
    public boolean emergencia = false;
    private MediaPlayer mediaPlayer;
    private CheckBox checkbox;
    //private Switch swDaltonismo;
    private int tiempo_alarma = 5000;
    private boolean daltonismo = false;
    private SmsManager smsManager;
    String namePref, phone1Pref,phone2Pref,phone3Pref,phone4Pref;
    private Context context;
    private int duration;
    final Handler handler = new Handler();
    final Handler handler2 = new Handler();

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        //bluetooth conectado
        mShowConnection = (TextView) findViewById(R.id.info_connection);
        bEmergencia = findViewById(R.id.bEmergencia);
        bEmergencia.setText("Emergencia");
        checkbox = (CheckBox)findViewById(R.id.checkBox);

        //para que vuelva a funcionar bien otra vez, hay que esperar el mismo tiempo que dura la alarma
        //el sonido falla

        Handler handler = new Handler();
        final int tiempo_alarma_activa = 5000; // Wait time in milliseconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //if (emergencia) bEmergencia.setBackgroundColor(R.color.red);
                if (emergencia) {
                    Log.d("EMERGENCIA", "espero los X segundos");
                    bEmergencia.setText("Cancelar alarma");
                    //playSound(); // fix

                    // Wait for tiempo_alarma_activa milliseconds
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //cuando se activa la emergencia, no vuelve a funcionar bien hasta dentro del mismo tiempo que mantiene la alarma
                            if (emergencia) {
                                Log.d("EMERGENCIA", "avisar emergencia");
                                checkbox.setChecked(true);
                                //LLAMAR A EMERGENCIAS
                                // Reset the behavior to default
                                emergencia = false;
                                bEmergencia.setText("Emergencia");
                            }
                        }
                    }, tiempo_alarma_activa);
                } else {
                    bEmergencia.setText("Emergencia");
                    checkbox.setChecked(false);
                    //stopSound(); // fix
                }
                // Check again after 500 milliseconds
                handler.postDelayed(this, 500);
            }
        }, 500);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        context = getApplicationContext();
        //CharSequence text = "Hello toast!";
        duration = Toast.LENGTH_SHORT;

        //bluetooth conectado
        mShowConnection = (TextView) findViewById(R.id.info_connection);
        bEmergencia = findViewById(R.id.bEmergencia);
        bEmergencia.setText("Emergencia");
        //checkbox = (CheckBox) findViewById(R.id.checkBox);
        //swDaltonismo = (Switch) findViewById(R.id.sw_daltonismo);

        final int delay = 500; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                //Log.d("VALORES", phone1Pref);
                comprobarEmergencia();
                handler.postDelayed(this, delay);
            }
        }, delay);

        //Initializing and creating a variable for our button
        FloatingActionButton settingsBtn = findViewById(R.id.floatingActionButton);

        //Adding on click listener for our button
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opening a new intent to open settings activity
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        //Ensures that the settings are properly initialized with their default values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Get the setting as a SharedPreferences object
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the value of the setting that uses the key (KEY_XXX defined in SettingsActivity)
        //If there is no value for the key, the getString() method sets the setting value to Empty
        //For other values such as booleans, integers, or floating point numbers, you can use the getBoolean(), getInt(), or getFloat() methods
        namePref = sharedPref.getString(SettingsActivity.KEY_NAME, "Empty");
        phone1Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_1, "Empty");
        phone2Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_2, "Empty");
        phone3Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_3, "Empty");
        phone4Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_4, "Empty");
        daltonismo = sharedPref.getBoolean(SettingsActivity.KEY_DALT, false);
/*
        //Displays the value of the settings
        TextView mNameTextView = findViewById(R.id.textViewName);
        TextView mPhone1TextView = findViewById(R.id.textViewPhone1);
        TextView mPhone2TextView = findViewById(R.id.textViewPhone2);
        TextView mPhone3TextView = findViewById(R.id.textViewPhone3);
        TextView mPhone4TextView = findViewById(R.id.textViewPhone4);

        mNameTextView.setText(namePref);
        mPhone1TextView.setText(phone1Pref);
        mPhone2TextView.setText(phone2Pref);
        mPhone3TextView.setText(phone3Pref);
        mPhone4TextView.setText(phone4Pref);
        */
    }

    public void comprobarEmergencia(){
            if (emergencia) {
                Log.d("EMERGENCIA", "espero los X segundos");
                if(!daltonismo){
                    bEmergencia.setBackgroundColor(Color.BLUE);
                    bEmergencia.setTextColor(Color.WHITE);
                }
                else if(daltonismo){
                    bEmergencia.setBackgroundColor(Color.GREEN);
                    bEmergencia.setTextColor(Color.BLACK);
                }
                bEmergencia.setText("Cancelar alarma");
                if(!(mediaPlayer.isPlaying())) playSound();
                //esperar los X segundos por la alarma
                handler2.postDelayed(new Runnable() {
                    public void run() {
                        if(emergencia){
                            Log.d("EMERGENCIA", "llamo a emergencias");
                            //llamar emergencias
                            try {
                                enviarSMS();
                            }
                            catch (Exception e){
                                Log.d("EMERGENCIA", "envioSMS no funciona");
                            }

                            Toast.makeText(context, "SMS de emergencia enviado.", duration).show();

                            if(mediaPlayer.isPlaying()) stopSound();
                            emergencia=false;
                        }
                    }
                }, tiempo_alarma); // X seconds
            } else {
                bEmergencia.setText("Emergencia");
                if(!daltonismo){
                    bEmergencia.setBackgroundColor(Color.RED);
                    bEmergencia.setTextColor(Color.WHITE);
                }
                else if(daltonismo){
                    bEmergencia.setBackgroundColor(Color.YELLOW);
                    bEmergencia.setTextColor(Color.BLACK);
                }
            }
    }

    public void botonEmergencias(View view) {
        if(bEmergencia.getText().equals("Emergencia")){
            //activo emergencia de forma manual
            emergencia = true;
            //bEmergencia.setText("Cancelar alarma");
            Log.d("EMERGENCIA", "pulso boton, activo emergencias, 'emergencia'="+String.valueOf(emergencia));

        }
        else if(bEmergencia.getText().equals("Cancelar alarma")){
            //cancelo la alarma
            emergencia = false;
            //bEmergencia.setText("Emergencia");
            Log.d("EMERGENCIA", "pulso boton, cancelo la alarma, 'emergencia'="+String.valueOf(emergencia));
        }
    }

    private void playSound() {
        mediaPlayer.start();
    }

    private void stopSound() {
        mediaPlayer.stop();
    }

    /*public void testConnection(View view) {
        mShowConnection.setText("connected");

        Toast toast = Toast.makeText(this, mShowConnection.getText(), Toast.LENGTH_SHORT);
        toast.show();
    }*/

    public void testCambioEmergencia(View view) {
        if(emergencia)  emergencia = false;
        else if(!emergencia) emergencia = true;
        Log.d("EMERGENCIA", "'emergencia'="+String.valueOf(emergencia));
        //Toast toast = Toast.makeText(this, String.valueOf(emergencia), Toast.LENGTH_SHORT);
        //toast.show();
    }
/*
    public void switchDaltonismo(View view){
        if(swDaltonismo.isChecked()) {
            daltonismo=true;
        } else {
            daltonismo=false;
        }
    }
*/
    public void enviarSMS(){
        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone1Pref, null, namePref + " se ha caído.", null, null);
        smsManager.sendTextMessage(phone2Pref, null, namePref + " se ha caído.", null, null);
        smsManager.sendTextMessage(phone3Pref, null, namePref + " se ha caído.", null, null);
        smsManager.sendTextMessage(phone4Pref, null, namePref + " se ha caído.", null, null);
    }

}