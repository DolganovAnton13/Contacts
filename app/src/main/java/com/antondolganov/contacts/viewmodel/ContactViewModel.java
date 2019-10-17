package com.antondolganov.contacts.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.antondolganov.contacts.App;
import com.antondolganov.contacts.callback.SnackbarCallback;
import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.repository.DataRepository;
import com.antondolganov.contacts.repository.DatabaseRepository;

import java.util.List;

public class ContactViewModel extends ViewModel {

    private DataRepository data;
    private DatabaseRepository database;
    private String searchQuery;

    public ContactViewModel() {
        data = App.getComponent().getDataRepository();
        database = App.getComponent().getDatabaseRepository();
    }

    public LiveData<List<Contact>> getContactsFromServer(SnackbarCallback callback) {
        return data.getContacts(callback);
    }

    public LiveData<List<Contact>> getSearchQuery() {
        if (TextUtils.isEmpty(searchQuery)) {
            return database.getContacts();
        } else {
            if (TextUtils.isDigitsOnly(searchQuery)) {
                return database.getContactsByPhone(searchQuery);
            } else {
                return database.getContactsByName(searchQuery);
            }
        }
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void insertContactList(List<Contact> contacts) {
        database.insertContactList(contacts);
    }

    public LiveData<List<Contact>> getContacts() {
        return database.getContacts();
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
