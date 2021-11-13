package com.warh.damlab3.database;

import android.content.Context;

import androidx.room.Room;

import com.warh.damlab3.dao.RecordatorioRoomDao;

public class BuilderDB {
    RecordatorioRoomDao recordatorioRoomDao;
    private static BuilderDB _INSTANCIA;
    private RoomDB databaseRoom;

    private void iniciarRoom(Context context){
        databaseRoom = Room.databaseBuilder(context, RoomDB.class, "dam-lab4").allowMainThreadQueries().build();
        recordatorioRoomDao = databaseRoom.recordatorioRoomDao();
    }

    public static BuilderDB getInstancia() {
        if (_INSTANCIA == null){
            _INSTANCIA = new BuilderDB();
        }
        return _INSTANCIA;
    }

    public RecordatorioRoomDao getRecordatorioRoomDao(Context context){
        if (databaseRoom == null){
            iniciarRoom(context);
        }
        return recordatorioRoomDao;
    }
}
