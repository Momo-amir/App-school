package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final List<ContactModel> contactList;
    private final Context context;
    private final ContactEditListener editListener;

    public ContactAdapter(Context context, List<ContactModel> contactList, ContactEditListener editListener) {
        this.context = context;
        this.contactList = contactList;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_card, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactModel contact = contactList.get(position);
        holder.name.setText(contact.getName());
        String phone = contact.getPhone().trim();

        if (phone.isEmpty()) {
            holder.phoneRow.setVisibility(View.GONE);
        } else {
            holder.phoneRow.setVisibility(View.VISIBLE);
            holder.phone.setText(phone);
            holder.phoneRow.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                context.startActivity(intent);
            });
        }

        String email = contact.getEmail().trim();
        if (email.isEmpty()) {
            holder.emailRow.setVisibility(View.GONE);
        } else {
            holder.emailRow.setVisibility(View.VISIBLE);
            holder.email.setText(email);
            holder.emailRow.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                context.startActivity(intent);
            });
        }
        holder.editButton.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEditContactRequested(contact, position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            contactList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contactList.size());
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, email;
        LinearLayout phoneRow, emailRow;
        ImageButton editButton, deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.contact_phone);
            email = itemView.findViewById(R.id.contact_email);
            phoneRow = itemView.findViewById(R.id.phone_row);
            emailRow = itemView.findViewById(R.id.email_row);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
    public interface ContactEditListener {
        void onEditContactRequested(ContactModel contact, int position);
    }
}