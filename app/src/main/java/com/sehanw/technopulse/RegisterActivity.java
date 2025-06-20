package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for user registration.
 * Handles input validation, Firebase Authentication, and Firestore data saving.
 */
public class RegisterActivity extends BaseActivity {

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // UI elements for input fields and their layouts
    private TextInputLayout usernameLayout, passwordLayout, confirmLayout, emailLayout;
    private TextInputEditText usernameInput, passwordInput, confirmInput, emailInput;

    // Called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind layouts (matching activity_register.xml)
        usernameLayout = findViewById(R.id.uUsername);
        passwordLayout = findViewById(R.id.uPassword);
        confirmLayout = findViewById(R.id.uConfirmPassword);
        emailLayout = findViewById(R.id.uEmail);

        // Bind inputs
        usernameInput = findViewById(R.id.inputUsername);
        passwordInput = findViewById(R.id.inputPassword);
        confirmInput = findViewById(R.id.inputConfirmPassword);
        emailInput = findViewById(R.id.inputEmail);

        // Bind buttons
        Button registerButton = findViewById(R.id.btnRegister);
        Button loginButton = findViewById(R.id.btnLogin);

        // TextWatcher to clear errors when text is changed
        TextWatcher clearErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int b, int c) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                usernameLayout.setError(null);
                passwordLayout.setError(null);
                confirmLayout.setError(null);
                emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable e) {
            }
        };
        usernameInput.addTextChangedListener(clearErrorWatcher);
        passwordInput.addTextChangedListener(clearErrorWatcher);
        confirmInput.addTextChangedListener(clearErrorWatcher);
        emailInput.addTextChangedListener(clearErrorWatcher);

        // Set click listener for the register button
        registerButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            if (validateRegistration(username, email, password, confirmPassword)) {
                registerUser(username, email, password);
            }
        });

        // Set click listener for the login button (finishes current activity)
        loginButton.setOnClickListener(view -> finish());
    }

    /**
     * Validates user registration input.
     *
     * @param username        The username entered by the user.
     * @param email           The email entered by the user.
     * @param password        The password entered by the user.
     * @param confirmPassword The confirmed password entered by the user.
     * @return True if all inputs are valid, false otherwise.
     */
    private boolean validateRegistration(String username, String email,
                                         String password, String confirmPassword) {
        if (username.isEmpty()) {
            usernameLayout.setError("Username is required");
            return false;
        }
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmLayout.setError("Passwords don't match");
            return false;
        }
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    /**
     * Registers a new user with Firebase Authentication.
     * Updates user profile and saves user data to Firestore.
     *
     * @param username The username for the new user.
     * @param email    The email for the new user.
     * @param password The password for the new user.
     */
    private void registerUser(String username, String email, String password) {
        showProgressDialog("Creating account..."); // Show progress dialog
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates =
                                new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    if (profileTask.isSuccessful()) {
                                        saveUserToFirestore(user.getUid(), username, email);
                                    } else { // Profile update failed
                                        dismissProgressDialog();
                                        Toast.makeText(this,
                                                "Failed to update profile: " + profileTask.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else { // User creation failed
                        dismissProgressDialog();
                        Toast.makeText(this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Saves user data to Firestore.
     *
     * @param userId   The unique ID of the user.
     * @param username The username of the user.
     * @param email    The email of the user.
     */
    private void saveUserToFirestore(String userId, String username, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    dismissProgressDialog();
                    if (task.isSuccessful()) { // Firestore save successful
                        Toast.makeText(this,
                                "Registration successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else { // Firestore save failed
                        Toast.makeText(this,
                                "Failed to save user data: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
