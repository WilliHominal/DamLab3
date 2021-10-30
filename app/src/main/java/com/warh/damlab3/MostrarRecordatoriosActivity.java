package com.warh.damlab3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.warh.damlab3.adapter.RecordatorioAdapter;
import com.warh.damlab3.dao.RecordatorioPreferencesDataSource;
import com.warh.damlab3.dao.RecordatorioRepository;
import com.warh.damlab3.model.RecordatorioModel;

import java.util.List;

public class MostrarRecordatoriosActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recordatoriosRecyclerView;
    RecordatorioAdapter recordatoriosAdapter;
    RecyclerView.LayoutManager recordatoriosLayoutManager;
    RecordatorioRepository repository;
    FloatingActionButton agregarRecordatorioBtn;

    DrawerLayout drawerLayout;
    NavigationView drawerNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_recordatorios);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.MR_toolbar_titulo);
        setSupportActionBar(toolbar);

        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();

        recordatoriosRecyclerView = (RecyclerView) findViewById(R.id.MR_recordatoriosRecycler);
        recordatoriosRecyclerView.setHasFixedSize(true);
        recordatoriosLayoutManager = new LinearLayoutManager(this);
        recordatoriosRecyclerView.setLayoutManager(recordatoriosLayoutManager);

        repository = new RecordatorioRepository(new RecordatorioPreferencesDataSource(this));
        repository.recuperarRecordatorios((exito, recordatorios) -> {
            recargarDatosAdapter(recordatorios);
        });

        agregarRecordatorioBtn = (FloatingActionButton) findViewById(R.id.MR_agregar_recordatorio_btn);
        agregarRecordatorioBtn.setOnClickListener(view -> {
            Intent i1 = new Intent(MostrarRecordatoriosActivity.this, AgregarRecordatorioActivity.class);
            startActivityForResult(i1, 10);
        });

        drawerLayout = findViewById(R.id.MR_drawer_layout);
        drawerNavigationView = findViewById(R.id.MR_navigation_view);
        drawerNavigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_configuracion_opc:
                    drawerLayout.closeDrawer(drawerNavigationView);
                    Intent i1 = new Intent(MostrarRecordatoriosActivity.this, ConfiguracionActivity.class);
                    startActivity(i1);
                    break;
                case R.id.menu_borrar_recordatorios_opc:
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Eliminar recordatorios")
                            .setMessage("Quieres eliminar todos los recordatorios?")
                            .setPositiveButton("ACEPTAR", (dialog, i) -> repository.borrarRecordatorios(todoOk -> {
                                if (todoOk) {
                                    repository.recuperarRecordatorios((exito, recordatorios) -> {
                                        recargarDatosAdapter(recordatorios);
                                    });
                                    drawerLayout.closeDrawer(drawerNavigationView);
                                    Toast.makeText(getApplicationContext(), "Recordatorios borrados.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error al borrar recordatorios", Toast.LENGTH_SHORT).show();
                                }
                            }))
                            .setNegativeButton("CANCELAR", null)
                            .show();
                    break;
            }
            return false;
        });

        recordatoriosAdapter.setOnItemClickListener((itemView, position) -> {
            repository.borrarRecordatorio(position, exito -> {
                Toast.makeText(this, "Recordatorio borrado", Toast.LENGTH_SHORT).show();
                repository.recuperarRecordatorios((exito1, recordatorios) -> {
                    recargarDatosAdapter(recordatorios);
                });
            });});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_configuration_opt) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10){
            switch(resultCode){
                case Activity.RESULT_OK:
                    repository.recuperarRecordatorios((exito, recordatorios) -> {
                        recargarDatosAdapter(recordatorios);
                    });
                    break;
            }
        }
    }

    private void recargarDatosAdapter(List<RecordatorioModel> recordatorios){
        recordatoriosAdapter = new RecordatorioAdapter(recordatorios);
        recordatoriosRecyclerView.setAdapter(recordatoriosAdapter);
    }
}