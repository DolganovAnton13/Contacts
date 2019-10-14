package com.antondolganov.contacts.repository;

import com.antondolganov.contacts.db.dao.ContactDao;

public class DatabaseRepository {

    private ContactDao contactDao;

    public DatabaseRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
    }
}
