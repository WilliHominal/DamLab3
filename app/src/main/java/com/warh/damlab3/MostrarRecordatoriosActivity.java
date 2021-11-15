package com.warh.damlab3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.warh.damlab3.adapter.RecordatorioAdapter;
import com.warh.damlab3.dao.RecordatorioDataSource;
import com.warh.damlab3.dao.RecordatorioPreferencesDataSource;
import com.warh.damlab3.dao.RecordatorioRepository;
import com.warh.damlab3.dao.RecordatorioRetrofitDataSource;
import com.warh.damlab3.dao.RecordatorioRoomDataSource;
import com.warh.damlab3.model.RecordatorioModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class MostrarRecordatoriosActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recordatoriosRecyclerView;
    RecordatorioAdapter recordatoriosAdapter;
    RecyclerView.LayoutManager recordatoriosLayoutManager;
    RecordatorioRepository repository;
    FloatingActionButton agregarRecordatorioBtn;

    DrawerLayout drawerLayout;
    NavigationView drawerNavigationView;

    RecordatorioDataSource dataSource;
    RecordatorioRoomDataSource roomDataSource;
    RecordatorioPreferencesDataSource prefDataSource;
    RecordatorioDataSource apiDataSource;

    ProgressBar barraCarga;
    Handler miHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_recordatorios);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.MR_toolbar_titulo);
        setSupportActionBar(toolbar);

        miHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Recordatorios borrados.", Toast.LENGTH_SHORT).show();
                        barraCarga.setVisibility(View.GONE);
                        break;
                    case 1:
                        recordatoriosRecyclerView.setVisibility(View.VISIBLE);
                        barraCarga.setVisibility(View.GONE);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "Recordatorio #" + inputMessage.arg1 + " borrado", Toast.LENGTH_SHORT).show();
                        repository.recuperarRecordatorios((exito1, recos) -> {
                            recordatoriosAdapter = new RecordatorioAdapter(recos);
                            recordatoriosRecyclerView.setAdapter(recordatoriosAdapter);
                            miHandler.sendEmptyMessage(1);
                        });
                        break;
                    case 3:
                        recargarDatosAdapter((List<RecordatorioModel>) inputMessage.obj);
                        cargarListenerAdapter();
                        break;
                    case 4:
                        recargarDatosAdapter((List<RecordatorioModel>) inputMessage.obj);
                        drawerLayout.closeDrawer(drawerNavigationView);
                        break;
                    case 5:
                        recargarDatosAdapter((List<RecordatorioModel>) inputMessage.obj);
                        break;
                }
            }
        };

        recordatoriosRecyclerView = (RecyclerView) findViewById(R.id.MR_recordatoriosRecycler);
        recordatoriosRecyclerView.setHasFixedSize(true);
        recordatoriosLayoutManager = new LinearLayoutManager(this);
        recordatoriosRecyclerView.setLayoutManager(recordatoriosLayoutManager);

        initDataSources();
        actualizarDataSource();

        barraCarga = findViewById(R.id.MR_indicador_carga);

        Thread thread = new Thread(() -> {
            try  {
                Message mensaje = new Message();
                mensaje.what = 3;
                repository.recuperarRecordatorios((exito, recordatorios) -> {
                    mensaje.obj = recordatorios;
                    miHandler.sendMessage(mensaje);
                    miHandler.sendEmptyMessage(1);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();

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
                    startActivityForResult(i1, 20);
                    break;
                case R.id.menu_borrar_recordatorios_opc:
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_delete)
                            .setTitle("Eliminar recordatorios")
                            .setMessage("Quieres eliminar todos los recordatorios?")
                            .setPositiveButton("ACEPTAR", (dialog, i) -> {
                                barraCarga.setVisibility(View.VISIBLE);
                                Message mensaje = new Message();
                                mensaje.what = 4;
                                Thread th1 = new Thread(() -> {
                                    try  {
                                        repository.borrarRecordatorios(exito -> {});
                                        mensaje.obj = new ArrayList<>();
                                        miHandler.sendMessage(mensaje);
                                        miHandler.sendEmptyMessage(0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                                th1.start();
                            })
                            .setNegativeButton("CANCELAR", null)
                            .show();
                    break;
            }
            return false;
        });

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
        switch (requestCode){
            case 10:
                if (resultCode == Activity.RESULT_OK){
                    recordatoriosRecyclerView.setVisibility(View.GONE);
                    barraCarga.setVisibility(View.VISIBLE);
                    Message mensaje = new Message();
                    mensaje.what = 5;
                    Thread t = new Thread(() -> {
                        try  {
                            Thread.sleep(1000);
                            repository.recuperarRecordatorios((exito, recordatorios) -> {
                                mensaje.obj = recordatorios;
                                miHandler.sendMessage(mensaje);
                                miHandler.sendEmptyMessage(1);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    t.start();

                }
                break;
            case 20:
                if (resultCode == Activity.RESULT_OK){
                    actualizarDataSource();
                    repository.recuperarRecordatorios((exito, recordatorios) -> {
                        recargarDatosAdapter(recordatorios);
                    });
                }
                break;
        }
    }

    private void initDataSources(){
        roomDataSource = new RecordatorioRoomDataSource(this);
        prefDataSource = new RecordatorioPreferencesDataSource(this);
        apiDataSource = new RecordatorioRetrofitDataSource(this);
    }

    private void actualizarDataSource(){
        String tipoDataSource = PreferenceManager.getDefaultSharedPreferences(this).getString("datasource", "0");
        switch (tipoDataSource){
            case "0": dataSource = prefDataSource; break;
            case "1": dataSource = roomDataSource; break;
            case "2": dataSource = apiDataSource; break;
        }
        repository = new RecordatorioRepository(dataSource);
    }

    private void recargarDatosAdapter(List<RecordatorioModel> recordatorios){
        recordatoriosAdapter = new RecordatorioAdapter(recordatorios);
        recordatoriosRecyclerView.setAdapter(recordatoriosAdapter);
    }

    public void cargarListenerAdapter(){
        recordatoriosAdapter.setOnItemClickListener((itemView, position) -> {
            recordatoriosRecyclerView.setVisibility(View.GONE);
            barraCarga.setVisibility(View.VISIBLE);
            int idRecordatorioTemp = recordatoriosAdapter.getRecordatorioId(position);
            Thread t = new Thread(() -> {
                try  {
                    Message mensaje = new Message();
                    mensaje.what = 2;
                    repository.borrarRecordatorio(idRecordatorioTemp, exito -> {
                        mensaje.arg1 = idRecordatorioTemp;
                        miHandler.sendMessage(mensaje);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        });
    }
}