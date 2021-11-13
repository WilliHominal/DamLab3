package com.warh.damlab3.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.warh.damlab3.dao.RecordatorioRoomDao;
import com.warh.damlab3.model.RecordatorioModel;

@Database(entities = {RecordatorioModel.class}, version = 1)
@TypeConverters(ConvertidoresDB.class)
public abstract class RoomDB extends RoomDatabase {
    public abstract RecordatorioRoomDao recordatorioRoomDao();
}
