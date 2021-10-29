package com.warh.damlab3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ConfiguracionActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configuración");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        getFragmentManager().beginTransaction().replace(R.id.fragment_configuracion_container, new ConfiguracionPreferencesFragment()).commit();
    }
}