package com.antondolganov.contacts.repository;

import androidx.lifecycle.LiveData;

import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.db.dao.ContactDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class DatabaseRepository {

    private ContactDao contactDao;

    public DatabaseRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public LiveData<List<Contact>> getContacts() {
        return contactDao.getAllContacts();
    }

    public LiveData<Contact> getContactById(String id) {
        return contactDao.getContactById(id);
    }

    public LiveData<List<Contact>> getContactsByName(String query) {
        return contactDao.getSimpleContactsByName(query);
    }

    public LiveData<List<Contact>> getContactsByPhone(String query) {
        return contactDao.getSimpleContactsByPhone(query);
    }

    public void insertContactList(List<Contact> contacts) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                contactDao.insert(contacts);
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteAllContacts() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                contactDao.deleteAll();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();
    }
}
