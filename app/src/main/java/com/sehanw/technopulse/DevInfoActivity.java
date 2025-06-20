package com.sehanw.technopulse;

import android.os.Bundle;
import android.widget.Button;

/**
 * Activity to display developer information.
 * Provides a simple interface with a back button to return to the previous screen.
 */
public class DevInfoActivity extends BaseActivity {
    // UI elements
    private Button backButton;

    /**
     * Initializes the activity, sets the content view, and sets up UI components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        initViews();
        setupClickListeners();
    }

    /**
     * Initializes views by finding them in the layout.
     */
    private void initViews() {
        // Find the back button in the layout
        backButton = findViewById(R.id.back_button);
    }

    /**
     * Sets up click listeners for interactive UI elements.
     */
    private void setupClickListeners() {
        // Set a click listener for the back button to finish the activity
        backButton.setOnClickListener(v -> finish());
    }
}
