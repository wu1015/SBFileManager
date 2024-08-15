package com.wu1015.sbfilemanager.utils;

import android.util.Log;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;

public class MyOnBackPressedDispatcher extends OnBackPressedCallback {
    public MyOnBackPressedDispatcher(boolean enabled) {
        super(enabled);
    }

    @Override
    public void handleOnBackPressed() {
        Log.d("test", "handleOnBackPressed: ");
    }
}
