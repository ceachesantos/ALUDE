package com.example.alude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    String Latitude, Longitude;
    private TextView mShowConnection;
    private Button bEmergencia;
    private Button bReconectar;
    public boolean emergencia = false;
    public boolean emergenciaArduino = false;
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
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic characteristic;
    BluetoothDevice device;
    private final String DEVICE_ADDRESS = "67:C1:0A:8C:FA:D4";
    private final UUID SERVICE_UUID = UUID.fromString("19B10010-E8F2-537E-4F6C-D104768A1214");
    private final UUID CHARACTERISTIC_UUID = UUID.fromString("29B10011-E8F2-537E-4F6C-D104768A1214");

    @SuppressLint("MissingPermission")
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

        bReconectar = findViewById(R.id.bReconectar);
        bEmergencia = findViewById(R.id.bEmergencia);
        bEmergencia.setText("Emergencia");
        //checkbox = (CheckBox) findViewById(R.id.checkBox);
        //swDaltonismo = (Switch) findViewById(R.id.sw_daltonismo);

        // Get the Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Connect to the BLE device
        device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
        try {
            bluetoothGatt = device.connectGatt(this, false, gattCallback);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Error onCreate", Toast.LENGTH_SHORT).show();
        }

        final int delay = 500; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                //Log.d("VALORES", phone1Pref);
                // Read the characteristic
                if (bluetoothGatt != null && characteristic != null) {
                    try {
                        bluetoothGatt.readCharacteristic(characteristic);
                        mShowConnection.setText("Conectado");
                        mShowConnection.setTextColor(Color.GREEN);
                    } catch (SecurityException e) {
                        Toast.makeText(getApplicationContext(), "Error readCharacteristic", Toast.LENGTH_SHORT).show();
                    }
                }
                //no funciona
                else{
                    mShowConnection.setText("Desconectado");
                    mShowConnection.setTextColor(Color.GRAY);
                }
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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        namePref = sharedPref.getString(SettingsActivity.KEY_NAME, "Empty");
        phone1Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_1, "Empty");
        phone2Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_2, "Empty");
        phone3Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_3, "Empty");
        phone4Pref = sharedPref.getString(SettingsActivity.KEY_PHONE_4, "Empty");
        daltonismo = sharedPref.getBoolean(SettingsActivity.KEY_DALT, false);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

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
                                //finish(); //terminar
                                //startActivity(getIntent()); //volver a abrir Activity. Esto soluciona el bug del sonido y que se active de golpe otra vez el envio del SMS
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
            finish(); //terminar
            startActivity(getIntent());
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
        smsManager.sendTextMessage(phone1Pref, null, namePref + " se ha caído. https://maps.google.com/?q="+Latitude+","+Longitude, null, null);
        smsManager.sendTextMessage(phone2Pref, null, namePref + " se ha caído. https://maps.google.com/?q="+Latitude+","+Longitude, null, null);
        smsManager.sendTextMessage(phone3Pref, null, namePref + " se ha caído. https://maps.google.com/?q="+Latitude+","+Longitude, null, null);
        smsManager.sendTextMessage(phone4Pref, null, namePref + " se ha caído. https://maps.google.com/?q="+Latitude+","+Longitude, null, null);
    }

    //------------------------------BLUETOOTH-------------------------------------
    // Define the callback function for BluetoothGatt
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // Connected to the device, discover services
                try{
                    gatt.discoverServices();
                }catch (SecurityException e){
                    Toast.makeText(getApplicationContext(), "Error if onConnectionStateChange", Toast.LENGTH_SHORT).show();
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Disconnected from the device
                try{
                    gatt.close();
                }catch (SecurityException e){
                    Toast.makeText(getApplicationContext(), "Error else if onConnectionStateChange", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Services discovered, get the characteristic
                BluetoothGattService service = gatt.getService(SERVICE_UUID);
                characteristic = service.getCharacteristic(CHARACTERISTIC_UUID);
                // Read the characteristic
                try{
                    gatt.readCharacteristic(characteristic);
                }catch (SecurityException e){
                    Toast.makeText(getApplicationContext(), "Error readCharacteristic", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // Characteristic read, get the value
                byte[] value = characteristic.getValue();
                emergencia = (value[0] != 0);
                // Do something with the value
                //Toast.makeText(getApplicationContext(), "Valor: "+String.valueOf(value), Toast.LENGTH_SHORT).show();
                Log.i("BLE", "valor escaneo: "+ emergencia);
            }
        }
    };

    public void recargar(View view) {
        finish(); //terminar
        startActivity(getIntent());
    }
}