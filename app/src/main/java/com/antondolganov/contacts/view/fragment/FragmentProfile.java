package com.antondolganov.contacts.view.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antondolganov.contacts.R;
import com.antondolganov.contacts.databinding.FragmentProfileBinding;
import com.antondolganov.contacts.view.MainActivity;
import com.antondolganov.contacts.viewmodel.ContactViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    private ContactViewModel model;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setModel(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(getActivity()).get(ContactViewModel.class);

        setContact();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setToolbarWithButtonHome(binding.toolbarProfile, "");
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).setToolbarWithButtonHome(null, "");
        super.onDestroyView();
    }

    private void setContact() {
        Bundle arguments = getArguments();
        String id = arguments.getString("id");
        model.getContactById(id).observe(getViewLifecycleOwner(), contact -> {
                binding.setContact(contact);
        });

    }

    public void onPhoneNumberClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + binding.phone.getText()));
        startActivity(intent);
    }
}
