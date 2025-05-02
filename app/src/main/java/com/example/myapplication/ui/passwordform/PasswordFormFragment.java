package com.example.myapplication.ui.passwordform;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.PasswordDbHelper;
import com.example.myapplication.PasswordService;
import com.example.myapplication.R;

public class PasswordFormFragment extends Fragment {

    private ImageView serviceIcon;
    private TextView serviceName;
    private EditText inputWebsite, inputUsername, inputPassword, inputPasswordConfirm;
    private Button saveButton;

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
        View view = inflater.inflate(R.layout.fragment_password_form, container, false);

        serviceIcon = view.findViewById(R.id.service_icon);
        serviceName = view.findViewById(R.id.service_name);
        inputWebsite = view.findViewById(R.id.input_website);
        inputUsername = view.findViewById(R.id.input_username);
        inputPassword = view.findViewById(R.id.input_password);
        inputPasswordConfirm = view.findViewById(R.id.input_password_confirm);
        saveButton = view.findViewById(R.id.button_save);
        TextView websiteLink = view.findViewById(R.id.website_link);


        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("serviceName");
            String url = args.getString("serviceUrl");
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
                        }                );
                header.setBackground(gradient);
                saveButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
            }

            if (url != null) {
                inputWebsite.setText(url);
                websiteLink.setText(url);
                inputWebsite.setEnabled(false); // lock URL for known services
            }

            // Optional: handle username/password if passed for editing
            inputUsername.setText(args.getString("username", ""));
            inputPassword.setText(args.getString("password", ""));
            inputPasswordConfirm.setText(args.getString("password", ""));

        }

        saveButton.setOnClickListener(v -> {
            String website = inputWebsite.getText().toString();

            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();
            String confirm = inputPasswordConfirm.getText().toString();

            if (TextUtils.isEmpty(website) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirm)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            PasswordDbHelper dbHelper = new PasswordDbHelper(getContext());
            dbHelper.addPassword(website, username, password);
            Toast.makeText(getContext(), "Password saved", Toast.LENGTH_SHORT).show();

            requireActivity().onBackPressed(); // navigate back
        });

        return view;
    }
}
