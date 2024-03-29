package com.antondolganov.contacts;

import android.app.Application;

import com.antondolganov.contacts.di.AppComponent;
import com.antondolganov.contacts.di.DaggerAppComponent;
import com.antondolganov.contacts.di.module.ContextModule;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}