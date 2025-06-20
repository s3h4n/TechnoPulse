package com.sehanw.technopulse;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLoggedIn = true;

        Class<?> next = isLoggedIn
                ? HomeActivity.class
                : LoginActivity.class;

        startActivity(new Intent(this, next));
        finish();
    }
}
