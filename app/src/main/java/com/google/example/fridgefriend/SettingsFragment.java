package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "TAG: ";
    private FirebaseAuth mAuth;
    private Preference delete;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mAuth = FirebaseAuth.getInstance();
        Preference delete = (Preference) findPreference("delete");

        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d(TAG, "clicked");

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                        //works but it doesn't force me to log in again
                                        Intent loggedOut = new Intent(getActivity(), MainActivity.class);
                                        SettingsFragment.this.startActivity(loggedOut);

                                    }
                                }
                            });

                return true;
                };
        });
    }




}