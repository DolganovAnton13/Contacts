package com.antondolganov.contacts.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.antondolganov.contacts.api.Api;
import com.antondolganov.contacts.callback.SnackbarCallback;
import com.antondolganov.contacts.data.model.Contact;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {

    private Api api;

    private MutableLiveData<List<Contact>> result;
    private boolean isUpdateInProgress;

    public DataRepository(Api api) {
        this.api = api;
        result = new MutableLiveData<>();
    }

    @SuppressLint("CheckResult")
    public LiveData<List<Contact>> getContacts(SnackbarCallback callback) {
        isUpdateInProgress = true;

        Observable.merge(api.sourceContactsOne(), api.sourceContactsTwo(), api.sourceContactsThree())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)  //разбиваем список на множество отдельных контактов
                .doOnNext(Contact::createClearPhone) // Добавляем чистый номер для каждого контакта (для поска по номеру)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    result.setValue(contacts);
                    isUpdateInProgress = false;
                    callback.SnackbarShow("Загружено");
                }, throwable -> {
                    callback.SnackbarShow("Ошибка загрузки: " + throwable.getMessage());
                    isUpdateInProgress = false;
                });

        return result;
    }

    public boolean isUpdateInProgress() {
        return isUpdateInProgress;
    }
}
