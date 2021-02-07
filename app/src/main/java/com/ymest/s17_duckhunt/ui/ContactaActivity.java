package com.ymest.s17_duckhunt.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ymest.s17_duckhunt.R;

public class ContactaActivity extends AppCompatActivity {

    Button btnEnviar;
    EditText txtMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacta);
        Toolbar toolbar = findViewById(R.id.toolbarContacta);

        setSupportActionBar(toolbar);

        if(toolbar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnEnviar = findViewById(R.id.buttonEnviaMail);
        txtMensaje = findViewById(R.id.editTextMensaje);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dircorreo = "msobrepera@gmail.com";
                String asunto = "DuckHunter";
                String mensaje = txtMensaje.getText().toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {dircorreo});
                i.putExtra(Intent.EXTRA_SUBJECT, asunto);
                i.putExtra(Intent.EXTRA_TEXT, mensaje);

                i.setType("message/rfc822");

                startActivity(i.createChooser(i, "Elige un cliente de Correo:"));
            }
        });
    }


}