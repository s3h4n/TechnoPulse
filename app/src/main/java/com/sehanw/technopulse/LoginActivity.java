package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameInput = findViewById(R.id.inputUsername);
        EditText passwordInput = findViewById(R.id.inputPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);

        loginButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            //  TODO: Login Logic
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

    }
}
