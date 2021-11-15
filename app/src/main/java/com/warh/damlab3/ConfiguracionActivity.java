package com.warh.damlab3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ConfiguracionActivity extends AppCompatActivity {

    Toolbar toolbar;
    MaterialButton borrarDatosBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configuración");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            Intent i1 = new Intent(ConfiguracionActivity.this, MostrarRecordatoriosActivity.class);
            setResult(RESULT_OK, i1);
            finish();
        });

        getFragmentManager().beginTransaction().replace(R.id.fragment_configuracion_container, new ConfiguracionPreferencesFragment()).commit();

        borrarDatosBtn = (MaterialButton) findViewById(R.id.C_borrar_datos_btn);
        borrarDatosBtn.setOnClickListener((view)->{
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            sharedPreferences.edit()
                    .remove("usuario_sysacad")
                    .remove("contrasena_sysacad")
                    .apply();
            getFragmentManager().beginTransaction().replace(R.id.fragment_configuracion_container, new ConfiguracionPreferencesFragment()).commit();
        });
    }
}