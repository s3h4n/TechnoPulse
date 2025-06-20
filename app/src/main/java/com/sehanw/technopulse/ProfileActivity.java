package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * Activity for managing user profile information, including editing details and logging out.
 * Provides options to update username and email, and handles user authentication state.
 */
public class ProfileActivity extends BaseActivity {

    // UI Elements
    private TextView tvUsername, tvEmail;
    private Button backButton, btnEditAccount, btnLogout;
    // Dialogs for editing profile and confirming logout
    private BottomSheetDialog editSheetDialog, logoutSheetDialog;

    // Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupEditSheet();
        setupLogoutSheet();
        setupClickListeners();

        // Load current user data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvUsername.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
        }
    }

    // Initialization Methods
    private void initViews() {
        backButton = findViewById(R.id.back_button);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        btnEditAccount = findViewById(R.id.btnEditAccount);
        btnLogout = findViewById(R.id.btnLogout);        // trigger for logout bottom sheet
    }

    // Setup Methods for UI interactions
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        btnEditAccount.setOnClickListener(v -> editSheetDialog.show());

        btnLogout.setOnClickListener(v -> logoutSheetDialog.show());
    }

    /**
     * Sets up the bottom sheet dialog for editing user profile information.
     */
    private void setupEditSheet() {
        editSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater()
                .inflate(R.layout.edit_profile,
                        findViewById(android.R.id.content),
                        false);
        editSheetDialog.setContentView(sheetView);

        // Initialize views within the bottom sheet
        TextInputEditText etUsername = sheetView.findViewById(R.id.inputUsername);
        TextInputEditText etEmail = sheetView.findViewById(R.id.inputEmail);
        Button btnCancel = sheetView.findViewById(R.id.btnCancel);
        Button btnSave = sheetView.findViewById(R.id.btnSave);

        // Pre-fill fields when dialog is shown
        editSheetDialog.setOnShowListener(d -> {
            etUsername.setText(tvUsername.getText().toString());
            etEmail.setText(tvEmail.getText().toString());
        });

        // Handle button clicks
        btnCancel.setOnClickListener(v -> editSheetDialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String u = etUsername.getText().toString().trim();
            String e = etEmail.getText().toString().trim();
            if (validateInput(u, e)) {
                updateProfile(u, e);
                editSheetDialog.dismiss();
            }
        });
    }

    /**
     * Sets up the bottom sheet dialog for confirming user logout.
     */
    private void setupLogoutSheet() {
        logoutSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_logout,
                        findViewById(android.R.id.content),
                        false);
        logoutSheetDialog.setContentView(view);

        // Initialize views within the bottom sheet
        Button cancel = view.findViewById(R.id.btnCancelLogout);
        Button confirm = view.findViewById(R.id.btnConfirmLogout);

        cancel.setOnClickListener(v -> logoutSheetDialog.dismiss());
        // Perform logout and dismiss dialog on confirmation
        confirm.setOnClickListener(v -> {
            performLogout();
            logoutSheetDialog.dismiss();
        });
    }

    private void performLogout() {
        // Sign out from Firebase and navigate to LoginActivity
        FirebaseAuth.getInstance().signOut();
        showToast("Logged out successfully");
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private boolean validateInput(@NonNull String username, @NonNull String email) {
        /**
         * Validates the username and email inputs.
         *
         * @param username The username to validate.
         * @param email    The email to validate.
         * @return True if inputs are valid, false otherwise.
         */
        if (username.isEmpty()) {
            showToast("Username cannot be empty");
            return false;
        }
        if (email.isEmpty()) {
            showToast("Email cannot be empty");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address");
            return false;
        }
        return true;
    }

    /**
     * Updates the user's profile information in Firebase Authentication and Firestore.
     *
     * @param username The new username.
     * @param email    The new email.
     */
    private void updateProfile(String username, String email) {
        showProgressDialog("Updating profile...");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Update email
        user.updateEmail(email)
                .addOnCompleteListener(emailTask -> {
                    if (emailTask.isSuccessful()) {
                        // Update profile (display name)
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    dismissProgressDialog();
                                    if (profileTask.isSuccessful()) {
                                        // Update UI and show success message
                                        tvUsername.setText(username);
                                        tvEmail.setText(email);
                                        showToast("Profile updated successfully");

                                        // Optional: Update Firestore if you're using it
                                        updateFirestoreUserData(user.getUid(), username, email);

                                        // Handle profile update failure
                                    } else {
                                        showToast("Failed to update profile: " + profileTask.getException());
                                    }
                                    // Handle email update failure
                                });
                    } else {
                        dismissProgressDialog();
                        showToast("Failed to update email: " + emailTask.getException());
                    }
                });
    }

    /**
     * Updates user data in Firestore. This is optional and used if user data is also stored in Firestore.
     *
     * @param userId   The ID of the user.
     * @param username The new username.
     * @param email    The new email.
     */
    private void updateFirestoreUserData(String userId, String username, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);
        updates.put("email", email);

        db.collection("users").document(userId)
                .update(updates)
                .addOnFailureListener(e -> {
                    // Log the error but don't show to user since core profile was updated
                    Log.e("ProfileActivity", "Failed to update Firestore", e);
                });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
