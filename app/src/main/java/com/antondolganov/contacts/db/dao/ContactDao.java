package com.antondolganov.contacts.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.antondolganov.contacts.data.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM Contact")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM Contact WHERE id = :id")
    LiveData<Contact> getContactById(String id);

    @Query("SELECT id, name, phone, height FROM Contact WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    LiveData<List<Contact>> getSimpleContactsByName(String query);

    @Query("SELECT id, name, phone, height FROM Contact WHERE clearPhone LIKE '%' || :query || '%' ORDER BY name ASC")
    LiveData<List<Contact>> getSimpleContactsByPhone(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Contact> contacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM Contact")
    void deleteAll();

}