package com.antondolganov.contacts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.antondolganov.contacts.callback.ContactClickListener;
import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.databinding.ItemContactBinding;

import java.util.List;
import java.util.Objects;

public class ContactsAdapter extends PagedListAdapter<Contact, ContactsAdapter.ContactHolder> {

    private ContactClickListener contactClickListener;

    public ContactsAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setContactClickListener(ContactClickListener contactClickListener) {
        this.contactClickListener = contactClickListener;
    }

    @NonNull
    @Override
    public ContactsAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemContactBinding binding = ItemContactBinding.inflate(inflater, parent, false);
        return new ContactsAdapter.ContactHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ContactHolder holder, int position) {
        holder.binding.setContact(getItem(position));
    }

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemContactBinding binding;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            contactClickListener.onContactClick(binding.getContact());
        }
    }

    private static DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return Objects.equals(oldItem.getId(),newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId())
                    && Objects.equals(oldItem.getName(), newItem.getName())
                    && Objects.equals(oldItem.getPhone(), newItem.getPhone())
                    && oldItem.getHeight() == newItem.getHeight();
        }
    };
}
