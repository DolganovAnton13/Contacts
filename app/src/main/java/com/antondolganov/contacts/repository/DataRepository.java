package com.antondolganov.contacts.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.antondolganov.contacts.api.Api;
import com.antondolganov.contacts.data.model.Contact;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {

    private Api api;

    private MutableLiveData<List<Contact>> result;

    public DataRepository(Api api) {
        this.api = api;
        result = new MutableLiveData<>();
    }

    @SuppressLint("CheckResult")
    public LiveData<List<Contact>> getContacts() {
        Observable.merge(api.getSourceOne(), api.getSourceTwo(), api.getSourceThree())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .doOnNext(Contact::createClearPhone) // Добавление чистого номера (только цифры)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    result.setValue(contacts);
                }, throwable -> {
                    throwable.printStackTrace();
                });

        return result;
    }

}
