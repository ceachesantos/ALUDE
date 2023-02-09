package com.example.alude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mShowConnection;
    private Button bEmergencia;
    public boolean emergencia = false;
    private MediaPlayer mediaPlayer;
    private CheckBox checkbox;

    @Override
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
}