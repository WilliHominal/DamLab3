package com.warh.damlab3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.warh.damlab3.adapter.RecordatorioAdapter;
import com.warh.damlab3.dao.RecordatorioPreferencesDataSource;
import com.warh.damlab3.dao.RecordatorioRepository;

import java.text.SimpleDateFormat;

public class MostrarRecordatoriosActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recordatoriosRecyclerView;
    RecordatorioAdapter recordatoriosAdapter;
    RecyclerView.LayoutManager recordatoriosLayoutManager;
    RecordatorioRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_recordatorios);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.MR_toolbar_titulo);
        setSupportActionBar(toolbar);

        recordatoriosRecyclerView = (RecyclerView) findViewById(R.id.MR_recordatoriosRecycler);
        recordatoriosRecyclerView.setHasFixedSize(true);
        recordatoriosLayoutManager = new LinearLayoutManager(this);
        recordatoriosRecyclerView.setLayoutManager(recordatoriosLayoutManager);

        repository = new RecordatorioRepository(new RecordatorioPreferencesDataSource(this));
        repository.recuperarRecordatorios((exito, recordatorios) -> {
            recordatoriosAdapter = new RecordatorioAdapter(recordatorios);
            recordatoriosRecyclerView.setAdapter(recordatoriosAdapter);
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
        switch (item.getItemId())
        {
            case R.id.menu_configuration_opt:
                Toast.makeText(this, "CONFIG", Toast.LENGTH_SHORT).show();
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }
}