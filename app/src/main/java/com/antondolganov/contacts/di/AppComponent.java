package com.antondolganov.contacts.di;

import com.antondolganov.contacts.repository.DataRepository;
import com.antondolganov.contacts.di.module.DatabaseModule;
import com.antondolganov.contacts.repository.DatabaseRepository;
import com.antondolganov.contacts.di.module.NetworkModule;
import com.antondolganov.contacts.di.module.RepositoryModule;
import com.antondolganov.contacts.di.module.ContextModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class, NetworkModule.class, DatabaseModule.class, ContextModule.class})
public interface AppComponent {

    DataRepository getDataRepository();
    DatabaseRepository getDatabaseRepository();

}