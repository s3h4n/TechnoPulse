package com.sehanw.technopulse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText usernameInput = findViewById(R.id.inputUsername);
        EditText passwordInput = findViewById(R.id.inputPassword);
        EditText confirmInput = findViewById(R.id.inputConfirmPassword);
        EditText emailInput = findViewById(R.id.inputEmail);

        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        registerButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmInput.getText().toString();
            String email = emailInput.getText().toString();

        });

        loginButton.setOnClickListener(view -> {
            finish();
        });

    }
}
