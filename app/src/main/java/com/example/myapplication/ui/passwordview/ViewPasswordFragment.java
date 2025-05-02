package com.example.myapplication.ui.passwordview;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.PasswordDbHelper;
import com.example.myapplication.R;

public class ViewPasswordFragment extends Fragment {

    private TextView serviceName, websiteLink, usernameText, passwordText;
    private ImageView serviceIcon;
    private Button togglePasswordButton, copyPasswordButton;
    private ImageButton  editButton, deleteButton;

    private boolean isPasswordVisible = false;
    private String currentWebsite;
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_password, container, false);

        serviceName = view.findViewById(R.id.service_name);
        websiteLink = view.findViewById(R.id.website_link);
        usernameText = view.findViewById(R.id.username_text);
        passwordText = view.findViewById(R.id.password_text);
        serviceIcon = view.findViewById(R.id.service_icon);

        togglePasswordButton = view.findViewById(R.id.button_toggle_password);
        copyPasswordButton = view.findViewById(R.id.button_copy_password);
        editButton = view.findViewById(R.id.button_edit);
        deleteButton = view.findViewById(R.id.button_delete);

        Bundle args = getArguments();
        if (args != null) {
            currentWebsite = args.getString("website");
            String name = args.getString("serviceName");
            int iconResId = args.getInt("serviceIconResId", 0);
            int color = args.getInt("serviceColor", 0);

            if (name != null) {
                serviceName.setText(name);
                serviceName.setVisibility(View.VISIBLE);
            }
            if (iconResId != 0) {
                serviceIcon.setImageResource(iconResId);
                serviceIcon.setVisibility(View.VISIBLE);
            }
            if (color != 0) {
                View header = view.findViewById(R.id.header_container);
                GradientDrawable gradient = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[] {
                                color, color, // hold strong color
                                adjustAlpha(color, 0.8f),
                                Color.WHITE  // fade to white
                        }
                );
                header.setBackground(gradient);
            }
        }

        PasswordDbHelper dbHelper = new PasswordDbHelper(getContext());
        Cursor cursor = dbHelper.getAllPasswords();
        while (cursor.moveToNext()) {
            String website = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_WEBSITE));
            if (website.equals(currentWebsite)) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_PASSWORD));
                websiteLink.setText(website);
                usernameText.setText(username);
                passwordText.setText(password);
                passwordText.setTransformationMethod(new PasswordTransformationMethod());
                serviceName.setText("Service Info");

                // Optional: set serviceIcon if known
                break;
            }
        }
        cursor.close();

        togglePasswordButton.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordText.setTransformationMethod(new PasswordTransformationMethod());
                togglePasswordButton.setText("Show Password");
            } else {
                passwordText.setTransformationMethod(new SingleLineTransformationMethod());
                togglePasswordButton.setText("Hide Password");
            }
            isPasswordVisible = !isPasswordVisible;
        });

        copyPasswordButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", passwordText.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Password copied", Toast.LENGTH_SHORT).show();
        });

        editButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("serviceUrl", currentWebsite);
            bundle.putString("username", usernameText.getText().toString());
            bundle.putString("password", passwordText.getText().toString());
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_viewPasswordFragment_to_passwordFormFragment, bundle);
        });

        deleteButton.setOnClickListener(v -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(PasswordDbHelper.TABLE_NAME, PasswordDbHelper.COLUMN_WEBSITE + " = ?", new String[]{currentWebsite});
            Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });



        return view;
    }


}
