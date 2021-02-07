package com.ymest.s17_duckhunt.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.ymest.s17_duckhunt.R;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn10s, btn30s, btn60s;
    TextView tvTipoRanking;
    SharedPreferences prefDuracion;
    int duracion = 0;
    Bundle data = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        initbotones();
        

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        reseteaColorBoton();
        seleccionaRanking();
        coloreaBoton(duracion);
        UserRankingFragment fragment0 = new UserRankingFragment();
        data.clear();
        data.putInt("duracion", duracion);
        fragment0.setArguments(data);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment0)
                .commit();

    }

    private void coloreaBoton(int duracion) {
        switch (duracion){
            case 10000:
                btn10s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                tvTipoRanking.setText(btn10s.getText());
                break;
            case 30000:
                btn30s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                tvTipoRanking.setText(btn30s.getText());
                break;
            case 60000:
                btn60s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                tvTipoRanking.setText(btn60s.getText());
                break;
        }
    }

    private void seleccionaRanking() {
        prefDuracion = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String duracionSeleccionada = prefDuracion.getString("duration", "10");
        duracion = Integer.parseInt(duracionSeleccionada.toString()) * 1000;
    }

    private void initbotones() {
        btn10s = findViewById(R.id.buttonRanking10);
        btn10s.setOnClickListener(this);
        btn30s = findViewById(R.id.buttonRanking30);
        btn30s.setOnClickListener(this);
        btn60s = findViewById(R.id.buttonRanking60);
        btn60s.setOnClickListener(this);
        tvTipoRanking = findViewById(R.id.textViewTipoRanking);
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

//getSupportFragmentManager().beginTransaction().remove(fragment1);
        //getSupportFragmentManager().beginTransaction().detach(fragment1).commit();




        reseteaColorBoton();
        switch (v.getId()){
            case R.id.buttonRanking10:
                UserRankingFragment fragment1 = new UserRankingFragment();
                btn10s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                tvTipoRanking.setText(btn10s.getText());
                data.clear();
                data.putInt("duracion", 10000);
                fragment1.setArguments(data);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment1)
                        .commit();
                break;
            case R.id.buttonRanking30:
                UserRankingFragment fragment2 = new UserRankingFragment();
                btn30s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                tvTipoRanking.setText(btn30s.getText());
                data.clear();
                data.putInt("duracion", 30000);
                fragment2.setArguments(data);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment2)
                        .commit();

                break;
            case R.id.buttonRanking60:
                UserRankingFragment fragment3 = new UserRankingFragment();
                btn60s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryDark));
                tvTipoRanking.setText(btn60s.getText());
                data.clear();
                data.putInt("duracion", 60000);
                fragment3.setArguments(data);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, fragment3)
                        .commit();
                break;
        }
    }

    private void reseteaColorBoton() {
        btn10s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        btn30s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        btn60s.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
    }
}