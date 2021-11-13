package com.warh.damlab3.dao;

import android.content.Context;
import android.util.Log;

import com.warh.damlab3.database.BuilderDB;
import com.warh.damlab3.model.RecordatorioModel;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

public class RecordatorioRoomDataSource implements RecordatorioDataSource{

    RecordatorioRoomDao recordatorioDao;

    public RecordatorioRoomDataSource(Context context){
        recordatorioDao = BuilderDB.getInstancia().getRecordatorioRoomDao(context);
    }

    @Override
    public void guardarRecordatorio(RecordatorioModel recordatorio, GuardarRecordatorioCallback callback) {
        recordatorioDao.insertarRecordatorio(recordatorio);
        callback.resultado(true);
    }

    @Override
    public void recuperarRecordatorios(RecuperarRecordatorioCallback callback) {
        RecordatorioModel[] recordatorios = recordatorioDao.cargarRecordatorios();
        ArrayList<RecordatorioModel> recordatoriosList = new ArrayList<>();
        Collections.addAll(recordatoriosList, recordatorios);
        callback.resultado(true, recordatoriosList);
    }

    @Override
    public void borrarRecordatorios(BorrarRecordatoriosCallback callback) {
        recordatorioDao.borrarRecordatorios();
        callback.resultado(true);
    }

    @Override
    public void borrarRecordatorio(int idRecordatorio, BorrarRecordatorioCallback callback) {
        RecordatorioModel reco = recordatorioDao.obtenerRecordatorio(idRecordatorio);
        recordatorioDao.borrarRecordatorio(reco);
        callback.resultado(true);
    }
}
