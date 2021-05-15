package com.google.example.fridgefriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "TAG: ";
    private FirebaseAuth mAuth;
    private Preference delete, group;
    private MultiSelectListPreference allergies;
    private Context Context;
    private ArrayList<String> groupList = new ArrayList<String>();
    private ArrayList<String> groupCodes = new ArrayList<String>();



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        mAuth = FirebaseAuth.getInstance();
        delete = (Preference) findPreference("delete");
        group = (Preference) findPreference("group");
        updateUI();
        allergies = (MultiSelectListPreference) findPreference("allergies");

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

        allergies.setSummary(""); // required or will not update
        /*
        allergies.setOnPreferenceChangeListener(new        Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                MultiSelectListPreference mPreference = (MultiSelectListPreference)preference;
                int id = 0;
                for (int i = 0; i < mPreference.getEntryValues().length; i++)
                {
                    if(mPreference.getEntryValues()[i].equals(newValue.toString())){
                        id += i;
                        break;
                    }
                }
                allergies.setSummary(mPreference.getEntries()[id]);
                return true;
            }
        }); */

        allergies.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                MultiSelectListPreference mPreference = (MultiSelectListPreference)preference;
                String summary ="";
               for(int i =0; i<mPreference.getEntryValues().length; i ++)
               {
                summary += mPreference.getEntryValues()[i].toString() + ", ";
               }
                allergies.setSummary(summary);
                return true;
            };
        });
        //group name should be added
        updateUI();

    }


    private void updateUI() {

        //FirebaseData.firebaseData.getFridgeGroup().child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child("groups").setValue(groupList);
        fillListFromDatabase();
        String allGroups = "";
        for (int i = 0; i < groupList.size(); i++) {
            allGroups += groupList.get(i);
        }

        group.setSummary((CharSequence)allGroups);
    }


    private void fillListFromDatabase(){
        DatabaseReference localMyRefGroups = FirebaseData.firebaseData.getMyUserRef().child("groups");

        //Retrieves all the user's groups
        localMyRefGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if(value instanceof ArrayList){
                    groupCodes = (ArrayList) value;

                    //retrieve the map from Firebase Database once and then get the corresponding names
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("/groupsMap");
                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Object value = snapshot.getValue();
                            if(value instanceof HashMap){
                                HashMap groupMap = (HashMap)value;
                                groupList.clear();
                                for(String s : groupCodes){
                                    if(groupMap.containsKey(s)) {
                                        groupList.add((String) groupMap.get(s));
                                    }
                                }
                                updateUI();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


}