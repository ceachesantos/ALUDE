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

        /*In this example, the variableToCheck is the variable that you want to monitor.
        The Handler object is used to periodically check the variable, using the postDelayed method.
        In this case, the variable is checked every 500 milliseconds.
        If the variable has changed, the code inside the if statement will be executed.
        If the variable hasn't changed, the handler will check it again after 500 milliseconds.*/

        Handler handler = new Handler();
        final int tiempo_alarma_activa = 5000; // Wait time in milliseconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check every 500 milliseconds if emergencia is true or false
                if (emergencia) {
                    Log.d("EMERGENCIA", "espero los X segundos");
                    bEmergencia.setText("Cancelar alarma");
                    //playSound(); // fix

                    // Wait for tiempo_alarma_activa milliseconds
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (emergencia) {
                                Log.d("EMERGENCIA", "avisar emergencia");
                                checkbox.setChecked(true);
                                //NotificationHelper.createNotification("hola", "hola");
                                // Reset the behavior to default
                                emergencia = false;
                                bEmergencia.setText("Emergencia");

                                // Disable the user interface
                                //bEmergencia.setEnabled(false);
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
        if (mediaPlayer != null) {
            try {
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e("MediaPlayer", "Error preparing media player", e);
            }
        }
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