package com.antondolganov.contacts.db.converter;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static long fromDate(Date data) {
        return data.getTime();
    }

    @TypeConverter
    public static Date fromLong(long timestamp) {
        return new Date(timestamp);
    }
}
