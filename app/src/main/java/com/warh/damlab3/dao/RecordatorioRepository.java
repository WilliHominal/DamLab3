package com.warh.damlab3.dao;

import com.warh.damlab3.model.RecordatorioModel;


public class RecordatorioRepository {
    private final RecordatorioDataSource datasource;

    public RecordatorioRepository(final RecordatorioDataSource datasource) {
        this.datasource = datasource;
    }

    public void guardarRepositorio(final RecordatorioModel recordatorio, final RecordatorioDataSource.GuardarRecordatorioCallback callback){
        datasource.guardarRecordatorio(recordatorio, callback);
    }

    public void recuperarRecordatorios(final RecordatorioDataSource.RecuperarRecordatorioCallback callback){
        datasource.recuperarRecordatorios(callback);
    }
}
