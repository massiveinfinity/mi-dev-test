package com.dharashah.showcaseandroidapp.utility;


import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CustomInputValidator implements TextWatcher {
    private MaterialEditText et;

    /**
     * @param editText
     */
    public CustomInputValidator(MaterialEditText editText) {
        this.et = editText;
    }

    /**
     * Check error function for all the edittext used in application.
     *
     * @param editText
     * @return boolean
     */
    public static boolean checkError(MaterialEditText editText) {
        switch (editText.getId()) {
            case R.id.etAndroidName:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
            case R.id.etDeviceName:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
            case R.id.etDeviceDescription:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
            case R.id.etAndroidVersion:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
            case R.id.etAndroidCodeName:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
            case R.id.etAndroidTarget:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
            case R.id.etDistribution:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    editText.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.error_color));
                    editText.requestFocus();
                    return true;
                }
                break;
        }
        return false;

    }

    /*
     * (non-Javadoc)
     *
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /*
     * (non-Javadoc)
     *
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!checkError(et)) {
            et.setUnderlineColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.colorPrimary));
            et.setHintTextColor(ContextCompat.getColor(ShowCaseApp.getAppContext(), R.color.hint_text));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    @Override
    public void afterTextChanged(Editable s) {
    }

}
