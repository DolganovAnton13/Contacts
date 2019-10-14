package com.antondolganov.contacts.view.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.antondolganov.contacts.R;
import com.antondolganov.contacts.adapter.ContactAdapter;
import com.antondolganov.contacts.callback.ContactClickListener;
import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.databinding.FragmentListContactsBinding;
import com.antondolganov.contacts.network.NetworkState;
import com.antondolganov.contacts.view.MainActivity;
import com.antondolganov.contacts.viewmodel.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListContacts extends Fragment implements ContactClickListener {

    private ContactViewModel model;
    private FragmentListContactsBinding binding;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_contacts, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);
        setRecyclerView();
        NetworkState networkState = new NetworkState(getActivity());
        if (networkState.isOnline())
            getContactsFromServer();
        else
            Snackbar.make(binding.contactList, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();

        loadContactList();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbarWithButtonHome(binding.toolbarMain, "Главная");
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).setToolbarWithButtonHome(null, "");
        super.onDestroyView();
    }

    private void setRecyclerView() {
        contactAdapter = new ContactAdapter();
        contactAdapter.setContactClickListener(this);

        RecyclerView contactList = binding.contactList;
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        contactList.setAdapter(contactAdapter);
        contactList.setHasFixedSize(true);
        contactList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onContactClick(Contact simpleContact) {
        FragmentProfile fragmentProfile = new FragmentProfile();
        Bundle bundle = new Bundle();
        bundle.putString("id", simpleContact.getId());
        fragmentProfile.setArguments(bundle);
        ((MainActivity) getActivity()).replaceFragment(fragmentProfile);
    }

    private void getContactsFromServer() {
        model.getContactsFromServer().observe(getViewLifecycleOwner(), contacts -> {
            model.insertContactList(contacts);
        });
    }

    private void loadContactList() {
        model.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactAdapter.setContacts(contacts);
            binding.loading.setVisibility(View.INVISIBLE);
        });
    }
}
