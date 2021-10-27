package com.warh.damlab3;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.warh.damlab3.broadcast.RecordatorioReceiver;
import com.warh.damlab3.fragment.DatePickerFragment;
import com.warh.damlab3.fragment.TimePickerFragment;
import com.warh.damlab3.model.RecordatorioModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AgregarRecordatorioActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;

    EditText fechaRecordatorioET;
    EditText horaRecordatorioET;
    android.support.design.widget.TextInputEditText descripcionRecordatorioET;
    android.support.design.widget.FloatingActionButton agregarRecordatorioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_recordatorio);
        this.createNotificationChannel();
        broadcastReceiver = new RecordatorioReceiver();

        descripcionRecordatorioET = (android.support.design.widget.TextInputEditText) findViewById(R.id.AR_descripcion_recordatorio_ET);

        fechaRecordatorioET = (EditText) findViewById(R.id.AR_fecha_recordatorio_ET);
        fechaRecordatorioET.setOnClickListener(view -> {
            mostrarDialogDatePicker();
        });

        horaRecordatorioET = (EditText) findViewById(R.id.AR_hora_recordatorio_ET);
        horaRecordatorioET.setOnClickListener(view -> {
            mostrarDialogTimePicker();
        });

        agregarRecordatorioBtn = (android.support.design.widget.FloatingActionButton) findViewById(R.id.AR_agregar_recordatorio_btn);
        agregarRecordatorioBtn.setOnClickListener(view -> {
            verificarDatos();

        });
    }

    private void mostrarDialogDatePicker() {
        DatePickerFragment fragmentoDatePicker = DatePickerFragment.nuevaInstancia(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                final String fechaSeleccionada = ((dia > 9) ? Integer.toString(dia) : ("0" + dia)) + "/" + ((mes+1 > 9) ? Integer.toString(mes+1) : ("0" + (mes+1))) + "/" + anio;
                fechaRecordatorioET.setText(fechaSeleccionada);
            }
        });
        fragmentoDatePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void mostrarDialogTimePicker() {
        TimePickerFragment fragmentoTimePicker = TimePickerFragment.nuevaInstancia(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int horas, int minutos) {
                final String horaSeleccionada = ((horas > 9) ? Integer.toString(horas) : ("0" + horas)) + ":" + ((minutos > 9) ? Integer.toString(minutos) : ("0" + minutos));
                horaRecordatorioET.setText(horaSeleccionada);
            }
        });
        fragmentoTimePicker.show(getSupportFragmentManager(), "timePicker");
    }

    private void verificarDatos(){
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

        Intent intentAux = new Intent(this, RecordatorioReceiver.class);
        intentAux.setAction("RECORDATORIO");
        intentAux.putExtra("FECHA", formatter.format(fechaSel));
        intentAux.putExtra("DESCRIPCION", descripcionRecordatorio);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intentAux, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarma = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarma.set(AlarmManager.RTC_WAKEUP, fechaSel.getTime(), alarmIntent);

        Toast.makeText(this, "Recordatorio registrado!", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
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