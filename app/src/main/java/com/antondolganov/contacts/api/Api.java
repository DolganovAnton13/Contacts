package com.antondolganov.contacts.api;

import com.antondolganov.contacts.data.model.Contact;

import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Api {

    @GET("generated-01.json")
    Observable<List<Contact>> sourceContactsOne();

    @GET("generated-02.json")
    Observable<List<Contact>> sourceContactsTwo();

    @GET("generated-03.json")
    Observable<List<Contact>> sourceContactsThree();

}