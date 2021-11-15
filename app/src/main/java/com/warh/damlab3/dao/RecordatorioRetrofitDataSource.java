package com.warh.damlab3.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.warh.damlab3.MostrarRecordatoriosActivity;
import com.warh.damlab3.model.RecordatorioModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordatorioRetrofitDataSource implements RecordatorioDataSource {

    RecordatorioAPI recordatorioAPI;

    public RecordatorioRetrofitDataSource(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        BuilderAPI.getInstancia().setDatos(sp.getString("usuario_sysacad", ""), sp.getString("contrasena_sysacad", ""));
        recordatorioAPI = BuilderAPI.getInstancia().getRecordatorioAPI();
    }

    @Override
    public void guardarRecordatorio(RecordatorioModel recordatorio, GuardarRecordatorioCallback callback) {
        Call<RecordatorioModel> llamada = recordatorioAPI.guardarRecordatorio(recordatorio);
        llamada.enqueue(new Callback<RecordatorioModel>() {
            @Override
            public void onResponse(Call<RecordatorioModel> call, Response<RecordatorioModel> response) {
                Log.println(Log.DEBUG, "API POST OK", ""+response.body());
                callback.resultado(true);
            }

            @Override
            public void onFailure(Call<RecordatorioModel> call, Throwable t) {
                Log.println(Log.ERROR, "API POST ERROR", ""+ t.toString());
                callback.resultado(false);
            }
        });
    }

    @Override
    public void recuperarRecordatorios(RecuperarRecordatorioCallback callback) {
        Call<List<RecordatorioModel>> llamada = recordatorioAPI.obtenerRecordatorios();

        llamada.enqueue(new Callback<List<RecordatorioModel>>() {
            @Override
            public void onResponse(Call<List<RecordatorioModel>> call, Response<List<RecordatorioModel>> response) {
                Log.println(Log.DEBUG, "API GET OK", ""+response.body());
                callback.resultado(true, response.body());
            }

            @Override
            public void onFailure(Call<List<RecordatorioModel>> call, Throwable t) {
                Log.println(Log.ERROR, "API GET ERROR", ""+ t.toString());
                callback.resultado(false, new ArrayList<>());
            }
        });
    }

    @Override
    public void borrarRecordatorios(BorrarRecordatoriosCallback callback) {
        recuperarRecordatorios((exito, recordatorios) -> {
            if (exito){
                for (RecordatorioModel recordatorio : recordatorios){
                    borrarRecordatorio(recordatorio.getId(), b -> {});
                }
            }
        });
    }

    @Override
    public void borrarRecordatorio(int idRecordatorio, BorrarRecordatorioCallback callback) {
        RecordatorioModel reco = new RecordatorioModel();
        reco.setId(idRecordatorio);
        Call<RecordatorioModel> llamada = recordatorioAPI.borrarRecordatorio(reco);
        llamada.enqueue(new Callback<RecordatorioModel>() {
            @Override
            public void onResponse(Call<RecordatorioModel> call, Response<RecordatorioModel> response) {
                Log.println(Log.DEBUG, "API DELETE " + idRecordatorio + " OK", ""+response.body());
                callback.resultado(true);
            }

            @Override
            public void onFailure(Call<RecordatorioModel> call, Throwable t) {
                Log.println(Log.ERROR, "API DELETE ERROR", ""+ t.toString());
                callback.resultado(false);
            }
        });
    }
}
