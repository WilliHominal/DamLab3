package com.warh.damlab3;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.warh.damlab3.dao.RecordatorioPreferencesDataSource;
import com.warh.damlab3.dao.RecordatorioRepository;
import com.warh.damlab3.receiver.RecordatorioReceiver;
import com.warh.damlab3.fragment.DatePickerFragment;
import com.warh.damlab3.fragment.TimePickerFragment;
import com.warh.damlab3.model.RecordatorioModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AgregarRecordatorioActivity extends AppCompatActivity {

    Toolbar toolbar;

    EditText fechaRecordatorioET;
    EditText horaRecordatorioET;
    TextInputEditText descripcionRecordatorioET;
    FloatingActionButton agregarRecordatorioBtn;
    RecordatorioRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_recordatorio);

        this.crearCanalNotificaciones();

        repository = new RecordatorioRepository(new RecordatorioPreferencesDataSource(this));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        descripcionRecordatorioET = (TextInputEditText) findViewById(R.id.AR_descripcion_recordatorio_ET);

        fechaRecordatorioET = (EditText) findViewById(R.id.AR_fecha_recordatorio_ET);
        fechaRecordatorioET.setOnClickListener(view -> mostrarDialogDatePicker());

        horaRecordatorioET = (EditText) findViewById(R.id.AR_hora_recordatorio_ET);
        horaRecordatorioET.setOnClickListener(view -> mostrarDialogTimePicker());

        agregarRecordatorioBtn = (FloatingActionButton) findViewById(R.id.AR_agregar_recordatorio_btn);
        agregarRecordatorioBtn.setOnClickListener(view -> agregarRecordatorio());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogDatePicker() {
        DatePickerFragment fragmentoDatePicker = DatePickerFragment.nuevaInstancia((datePicker, anio, mes, dia) -> {
            final String fechaSeleccionada = ((dia > 9) ? Integer.toString(dia) : ("0" + dia)) + "/" + ((mes+1 > 9) ? Integer.toString(mes+1) : ("0" + (mes+1))) + "/" + anio;
            fechaRecordatorioET.setText(fechaSeleccionada);
        });
        fragmentoDatePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void mostrarDialogTimePicker() {
        TimePickerFragment fragmentoTimePicker = TimePickerFragment.nuevaInstancia((timePicker, horas, minutos) -> {
            final String horaSeleccionada = ((horas > 9) ? Integer.toString(horas) : ("0" + horas)) + ":" + ((minutos > 9) ? Integer.toString(minutos) : ("0" + minutos));
            horaRecordatorioET.setText(horaSeleccionada);
        });
        fragmentoTimePicker.show(getSupportFragmentManager(), "timePicker");
    }

    private void agregarRecordatorio(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        TimeZone timeZoneArgentina = TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
        formatter.setTimeZone(timeZoneArgentina);

        String fechaSeleccionada = fechaRecordatorioET.getText().toString() + " " + horaRecordatorioET.getText().toString();

        Date fechaSel = null;
        Date fechaHoy = null;
        try {
            fechaSel = formatter.parse(fechaSeleccionada);
            fechaHoy = formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (fechaSel == null || fechaSel.before(fechaHoy)) {
            Toast.makeText(this, "Fecha no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String descripcionRecordatorio = descripcionRecordatorioET.getText().toString().trim();

        if (descripcionRecordatorio.isEmpty()){
            Toast.makeText(this, "Descripción no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("NOTIFICACIONES", true)) {
            Intent intentAux = new Intent(this, RecordatorioReceiver.class);
            intentAux.setAction("RECORDATORIO");
            intentAux.putExtra("FECHA", formatter.format(fechaSel));
            intentAux.putExtra("DESCRIPCION", descripcionRecordatorio);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intentAux, PendingIntent.FLAG_UPDATE_CURRENT);

            final AlarmManager alarma = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarma.set(AlarmManager.RTC_WAKEUP, fechaSel.getTime(), alarmIntent);
        }

        RecordatorioModel recordatorioTemp = new RecordatorioModel(descripcionRecordatorio, fechaSel);
        repository.guardarRepositorio(recordatorioTemp, exito -> {
            if (exito)
                Toast.makeText(this, "Recordatorio registrado!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Error al guardar recordatorio", Toast.LENGTH_SHORT).show();
        });

        Intent i1 = new Intent(AgregarRecordatorioActivity.this, MostrarRecordatoriosActivity.class);
        setResult(RESULT_OK, i1);
        finish();
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(String.valueOf(R.string.channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}