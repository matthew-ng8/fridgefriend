package com.google.example.fridgefriend;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_home_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_home_page extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int NEW_ENTRY = 2;
    static final int EXISTING_ENTRY = 3;

    private static final String TAG = "CustomAuthActivity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView group1, group2, group3;
    private FloatingActionButton add;
    private FirebaseAuth mAuth;
    private ArrayList<String> groupList = new ArrayList<String>();


    public fragment_home_page() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home_page.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_home_page newInstance(String param1, String param2) {
        fragment_home_page fragment = new fragment_home_page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference localRef = FirebaseDatabase.getInstance().getReference();
        FirebaseData.firebaseData.setFridgeList(new FridgeList());
        localRef = FirebaseData.firebaseData.getFridgeGroup();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View homeP = inflater.inflate(R.layout.fragment_home_page, container, false);
        group1 = (TextView) homeP.findViewById(R.id.group1);
        group2 = (TextView) homeP.findViewById(R.id.group2);
        group3 = (TextView) homeP.findViewById(R.id.group3);
        add = (FloatingActionButton) homeP.findViewById(R.id.add_fridge_friend);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFridgeGroup();
                Log.d(TAG, "here pressued blue");
            }
        });

        group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstGroup();
            }
        });
        //grab the groups from firebase here



        return homeP;
    }


    private void addFridgeGroup(){
    Log.d(TAG, "test i'm here not in intent yet");
        Intent intent = new Intent(getActivity(),AddGroupView.class );
        Log.d(TAG, "test after intent yet");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == NEW_ENTRY){
            final String fieldNew = data.getParcelableExtra("NewG");
            final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(fieldNew)) {
                        // The child doesn't exist
                        //dataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(fieldNew);
                        groupList.add(fieldNew);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "fridgegroup already exists. Enter a new name.", Toast.LENGTH_LONG + Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else if(requestCode == EXISTING_ENTRY){
            final String fieldOld = data.getParcelableExtra("oldG");
            final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(fieldOld)) {
                        // The child doesn't exist
                        Toast.makeText(getActivity(), "fridgegroup doesn't exist. Would you like to make a new one?", Toast.LENGTH_LONG + Toast.LENGTH_SHORT);

                    }
                    else
                    {
                        //child exists
                        //dataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(fieldOld);
                        groupList.add(fieldOld);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        updateUI();

    }

    private void updateUI() {
        FirebaseData.firebaseData.getFridgeGroup().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(groupList);

        if(groupList.get(0) != null)
        {
            group1.setText(groupList.get(0));

        }
        if(groupList.get(1) != null)
        {
            group2.setText(groupList.get(1));

        }
        if(groupList.get(2) != null)
        {
            group3.setText(groupList.get(2));

        }

    }


    //this is where i ended, doesn't work
    public void firstGroup(){
        Log.d(TAG, "dorm");
    }

    public void secondGroup(View v){

    }

    public void thirdGroup(View v){

    }
}