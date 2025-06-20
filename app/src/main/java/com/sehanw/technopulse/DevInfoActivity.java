package com.sehanw.technopulse;

import android.os.Bundle;
import android.widget.Button;

public class DevInfoActivity extends BaseActivity {
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
    }
}