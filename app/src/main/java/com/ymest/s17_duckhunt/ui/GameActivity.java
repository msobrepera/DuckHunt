package com.ymest.s17_duckhunt.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ymest.s17_duckhunt.R;
import com.ymest.s17_duckhunt.common.Constantes;
import com.ymest.s17_duckhunt.common.funciones;
import com.ymest.s17_duckhunt.models.User;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    TextView tvCounterDucks, tvTimer, tvNick, tvRecord;
    ImageView ivDuck;
    int counter = 0;
    int duracionJuego = 0;
    int anchoPantalla;
    int altoPantalla;
    Random aleatorio;
    boolean gameOver = false;
    String id, nick, record2;
    FirebaseFirestore db;
    SharedPreferences mSharedPreferences;
    ConstraintLayout fondoGame;
    ProgressBar barra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseFirestore.getInstance();

        initViewComponents();

        eventos();

        fondoGame.setBackgroundResource(funciones.configFondo());
        fondoGame.getBackground().setAlpha(128);
        initCuentaAtras();
        initPantalla();
        moverpato();

    }



    @Override
    protected void onRestart() {
        super.onRestart();
        fondoGame.setBackgroundResource(funciones.configFondo());
        fondoGame.getBackground().setAlpha(128);
        mostrarDialogoGameOver();
    }

    private void initCuentaAtras() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String duracionSeleccionada = mSharedPreferences.getString("duration", "10");
        duracionJuego = Integer.parseInt(duracionSeleccionada.toString()) * 1000;
        barra.setMax(duracionJuego / 1000);
        new CountDownTimer(duracionJuego, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000 + "s"));
                barra.setProgress((int) millisUntilFinished/1000);
            }

            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                GuardarDatos();
                mostrarDialogoGameOver();
            }
        }.start();
    }

    private void GuardarDatos() {
            db.collection("users")
                    .add(new User(nick, counter, duracionJuego))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Datos Guardados", Toast.LENGTH_LONG);
                        }
                    });
    }

    private void mostrarDialogoGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Has conseguido cazar " + counter + " patos")
                .setTitle("Tiempo Finalizado");

        builder.setPositiveButton("Volver a Jugar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                int recordanterior = 0;
                try {
                    recordanterior = Integer.parseInt(tvRecord.getText().toString());
                } catch(NumberFormatException nfe) {
                    // Handle parse error.
                }
                if (recordanterior < counter){
                    record2 = String.valueOf(counter);
                }
                counter = 0;
                tvCounterDucks.setText("0");
                gameOver = false;
                tvRecord.setText(String.valueOf(record2));
                initCuentaAtras();
                moverpato();
            }
        });
        builder.setNegativeButton("Mostrar Ranking", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                Intent i = new Intent(GameActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void initPantalla() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;


        aleatorio = new Random();

    }


    private void initViewComponents() {
        tvCounterDucks = findViewById(R.id.textViewCounter);
        tvNick = findViewById(R.id.textViewnick);
        tvTimer = findViewById(R.id.textViewTimer);
        tvRecord = findViewById(R.id.textViewRecord);
        ivDuck = findViewById(R.id.imageViewDuck);
        fondoGame = findViewById(R.id.constraintGame);
        barra = findViewById(R.id.progressBarTiempo);
        barra.setProgressTintList(getResources().getColorStateList(R.color.colorPrimary));


        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterDucks.setTypeface(typeface);
        tvNick.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvRecord.setTypeface(typeface);


        Bundle extras = getIntent().getExtras();
        nick = extras.getString(Constantes.EXTRA_NICK);
        record2 = extras.getString(Constantes.EXTRA_RECORD);


        //id = extras.getString(Constantes.EXTRA_ID);

        tvNick.setText(nick);
        tvRecord.setText(String.valueOf(record2));
    }

    private void eventos() {
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    ivDuck.setClickable(false);
                    //Aumentamos el contador
                    counter++;
                    tvCounterDucks.setText(String.valueOf(counter));
                    // Mostramos imagen del pato cazado
                    ivDuck.setImageResource(R.drawable.duck_clicked);

                    // Con un delay mostramos pato volando y movemos pato
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivDuck.setClickable(true);
                            ivDuck.setImageResource(R.drawable.duck);
                            moverpato();
                        }
                    }, 200);

                } /*else {
                    mostrarDialogoGameOver();
                }*/
            }
        });
    }

    private void moverpato() {
        int min = 0;
        int anchopato = 0;
        int altopato = 0;
        if(ivDuck.getMeasuredWidth() == 0) anchopato = 200;
        else anchopato = ivDuck.getWidth();
        if(ivDuck.getMeasuredHeight() == 0) altopato = 200;
        else altopato = ivDuck.getHeight();
        int maxX = anchoPantalla - anchopato;
        int maxY = altoPantalla - altopato;



        int randomX = aleatorio.nextInt((maxX - min) + 1);
        int randomY = aleatorio.nextInt((maxY - min) + 1);

        ivDuck.setX(randomX);
        ivDuck.setY(randomY);
    }
}