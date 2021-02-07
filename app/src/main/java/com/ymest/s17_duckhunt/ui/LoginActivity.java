package com.ymest.s17_duckhunt.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ymest.s17_duckhunt.R;
import com.ymest.s17_duckhunt.common.Constantes;
import com.ymest.s17_duckhunt.common.funciones;

import io.grpc.internal.SharedResourceHolder;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etnick;
    CheckBox chkrecuerda;
    Button btnStart, btnAyuda, btnRanking, btnAjustes;
    String nick;
    FirebaseFirestore db;
    Object record;
    SharedPreferences pref;
    Boolean recuerda = false;
    ConstraintLayout fondoLogin;


    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instanciar conexión a firebase
        db = FirebaseFirestore.getInstance();


        inicializarVistas();
        fondoLogin.setBackgroundResource(funciones.configFondo());
        fondoLogin.getBackground().setAlpha(128);
        configurarFuente();
        usuarioAnterior();


    }



    @Override
    protected void onRestart() {
        super.onRestart();
        usuarioAnterior();
        fondoLogin.setBackgroundResource(funciones.configFondo());
        fondoLogin.getBackground().setAlpha(128);
    }



    private void usuarioAnterior() {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        recuerda = pref.getBoolean("recuerdanick", false);
        chkrecuerda.setChecked(recuerda);

        if (recuerda){
             // 0 - for private mode
            if(pref.contains("nick")) {
                etnick.setText(pref.getString("nick", ""));
            }
        }
    }

    private void configurarFuente() {
        //Cambiar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        etnick.setTypeface(typeface);
        btnStart.setTypeface(typeface);
        btnAyuda.setTypeface(typeface);
        btnRanking.setTypeface(typeface);
        btnAjustes.setTypeface(typeface);
    }

    private void inicializarVistas() {
        etnick = findViewById(R.id.editTextNick);
        btnStart = findViewById(R.id.buttonStart);
        btnStart.setOnClickListener(this);
        btnAyuda = findViewById(R.id.buttonAyuda);
        btnAyuda.setOnClickListener(this);
        btnRanking = findViewById(R.id.buttonRanking);
        btnRanking.setOnClickListener(this);
        chkrecuerda = findViewById(R.id.checkBoxRecuerda);
        btnAjustes = findViewById(R.id.buttonAjustes);
        btnAjustes.setOnClickListener(this);
        fondoLogin = findViewById(R.id.constraintLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStart:
                nick = etnick.getText().toString().toUpperCase();
                GuardaUsuario();
                if (CompruebaNombre(nick)){
                    compruebaSiExiste();
                }
                break;
            case R.id.buttonAyuda:
                Intent j = new Intent(LoginActivity.this, AyudaActivity.class);
                startActivity(j);
                break;
            case R.id.buttonRanking:
                Intent i = new Intent(LoginActivity.this, RankingActivity.class);
                startActivity(i);
                break;
            case R.id.buttonAjustes:
                Intent k = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(k);
                break;
        }

    }

      private void GuardaUsuario() {
          SharedPreferences.Editor editor = pref.edit();
          String nickGuardar;
          if(chkrecuerda.isChecked()){
              recuerda = true;
              nickGuardar = etnick.getText().toString().toUpperCase();
          }else {
              recuerda = false;
              nickGuardar = "";
          }
          editor.putString("nick", nickGuardar);
          editor.putBoolean("recuerdanick", recuerda);
          editor.commit();
      }

    private boolean CompruebaNombre(String nick) {
        if (nick.isEmpty()) {
            etnick.setError("El nombre de usuario es obligatorio");
            return false;
        } else if(nick.length() < 3){
            etnick.setError("El nombre de usuario debe tener 3 caracteres como mínimo");
            return false;
        } else return true;
    }

    private void compruebaSiExiste() {
        db.collection("users")
                .whereEqualTo("nick", nick)
                .orderBy("ducks", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Si el nick existe, rescatamos el record
                        if(queryDocumentSnapshots.size() > 0){
                            record = queryDocumentSnapshots.getDocuments().get(0).get("ducks");
                        } else{
                            record = "0";
                        }
                        abrirGameActivity();
                    }
                });
    }

    private void abrirGameActivity() {
        etnick.setText("");
        Intent i = new Intent(LoginActivity.this, GameActivity.class);
        //pasamos la variable "nick" al nuevo activity
        i.putExtra(Constantes.EXTRA_NICK, nick);
        i.putExtra(Constantes.EXTRA_RECORD, record.toString());
        startActivity(i);
    }
}