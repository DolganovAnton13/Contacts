package com.antondolganov.contacts.viewmodel;

import androidx.lifecycle.LiveData;
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

    public ContactViewModel() {
        data = App.getComponent().getDataRepository();
        database = App.getComponent().getDatabaseRepository();
    }

    public LiveData<List<Contact>> getContactsFromServer(SnackbarCallback callback) {
        return data.getContacts(callback);
    }

    public void insertContactList(List<Contact> contacts)
    {
        database.insertContactList(contacts);
    }

    public LiveData<List<Contact>> getContacts() {
        return database.getContacts();
    }

    public LiveData<Contact> getContactById(String id)
    {
        return database.getContactById(id);
    }

    public void deleteAllContacts()
    {
        database.deleteAllContacts();
    }

    public boolean isDataUpdateInProgress() {
        return data.isUpdateInProgress();
    }
}
