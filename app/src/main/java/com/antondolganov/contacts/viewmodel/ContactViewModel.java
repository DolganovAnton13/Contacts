package com.antondolganov.contacts.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.antondolganov.contacts.App;
import com.antondolganov.contacts.callback.SnackbarCallback;
import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.repository.DataRepository;
import com.antondolganov.contacts.repository.DatabaseRepository;

import java.util.List;
import java.util.concurrent.Executors;

public class ContactViewModel extends ViewModel {

    private DataRepository data;
    private DatabaseRepository database;
    private String searchQuery;

    public ContactViewModel() {
        data = App.getComponent().getDataRepository();
        database = App.getComponent().getDatabaseRepository();
    }

    @SuppressWarnings("unchecked")
    public LiveData<PagedList<Contact>> getContactsPagedList() {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(20)
                .build();


        return new LivePagedListBuilder<>(database.getContacts(), pagedListConfig).build();
    }

    public LiveData<List<Contact>> getContactsFromServer(SnackbarCallback callback) {
        return data.getContacts(callback);
    }

    @SuppressWarnings("unchecked")
    public LiveData<PagedList<Contact>> getResultsSearchQuery() {

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(20)
                .build();

        if (TextUtils.isDigitsOnly(searchQuery)) {
            return new LivePagedListBuilder<>(database.getContactsByPhone(searchQuery), pagedListConfig).build();
        } else {
            return new LivePagedListBuilder<>(database.getContactsByName(searchQuery), pagedListConfig).build();
        }

    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void insertContactList(List<Contact> contacts) {
        database.insertContactList(contacts);
    }

    public LiveData<Contact> getContactById(String id) {
        return database.getContactById(id);
    }

    public void deleteAllContacts() {
        database.deleteAllContacts();
    }

    public boolean isDataUpdateInProgress() {
        return data.isUpdateInProgress();
    }
}
