package com.warh.damlab3;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.warh.damlab3.fragment.DatePickerFragment;
import com.warh.damlab3.fragment.TimePickerFragment;

public class MainActivity extends AppCompatActivity {

    EditText fechaRecordatorioET;
    EditText horaRecordatorioET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fechaRecordatorioET = (EditText) findViewById(R.id.fechaRecordatorioET);
        fechaRecordatorioET.setOnClickListener(view -> {
            mostrarDialogDatePicker();
        });

        horaRecordatorioET = (EditText) findViewById(R.id.horaRecordatorioET);
        horaRecordatorioET.setOnClickListener(view -> {
            mostrarDialogTimePicker();
        });
    }

    private void mostrarDialogDatePicker() {
        DatePickerFragment fragmentoDatePicker = DatePickerFragment.nuevaInstancia(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                final String fechaSeleccionada = ((dia > 9) ? Integer.toString(dia) : ("0" + dia)) + " / " + ((mes+1 > 9) ? Integer.toString(mes+1) : ("0" + (mes+1))) + " / " + anio;
                fechaRecordatorioET.setText(fechaSeleccionada);
            }
        });
        fragmentoDatePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void mostrarDialogTimePicker() {
        TimePickerFragment fragmentoTimePicker = TimePickerFragment.nuevaInstancia(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int horas, int minutos) {
                final String horaSeleccionada = ((horas > 9) ? Integer.toString(horas) : ("0" + horas)) + " : " + ((minutos > 9) ? Integer.toString(minutos) : ("0" + minutos));
                horaRecordatorioET.setText(horaSeleccionada);
            }
        });
        fragmentoTimePicker.show(getSupportFragmentManager(), "timePicker");
    }
}