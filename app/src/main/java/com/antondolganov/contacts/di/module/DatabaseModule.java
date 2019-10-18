package com.antondolganov.contacts.di.module;

import android.content.Context;

import androidx.room.Room;

import com.antondolganov.contacts.db.AppDatabase;
import com.antondolganov.contacts.db.dao.ContactDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DatabaseModule {

    @Singleton
    @Provides
    AppDatabase appDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    ContactDao contactDao(AppDatabase appDatabase) {
        return appDatabase.contactDao();
    }
}
