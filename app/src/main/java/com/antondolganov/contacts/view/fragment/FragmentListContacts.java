package com.antondolganov.contacts.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.antondolganov.contacts.R;
import com.antondolganov.contacts.adapter.ContactAdapter;
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

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentListContacts extends Fragment implements ContactClickListener, SwipeRefreshLayout.OnRefreshListener, SnackbarCallback {

    private ContactViewModel model;
    private FragmentListContactsBinding binding;
    private ContactAdapter contactAdapter;
    private NavController navController;

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
        getContactsFromServer();
        loadContactList();
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
        contactAdapter = new ContactAdapter();
        contactAdapter.setContactClickListener(this);

        RecyclerView contactList = binding.contactList;
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        contactList.setAdapter(contactAdapter);
        contactList.setHasFixedSize(true);
        contactList.setItemAnimator(new DefaultItemAnimator());

        binding.swipeRefreshLayout.setOnRefreshListener(this);

        getQuerySearch();
    }

    private void getQuerySearch() {
        RxSearchView.queryTextChanges(binding.searchView)
                .debounce(800, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (!s.isEmpty()) {
                            model.setSearchQuery(s);
                            ShowSearchQuery();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    private void ShowSearchQuery() {
        model.getSearchQuery().observe(getViewLifecycleOwner(), searchQuery -> {
            contactAdapter.setContacts(searchQuery);
            contactAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onContactClick(Contact simpleContact) {
        Bundle bundle = new Bundle();
        bundle.putString("id", simpleContact.getId());
        navController.navigate(R.id.fragmentProfile, bundle);
    }

    private void getContactsFromServer() {
        NetworkState networkState = new NetworkState(getActivity());
        if (networkState.isOnline()) {

            model.getContactsFromServer(this).observe(getViewLifecycleOwner(), contacts -> {
                model.insertContactList(contacts);
            });

        } else {
            Snackbar.make(binding.contactList, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            loadContactList();
            showLoading(false);
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadContactList() {
        showLoading(true);
        model.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            contactAdapter.setContacts(contacts);

            if (contacts.size() > 0) {
                binding.swipeRefreshLayout.setRefreshing(false);
                showLoading(false);
            }
            Toast.makeText(getActivity(), String.valueOf(contactAdapter.getItemCount()), Toast.LENGTH_SHORT).show();
        });
    }

    private void showLoading(boolean isShow) {
        binding.loading.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onRefresh() {
        NetworkState networkState = new NetworkState(getActivity());
        if (networkState.isOnline()) {
            if (!model.isDataUpdateInProgress()) {
                model.deleteAllContacts();
                binding.swipeRefreshLayout.setRefreshing(true);
                getContactsFromServer();
            } else
                binding.swipeRefreshLayout.setRefreshing(false);
        } else {
            Snackbar.make(binding.contactList, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            loadContactList();
            showLoading(false);
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void SnackbarShow(String text) {
        Snackbar.make(binding.contactList, text, Snackbar.LENGTH_LONG).show();
    }
}
