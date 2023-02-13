package com.example.alude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mShowConnection;
    private Button bEmergencia;
    public boolean emergencia = false;
    private MediaPlayer mediaPlayer;
    private CheckBox checkbox;
    private Switch swDaltonismo;
    private int tiempo_alarma = 5000;
    private boolean daltonismo = false;

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

        //bluetooth conectado
        mShowConnection = (TextView) findViewById(R.id.info_connection);
        bEmergencia = findViewById(R.id.bEmergencia);
        bEmergencia.setText("Emergencia");
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        swDaltonismo = (Switch) findViewById(R.id.sw_daltonismo);

        final Handler handler = new Handler();
        final int delay = 500; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                comprobarEmergencia();
                handler.postDelayed(this, delay);
            }
        }, delay);
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
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if(emergencia){
                            Log.d("EMERGENCIA", "llamo a emergencias");
                            //llamar emergencias

                            checkbox.setChecked(true);
                            checkbox.setChecked(false);
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

    public void switchDaltonismo(View view){
        if(swDaltonismo.isChecked()) {
            daltonismo=true;
        } else {
            daltonismo=false;
        }
    }
}