package com.daffa.kontaqu.ui.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.daffa.kontaqu.R;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
