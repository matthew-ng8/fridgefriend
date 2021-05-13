package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    static final int CODE = 10;

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
    private ArrayList<String> groupCodes = new ArrayList<String>();
    private ArrayList<TextView> textGroup = new ArrayList<TextView>();
    private ArrayList<Button> buttonGroup = new ArrayList<Button>();



    private Button group1Button, group2Button, group3Button;

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


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();

        //set user reference
        FirebaseData.firebaseData.setMyUserRef(FirebaseDatabase.getInstance().getReference().child("users/" +FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));

        //listener for when groups change and gets the current database
        fillListFromDatabase();


        //TODO delete all below for testing when submitting
        /*HashMap<String, String> testmap = new HashMap<>();
        testmap.put("QWERT", "homeGroup1");
        testmap.put("ASDF", "dormGroup3");
        localRef.child("groupsMap").setValue(testmap);


        ArrayList<String> shoppingListArray = new ArrayList<>(Arrays.asList("Peabuts", "Chocolate", "Melons"));
        localRef.child("groupsTesting/QWERT/shoppinglist").setValue(shoppingListArray);

        ArrayList<String> fridgeListArray = new ArrayList<>(Arrays.asList("ramen", "milk", "beans"));
        localRef.child("groupsTesting/QWERT/fridgeList").setValue(fridgeListArray);*/


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View homeP = inflater.inflate(R.layout.fragment_home_page, container, false);
        group1 = (TextView) homeP.findViewById(R.id.group1);
        group2 = (TextView) homeP.findViewById(R.id.group2);
        group3 = (TextView) homeP.findViewById(R.id.group3);

        group1Button = (Button) homeP.findViewById(R.id.group1Show);
        group2Button = (Button) homeP.findViewById(R.id.group2Show);
        group3Button = (Button) homeP.findViewById(R.id.group3Show);

        add = (FloatingActionButton) homeP.findViewById(R.id.add_fridge_friend);
        add.setClickable(true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFridgeGroup();
            }
        });


        textGroup.add(group1);
        textGroup.add(group2);
        textGroup.add(group3);

        buttonGroup.add(group1Button);
        buttonGroup.add(group2Button);
        buttonGroup.add(group3Button);


        //doesn't delete existing data
        updateTextViews();

        group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstGroup();
            }
        });
        group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondGroup();
            }
        });
        group3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thirdGroup();
            }
        });
        //grab the groups from firebase here

        group1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return homeP;
    }


    private void addFridgeGroup(){
    Log.d(TAG, "test i'm here not in intent yet");
        Intent intent = new Intent(getActivity(), AddGroup.class );
        startActivityForResult(intent, CODE);

    }

    /** Fills the groupCodes array with values from /root/users/userName/groups
     *  Changes in database result in:
     *      - a call to updateUI()
     *      - groupMap retrieved from /root/groupsMap to identify the group name from the group code
     *
     */
    private void fillListFromDatabase(){
        DatabaseReference localMyRefGroups = FirebaseData.firebaseData.getMyUserRef().child("groups");

        //Retrieves all the user's groups
        localMyRefGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if(value instanceof ArrayList){
                    groupCodes = (ArrayList) value;
                    updateUI();
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



    private void updateUI() {
        FirebaseData.firebaseData.getMyUserRef().child("groups").setValue(groupList);

        for(int i = 0; i <groupList.size(); i++)
        {
            textGroup.get(i).setText(groupList.get(i));
            if(groupList.get(i).length()>1 ){
                buttonGroup.get(i).setVisibility(View.VISIBLE);
            }
            else
            {
                buttonGroup.get(i).setVisibility(View.INVISIBLE);

            }
        }


    }

    private void updateTextViews() {
        //fillListFromDatabase();
        for(int i = 0; i <groupList.size(); i++)
        {
            textGroup.get(i).setText(groupList.get(i));
        }


    }



//adds blanks for now
    //path to a fridgeGroup would be root/groups/groupCode

    /**TODO add an array to keep track of the groupCodes so there's two arrays?
     * One array to keep track of the group names and one to keep track of the codes
     * */



    public void firstGroup(){
        Log.d(TAG, "first group");
        Toast.makeText(getActivity(), "You selected: " + group1.getText(), Toast.LENGTH_LONG).show();

        //FirebaseData.firebaseData.setFridgeGroup(FirebaseData.firebaseData.getFridgeGroup().child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child("groups").child((String) group1.getText()));
        FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference().child("groups" + "/" + groupCodes.get(0)));
        FirebaseData.firebaseData.setFridgeGroupName(group1.getText().toString());

    }

    public void secondGroup(){
        Toast.makeText(getActivity(), "You selected: " + group2.getText(), Toast.LENGTH_LONG).show();
        FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference().child("groups" + "/" + groupCodes.get(1)));
        FirebaseData.firebaseData.setFridgeGroupName(group1.getText().toString());

    }

    public void thirdGroup(){
        Toast.makeText(getActivity(), "You selected: " + group3.getText(), Toast.LENGTH_LONG).show();
        FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference().child("groups" + "/" + groupCodes.get(2)));
        FirebaseData.firebaseData.setFridgeGroupName(group1.getText().toString());

    }

    //General Issues:
    /*
    App will stop responding when multiple groups are added. UI doesn't update when user first logs in (Maybe create another method for that.
    Need to check if firebase data is in the right path
    When it goes back to Home, UI is gone
     */
}