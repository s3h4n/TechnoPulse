package com.sehanw.technopulse;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

/**
 * BaseActivity provides common functionality for all activities in the application.
 * It includes features like a progress dialog and touch event handling to dismiss the keyboard.
 */
public class BaseActivity extends AppCompatActivity {

    // Progress dialog for showing loading states
    private ProgressDialog progressDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Check if the action is a touch down event
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View current = getCurrentFocus();
            // If the current focus is an EditText
            if (current instanceof EditText) {
                // Find the view that was touched
                View touched = findTouchedView((int) ev.getRawX(), (int) ev.getRawY());
                // If the touched view is not a TextInputEditText, clear the keyboard
                if (!(touched instanceof TextInputEditText)) {
                    clearKeyboard(current);
                }
            }
        }
        // Call the superclass implementation
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Finds the view at the specified screen coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The view at the specified coordinates.
     */
    private View findTouchedView(int x, int y) {
        View root = findViewById(android.R.id.content);
        return findDeepestViewAt((ViewGroup) root, x, y);
    }

    /**
     * Recursively finds the deepest view at the specified coordinates within a ViewGroup.
     */
    private View findDeepestViewAt(ViewGroup parent, int x, int y) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int[] loc = new int[2];
            child.getLocationOnScreen(loc);
            if (x >= loc[0] && x <= loc[0] + child.getWidth()
                    && y >= loc[1] && y <= loc[1] + child.getHeight()) {
                if (child instanceof ViewGroup) {
                    // Recursively search in child ViewGroup
                    View deeper = findDeepestViewAt((ViewGroup) child, x, y);
                    if (deeper != null) return deeper;
                }
                return child;
            }
        }
        // Return the parent if no child view is found at the coordinates
        return parent;
    }

    /**
     * Clears the focus from the specified view and hides the soft keyboard.
     *
     * @param v The view to clear focus from.
     */
    private void clearKeyboard(View v) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        // Hide the keyboard if the InputMethodManager is available
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Shows a progress dialog with the specified message.
     *
     * @param message The message to display in the progress dialog.
     */
    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        // Set the message and show the dialog
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /**
     * Dismisses the progress dialog if it is currently showing.
     */
    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
