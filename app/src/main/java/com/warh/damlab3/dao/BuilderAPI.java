package com.warh.damlab3.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuilderAPI {
    private static BuilderAPI _INSTANCIA;
    private RecordatorioAPI recordatorioAPI;
    String usuario, contrasena;

    private void iniciarRetrofit(){
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient cliente = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(usuario, contrasena))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://recordatorio-api.duckdns.org/")
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        recordatorioAPI = retrofit.create(RecordatorioAPI.class);
    }

    public void actualizarDatos(){
        iniciarRetrofit();
    }

    public static BuilderAPI getInstancia(){
        if (_INSTANCIA == null){
            _INSTANCIA = new BuilderAPI();
        }
        return _INSTANCIA;
    }

    public RecordatorioAPI getRecordatorioAPI(){
        if (recordatorioAPI == null) {
            this.iniciarRetrofit();
        }
        return recordatorioAPI;
    }

    public void setDatos (String usuario, String contrasena){
        this.usuario = usuario;
        this.contrasena = contrasena;
    }
}
