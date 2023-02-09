package com.example.alude;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mShowConnection;
    private Button bEmergencia;
    public boolean emergencia = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bluetooth conectado
        mShowConnection = (TextView) findViewById(R.id.info_connection);
        bEmergencia = findViewById(R.id.bEmergencia);
        bEmergencia.setText("Emergencia");
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
        //if(emergencia) emergencia = false;
        //else if(!emergencia) emergencia = true;
    }


    /*public void showConnection(View view) {

        mShowConnection.setText("connected");

        Toast toast = Toast.makeText(this, mShowConnection.getText(), Toast.LENGTH_SHORT);
        toast.show();

    }*/


}