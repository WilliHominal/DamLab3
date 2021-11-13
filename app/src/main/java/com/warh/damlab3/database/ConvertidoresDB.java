package com.warh.damlab3.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class ConvertidoresDB {
    @TypeConverter
    public static Date convertirLongEnDate (Long time){
        return time == null ? null : new Date(time);
    }

    @TypeConverter
    public static Long convertirDateEnLong (Date time){
        return time == null ? null : time.getTime();
    }
}
