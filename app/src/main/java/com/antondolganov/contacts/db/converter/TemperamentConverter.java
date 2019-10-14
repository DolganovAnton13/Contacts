package com.antondolganov.contacts.db.converter;

import androidx.room.TypeConverter;

import com.antondolganov.contacts.data.Temperament;

public class TemperamentConverter {

    @TypeConverter
    public static Temperament fromString(String data) {
        return Temperament.valueOf(data);
    }

    @TypeConverter
    public static String fromEnum(Temperament temperament) {
        return temperament.name();
    }
}
