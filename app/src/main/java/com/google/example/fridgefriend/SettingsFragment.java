package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "TAG: ";
    private FirebaseAuth mAuth;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        Preference signout = (Preference) findPreference("signout");
        signout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //Log.w(TAG, "SIGN OUT CLICK"); CLICK DOES WORK
                mAuth.getInstance().signOut();
                if(mAuth.getInstance().getCurrentUser() == null)
                {
                    //send user back to Main Activity
                    Intent loggedOut = new Intent(SettingsFragment.this, MainActivity.class);
                    SettingsFragment.this.startActivity(loggedOut);
                }
                return true;
            }
        });
    }


}