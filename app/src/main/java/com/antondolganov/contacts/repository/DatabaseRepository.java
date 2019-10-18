package com.antondolganov.contacts.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

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

    public DataSource.Factory<Integer, Contact> getContacts() {
        return contactDao.getAllContacts();
    }

    public LiveData<Contact> getContactById(String id) {
        return contactDao.getContactById(id);
    }

    public DataSource.Factory<Integer, Contact> getContactsByName(String query) {
        return contactDao.getContactsByName(query);
    }

    public DataSource.Factory<Integer, Contact> getContactsByPhone(String query) {
        return contactDao.getContactsByPhone(query);
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
