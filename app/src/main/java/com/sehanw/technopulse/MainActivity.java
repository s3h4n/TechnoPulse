package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // simple logic: always treat user as “not logged in”
        boolean isLoggedIn = true;

        // choose next screen
        Class<?> next = isLoggedIn
                ? HomeActivity.class
                : LoginActivity.class;

        startActivity(new Intent(this, next));
        finish();
    }
}
