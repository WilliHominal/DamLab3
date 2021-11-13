package com.warh.damlab3.dao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.warh.damlab3.MostrarRecordatoriosActivity;
import com.warh.damlab3.model.RecordatorioModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class RecordatorioPreferencesDataSource implements RecordatorioDataSource {

    private final SharedPreferences sharedPreferences;

    public RecordatorioPreferencesDataSource(final Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void guardarRecordatorio(RecordatorioModel recordatorio, GuardarRecordatorioCallback callback) {
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");

        //OBTENGO EL PROXIMO ID
        int siguienteId = sharedPreferences.getInt("SIGUIENTE_ID", 0);

        //DATOS DEL RECORDATORIO CON FORMATO id*****fecha*****descripcion+*+*+
        String datosRecordatorio = siguienteId + "*****" + formatter.format(recordatorio.getFecha()) + "*****" + recordatorio.getTexto() + "+*+*+";

        //OBTENGO DATOS RECORDATORIOS GUARDADOS Y LE AGREGO EL NUEVO
        String recordatorios = sharedPreferences.getString("RECORDATORIOS", "");
        recordatorios += datosRecordatorio;

        //ACTUALIZO LAS SharedPreferences
        editorSP.putString("RECORDATORIOS", recordatorios);
        editorSP.putInt("SIGUIENTE_ID", siguienteId+1);

        //COMMIT DE LOS CAMBIOS
        editorSP.commit();

        callback.resultado(true);
    }

    @Override
    public void recuperarRecordatorios(RecuperarRecordatorioCallback callback) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm");

        List<RecordatorioModel> recordatorios = new ArrayList<>();

        String recordatoriosString = sharedPreferences.getString("RECORDATORIOS", null);

        if (recordatoriosString == null || recordatoriosString == "") {
            callback.resultado(true, recordatorios);
            return;
        }

        String[] recordatoriosStringArray = recordatoriosString.split("\\+\\*\\+\\*\\+");

        for (String recordatorio : recordatoriosStringArray){
            String[] recordatorioDatos = recordatorio.split("\\*\\*\\*\\*\\*");
            int idRecordatorioTemp = Integer.parseInt(recordatorioDatos[0]);
            Date fechaRecordatorioTemp = null;
            try {
                fechaRecordatorioTemp = formatter.parse(recordatorioDatos[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String descripcionRecordatorioTemp = recordatorioDatos[2];

            RecordatorioModel recordatorioTemp = new RecordatorioModel(idRecordatorioTemp, descripcionRecordatorioTemp, fechaRecordatorioTemp);
            recordatorios.add(recordatorioTemp);
        }

        callback.resultado(true, recordatorios);
    }

    @Override
    public void borrarRecordatorios(BorrarRecordatoriosCallback callback){
        boolean spNotificaciones = sharedPreferences.getBoolean("NOTIFICACIONES", true);
        String spDataSource = sharedPreferences.getString("datasource", "0");

        sharedPreferences.edit()
                .remove("RECORDATORIOS")
                .remove("SIGUIENTE_ID")
                .commit();

        callback.resultado(true);
    }

    @Override
    public void borrarRecordatorio(int idRecordatorio, BorrarRecordatorioCallback callback){

        //GET RECORDATORIOS GUARDADOS
        String recordatoriosString = sharedPreferences.getString("RECORDATORIOS", "");

        if (recordatoriosString == null || recordatoriosString == "") {
            callback.resultado(true);
            return;
        }

        String[] recordatoriosStringArray = recordatoriosString.split("\\+\\*\\+\\*\\+");

        String recordatoriosActualizados = "";

        for (String recordatorio : recordatoriosStringArray){
            String[] datosRecordatorio = recordatorio.split("\\*\\*\\*\\*\\*");
            if (idRecordatorio != Integer.parseInt(datosRecordatorio[0])){
                recordatoriosActualizados += recordatorio + "+*+*+";
            }
        }
        sharedPreferences.edit().putString("RECORDATORIOS", recordatoriosActualizados).commit();

        callback.resultado(true);
    }
}
