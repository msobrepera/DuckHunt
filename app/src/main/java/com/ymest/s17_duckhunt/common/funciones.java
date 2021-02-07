package com.ymest.s17_duckhunt.common;

import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.preference.PreferenceManager;

public class funciones {
    static SharedPreferences prefUsuario;

    public static Integer configFondo() {
        prefUsuario = PreferenceManager.getDefaultSharedPreferences(MyApp.getContext());
        String fondo = prefUsuario.getString("fondo", "bg_game");
        Resources res = MyApp.getContext().getResources();
        int fondoid = res.getIdentifier(fondo, "drawable", MyApp.getContext().getPackageName());
        return fondoid;

    }
}
