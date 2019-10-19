package com.antondolganov.contacts.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.antondolganov.contacts.R;
import com.antondolganov.contacts.adapter.ContactsAdapter;
import com.antondolganov.contacts.callback.ContactClickListener;
import com.antondolganov.contacts.callback.SnackbarCallback;
import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.databinding.FragmentListContactsBinding;
import com.antondolganov.contacts.network.NetworkState;
import com.antondolganov.contacts.view.MainActivity;
import com.antondolganov.contacts.viewmodel.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding3.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListContacts extends Fragment implements ContactClickListener, SwipeRefreshLayout.OnRefreshListener, SnackbarCallback {

    private ContactViewModel model;
    private FragmentListContactsBinding binding;
    private NavController navController;
    private ContactsAdapter contactsAdapter;
    private NetworkState networkState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_contacts, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);
        setUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setSupportActionBar(binding.toolbarMain);
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).setToolbarWithButtonHome(null, "");
        super.onDestroyView();
    }

    private void setUI() {

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        networkState = new NetworkState(getActivity());

        contactsAdapter = new ContactsAdapter();
        contactsAdapter.setContactClickListener(this);

        binding.contactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.contactList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.contactList.setAdapter(contactsAdapter);
        binding.contactList.setHasFixedSize(true);
        binding.contactList.setItemAnimator(new DefaultItemAnimator());

        binding.swipeRefreshLayout.setOnRefreshListener(this);

        getResultsQuerySearch();
    }

    @SuppressLint("CheckResult")
    private void getResultsQuerySearch() {
        if (model.getSearchQuery() != null) {
            binding.searchView.setQuery(model.getSearchQuery(), true);
        } else
            getContactsFromServer();

        RxSearchView.queryTextChanges(binding.searchView)
                .debounce(800, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    if (!query.isEmpty()) {
                        model.setSearchQuery(query);
                        showResultsSearchQuery();
                    } else {
                        model.setSearchQuery(null);
                        loadContactList();
                    }
                }, throwable -> {
                    Snackbar.make(binding.contactList, "Ошибка: " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    showLoading(false);
                });

    }

    private void showResultsSearchQuery() {
        showLoading(true);
        model.getResultsSearchQuery().observe(this, new androidx.lifecycle.Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(PagedList<Contact> contacts) {
                contactsAdapter.submitList(contacts);
                showLoading(false);
            }
        });
    }

    @Override
    public void onContactClick(Contact simpleContact) {
        Bundle bundle = new Bundle();
        bundle.putString("id", simpleContact.getId());
        navController.navigate(R.id.fragmentProfile, bundle);
    }

    private void getContactsFromServer() {
        if (networkState.isOnline()) {
            model.getContactsFromServer(this).observe(getViewLifecycleOwner(), contacts -> {
                model.insertContactList(contacts);
            });
        } else {
            actionIfNoInternet();
        }
    }

    private void loadContactList() {
        showLoading(true);

        model.getContactsPagedList().observe(this, new androidx.lifecycle.Observer<PagedList<Contact>>() {
            @Override
            public void onChanged(PagedList<Contact> contacts) {
                contactsAdapter.submitList(contacts);
                if (contacts.size() > 0) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                    showLoading(false);
                }
            }
        });
    }

    private void showLoading(boolean isShow) {
        binding.loading.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onRefresh() {

        if (networkState.isOnline()) {
            if (!model.isDataUpdateInProgress()) {
                binding.searchView.setQuery(null, false);
                model.setSearchQuery(null);
                model.deleteAllContacts();
                binding.swipeRefreshLayout.setRefreshing(true);
                getContactsFromServer();
            } else
                binding.swipeRefreshLayout.setRefreshing(false);
        } else {
            actionIfNoInternet();
        }
    }

    private void actionIfNoInternet() {
        Snackbar.make(binding.contactList, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
        loadContactList();
        showLoading(false);
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void SnackbarShow(String text) {
        Snackbar.make(binding.contactList, text, Snackbar.LENGTH_LONG).show();
    }
}
