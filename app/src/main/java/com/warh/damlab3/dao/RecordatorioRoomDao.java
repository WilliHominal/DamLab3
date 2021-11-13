package com.warh.damlab3.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.warh.damlab3.model.RecordatorioModel;

import java.util.ArrayList;

@Dao
public interface RecordatorioRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertarRecordatorio(RecordatorioModel recordatorio);

    @Delete
    public void borrarRecordatorio(RecordatorioModel recordatorio);

    @Query("DELETE FROM RECORDATORIO")
    public void borrarRecordatorios();

    @Query("SELECT * FROM RECORDATORIO")
    public RecordatorioModel[] cargarRecordatorios();

    @Query("SELECT * FROM RECORDATORIO WHERE id=:idRecordatorio")
    public RecordatorioModel obtenerRecordatorio(int idRecordatorio);
}
