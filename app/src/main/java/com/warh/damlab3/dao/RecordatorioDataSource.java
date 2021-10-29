package com.warh.damlab3.dao;

import com.warh.damlab3.model.RecordatorioModel;

import java.util.List;

public interface RecordatorioDataSource {
    interface GuardarRecordatorioCallback {
        void resultado(final boolean exito);
    }

    interface RecuperarRecordatorioCallback {
        void resultado(final boolean exito, final List<RecordatorioModel> recordatorios);
    }

    interface BorrarRecordatoriosCallback {
        void resultado(final boolean exito);
    }

    void guardarRecordatorio(final RecordatorioModel recordatorio, final GuardarRecordatorioCallback callback);
    void recuperarRecordatorios(final RecuperarRecordatorioCallback callback);
    void borrarRecordatorios(final BorrarRecordatoriosCallback callback);
}
