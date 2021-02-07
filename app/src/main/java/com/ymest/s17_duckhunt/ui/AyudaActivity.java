package com.ymest.s17_duckhunt.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.ymest.s17_duckhunt.R;

public class AyudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        Toolbar toolbar = findViewById(R.id.toolbarAyuda);

        setSupportActionBar(toolbar);

        if(toolbar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}