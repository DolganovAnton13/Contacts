package com.antondolganov.contacts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.antondolganov.contacts.callback.ContactClickListener;
import com.antondolganov.contacts.data.model.Contact;
import com.antondolganov.contacts.databinding.ItemContactBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<Contact> contacts;
    private ContactClickListener contactClickListener;

    public ContactAdapter() {
        this.contacts = new ArrayList<>();
    }

    public void setContactClickListener(ContactClickListener contactClickListener) {
        this.contactClickListener = contactClickListener;
    }

    public void setContacts(final List<Contact> update) {
        if (getItemCount() == 0) {
            contacts = update;
            notifyItemRangeInserted(0, update.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return contacts.size();
                }

                @Override
                public int getNewListSize() {
                    return update.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return Objects.equals(contacts.get(oldItemPosition).getId(), update.get(newItemPosition).getId());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Contact newContact = update.get(newItemPosition);
                    Contact oldContact = contacts.get(oldItemPosition);
                    return Objects.equals(newContact.getId(), oldContact.getId())
                            && Objects.equals(newContact.getName(), oldContact.getName())
                            && Objects.equals(newContact.getPhone(), oldContact.getPhone())
                            && newContact.getHeight() == oldContact.getHeight();
                }
            });

            contacts = update;
            result.dispatchUpdatesTo(this);
        }
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

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemContactBinding binding = ItemContactBinding.inflate(inflater, parent, false);
        return new ContactHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.binding.setContact(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }
}
