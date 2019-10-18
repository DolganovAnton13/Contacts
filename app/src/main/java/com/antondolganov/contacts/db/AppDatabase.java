package com.antondolganov.contacts.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.db.dao.ContactDao;

@Database(entities = {Contact.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();

}