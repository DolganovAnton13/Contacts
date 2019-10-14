package com.antondolganov.contacts.di.module;

import com.antondolganov.contacts.repository.DataRepository;
import com.antondolganov.contacts.repository.DatabaseRepository;
import com.antondolganov.contacts.api.Api;
import com.antondolganov.contacts.db.dao.ContactDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetworkModule.class, DatabaseModule.class})
public class RepositoryModule {

    @Singleton
    @Provides
    DataRepository dataRepository(Api testApi) {
        return new DataRepository(testApi);
    }

    @Singleton
    @Provides
    DatabaseRepository databaseRepository(ContactDao contactDao) {
        return new DatabaseRepository(contactDao);
    }
}