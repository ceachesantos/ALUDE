package com.example.alude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mShowConnection;
    private Button bEmergencia;
    public boolean emergencia = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        //bluetooth conectado
        mShowConnection = (TextView) findViewById(R.id.info_connection);
        bEmergencia = findViewById(R.id.bEmergencia);
        bEmergencia.setText("Emergencia");

        /*In this example, the variableToCheck is the variable that you want to monitor.
        The Handler object is used to periodically check the variable, using the postDelayed method.
        In this case, the variable is checked every 500 milliseconds.
        If the variable has changed, the code inside the if statement will be executed.
        If the variable hasn't changed, the handler will check it again after 500 milliseconds.*/

        Handler handler = new Handler();

        // Check the variable every 500 milliseconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (emergencia) {
                    playSound();
                }
                if (!emergencia) {
                    stopSound();
                }

                // Check again after 500 milliseconds
                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    public void botonEmergencias(View view) {
        if(bEmergencia.getText() == "Emergencia"){
            //activo emergencia de forma manual
            emergencia = true;
            bEmergencia.setText("Cancelar alarma");
            Log.d("EMERGENCIA", "activo emergencias, 'emergencia'="+String.valueOf(emergencia));


        }
        else if(bEmergencia.getText() == "Cancelar alarma"){
            //cancelo la alarma
            emergencia = false;
            bEmergencia.setText("Emergencia");
            Log.d("EMERGENCIA", "cancelo la alarma, 'emergencia'="+String.valueOf(emergencia));
        }
    }

    private void playSound() {
        mediaPlayer.start();
    }

    private void stopSound() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void testConnection(View view) {
        mShowConnection.setText("connected");

        Toast toast = Toast.makeText(this, mShowConnection.getText(), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void testCambioEmergencia(View view) {
        if(emergencia)  emergencia = false;
        else if(!emergencia) emergencia = true;
        Log.d("EMERGENCIA", "'emergencia'="+String.valueOf(emergencia));
    }
}