package com.sehanw.technopulse;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View current = getCurrentFocus();
            if (current instanceof EditText) {
                View touched = findTouchedView((int) ev.getRawX(), (int) ev.getRawY());
                if (!(touched instanceof TextInputEditText)) {
                    clearKeyboard(current);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private View findTouchedView(int x, int y) {
        View root = findViewById(android.R.id.content);
        return findDeepestViewAt((ViewGroup) root, x, y);
    }

    private View findDeepestViewAt(ViewGroup parent, int x, int y) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int[] loc = new int[2];
            child.getLocationOnScreen(loc);
            if (x >= loc[0] && x <= loc[0] + child.getWidth()
                    && y >= loc[1] && y <= loc[1] + child.getHeight()) {
                if (child instanceof ViewGroup) {
                    View deeper = findDeepestViewAt((ViewGroup) child, x, y);
                    if (deeper != null) return deeper;
                }
                return child;
            }
        }
        return parent;
    }

    private void clearKeyboard(View v) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
