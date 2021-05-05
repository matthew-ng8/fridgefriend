package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

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
    private static final String TAG = "CustomAuthActivity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView group1, group2, group3;
    private FloatingActionButton add;
    private FirebaseAuth mAuth;


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

        Intent intent = new Intent(getActivity(),AddGroupView.class );

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