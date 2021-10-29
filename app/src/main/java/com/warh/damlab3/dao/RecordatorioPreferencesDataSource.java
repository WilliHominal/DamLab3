package com.warh.damlab3.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.warh.damlab3.model.RecordatorioModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordatorioPreferencesDataSource implements RecordatorioDataSource {

    private final SharedPreferences sharedPreferences;

    public RecordatorioPreferencesDataSource(final Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void guardarRecordatorio(RecordatorioModel recordatorio, GuardarRecordatorioCallback callback) {
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");

        //OBTENGO CANTIDAD DE RECORDATORIOS
        int cantRecordatorios = sharedPreferences.getInt("CANTIDAD_RECORDATORIOS", 0);

        //CREO CLAVE nombreRecordatorio PARA NUEVA SharedPreference CON FORMATO RECORDATORIO_XX
        String nombreRecordatorio = "RECORDATORIO_" + cantRecordatorios;

        //GUARDO DATOS DEL RECORDATORIO CON FORMATO fecha*descripcion EN LA CLAVE nombreRecordatorio
        String datosRecordatorio = formatter.format(recordatorio.getFecha()) + "*****" + recordatorio.getTexto();

        //ACTUALIZO LAS SharedPreferences
        editorSP.putString(nombreRecordatorio, datosRecordatorio);
        editorSP.putInt("CANTIDAD_RECORDATORIOS", cantRecordatorios+1);

        //COMMIT DE LOS CAMBIOS
        editorSP.commit();

        callback.resultado(true);
    }

    @Override
    public void recuperarRecordatorios(RecuperarRecordatorioCallback callback) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");

        //OBTENGO CANTIDAD DE RECORDATORIOS
        int cantidadRecordatorios = sharedPreferences.getInt("CANTIDAD_RECORDATORIOS", 0);
        List<RecordatorioModel> recordatorios = new ArrayList<>();

        for (int i=0; i<cantidadRecordatorios; i++){
            //OBTENGO DATOS DEL RECORDATORIO i DESDE SharedPreference
            String numeroRecordatorio = "RECORDATORIO_" + i;
            String datosRecordatorio = sharedPreferences.getString(numeroRecordatorio, "");

            //DIVIDO EL DATO OBTENIDO DE SharedPreference EN fecha Y descripcion
            String datosRecordatorioAux[] = datosRecordatorio.split("\\*\\*\\*\\*\\*");
            Date fechaRecordatorioTemp = null;
            try {
                fechaRecordatorioTemp = formatter.parse(datosRecordatorioAux[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String descripcionRecordatorioTemp = datosRecordatorioAux[1];

            //CREO EL RECORDATORIO TEMPORAL Y LO AGREGO A LA LISTA
            RecordatorioModel recordatorioTemp = new RecordatorioModel(descripcionRecordatorioTemp, fechaRecordatorioTemp);
            recordatorios.add(recordatorioTemp);
        }

        callback.resultado(true, recordatorios);
    }

    @Override
    public void borrarRecordatorios(BorrarRecordatoriosCallback callback){
        sharedPreferences.edit().clear().commit();

        callback.resultado(true);
    }
}
