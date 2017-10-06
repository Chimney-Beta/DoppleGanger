package com.app.thomasrogers.cardsaver.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.app.thomasrogers.cardsaver.callbacks.TextWatcherCallback;

/**
 * Created by thomasrogers on 12/28/16.
 */

public class AutoSizeTextWatcher implements TextWatcher {

    private EditText mEditText;
    private TextWatcherCallback mCallback;

    public AutoSizeTextWatcher(EditText editText, TextWatcherCallback callback){
        mEditText = editText;
        mCallback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mEditText.length() > 70) {
            mEditText.setTextSize(4);
        } else if (mEditText.length() > 60) {
            mEditText.setTextSize(6);
        } else if (mEditText.length() > 50) {
            mEditText.setTextSize(8);
        } else if (mEditText.length() > 43) {
            mEditText.setTextSize(10);
        } else if (mEditText.length() > 37) {
            mEditText.setTextSize(12);
        } else if (mEditText.length() > 30) {
            mEditText.setTextSize(14);
        } else {
            mEditText.setTextSize(16);
        }

        if (mEditText.length() == 10) {
            mCallback.AddLayout(mEditText);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
