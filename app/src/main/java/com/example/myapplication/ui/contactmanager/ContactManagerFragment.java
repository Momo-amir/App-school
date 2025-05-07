package com.example.myapplication.ui.contactmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ContactModel;
import com.example.myapplication.ContactAdapter;
import com.example.myapplication.PasswordDbHelper;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactManagerFragment extends Fragment implements ContactAdapter.ContactEditListener {

    private ContactAdapter adapter;
    private List<ContactModel> contactList;
    private PasswordDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_manager, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.contact_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new PasswordDbHelper(requireContext());
        contactList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllContacts();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            contactList.add(new ContactModel(name, phone, email));
        }
        cursor.close();

        adapter = new ContactAdapter(requireContext(), contactList, this);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.fab_add_contact).setOnClickListener(v -> {
            showContactDialog(null, adapter, contactList, dbHelper);
        });

        return view;
    }

    private void showContactDialog(@Nullable ContactModel existingContact, ContactAdapter adapter, List<ContactModel> contactList, PasswordDbHelper dbHelper) {
        LayoutInflater inflaterDialog = LayoutInflater.from(getContext());
        View dialogView = inflaterDialog.inflate(R.layout.dialog_contact_form, null);

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getContext());
        dialogBuilder.setView(dialogView);
        android.app.AlertDialog dialog = dialogBuilder.create();

        android.widget.EditText inputName = dialogView.findViewById(R.id.input_name);
        android.widget.EditText inputPhone = dialogView.findViewById(R.id.input_phone);
        android.widget.EditText inputEmail = dialogView.findViewById(R.id.input_email);
        android.widget.Button saveButton = dialogView.findViewById(R.id.button_save_contact);

        if (existingContact != null) {
            inputName.setText(existingContact.getName());
            inputPhone.setText(existingContact.getPhone());
            inputEmail.setText(existingContact.getEmail());
        }

        saveButton.setOnClickListener(dialogV -> {
            String name = inputName.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();

            if (name.isEmpty() || (phone.isEmpty() && email.isEmpty())) {
                Toast.makeText(getContext(), "Name and at least one contact method required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existingContact != null) {
                // Update
                existingContact.setName(name);
                existingContact.setPhone(phone);
                existingContact.setEmail(email);
                int index = contactList.indexOf(existingContact);
                if (index != -1) {
                    contactList.set(index, existingContact);
                    adapter.notifyItemChanged(index);
                }
            } else {
                // Insert new
                ContactModel newContact = new ContactModel(name, phone, email);
                dbHelper.addContact(name, phone, email);
                contactList.add(newContact);
                adapter.notifyItemInserted(contactList.size() - 1);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onEditContactRequested(ContactModel contact, int position) {
        showContactDialog(contact, adapter, contactList, dbHelper);
    }
}
