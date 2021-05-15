package com.google.example.fridgefriend;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.WINDOW_SERVICE;

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
    private ArrayList<Button> leaveButtonGroup = new ArrayList<>();



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
        FirebaseData.firebaseData.setMyUserRef(FirebaseDatabase.getInstance().getReference().child("users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName()));

        //listener for when groups change and gets the current database
        fillListFromDatabase();


        //TODO delete all below for testing when submitting
        /*HashMap<String, String> testmap = new HashMap<>();
        testmap.put("QWERT", "homeGroup1");
        testmap.put("ASDF", "dormGroup3");
        localRef.child(")groupsMap".setValue(testmap);


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


        final Button leaveButton1 = (Button)homeP.findViewById(R.id.leaveGroup1);
        Button leaveButton2 = (Button)homeP.findViewById(R.id.leaveGroup2);
        Button leaveButton3 = (Button)homeP.findViewById(R.id.leaveGroup3);
        leaveButtonGroup.add(leaveButton1);
        leaveButtonGroup.add(leaveButton2);
        leaveButtonGroup.add(leaveButton3);


        //doesn't delete existing data
        //updateTextViews();
        updateUI();
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
                createQRCode(groupCodes.get(0));
            }
        });
        group2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQRCode(groupCodes.get(1));
            }
        });
        group3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQRCode(groupCodes.get(2));
            }
        });


        leaveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup(0);
            }
        });
        leaveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup(1);
            }
        });
        leaveButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveGroup(2);
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



    private void updateUI() {
        //FirebaseData.firebaseData.getMyUserRef().child("groups").setValue(groupList);
        for(int i =0; i < MainActivity.MAX_GROUP_SIZE;i++){
            buttonGroup.get(i).setVisibility(View.INVISIBLE);
            textGroup.get(i).setVisibility(View.INVISIBLE);
            leaveButtonGroup.get(i).setVisibility(View.INVISIBLE);
        }

        for(int i = 0; i <groupList.size(); i++)
        {
            textGroup.get(i).setText(groupList.get(i));

            textGroup.get(i).setVisibility(View.VISIBLE);
            buttonGroup.get(i).setVisibility(View.VISIBLE);
            leaveButtonGroup.get(i).setVisibility(View.VISIBLE);

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
        FirebaseData.firebaseData.setFridgeCode(groupCodes.get(0));
        FirebaseData.firebaseData.setGroupIndexSelected(0);

    }

    public void secondGroup(){
        Toast.makeText(getActivity(), "You selected: " + group2.getText(), Toast.LENGTH_LONG).show();
        FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference().child("groups" + "/" + groupCodes.get(1)));
        FirebaseData.firebaseData.setFridgeGroupName(group2.getText().toString());
        FirebaseData.firebaseData.setFridgeCode(groupCodes.get(1));
        FirebaseData.firebaseData.setGroupIndexSelected(1);
    }

    public void thirdGroup(){
        Toast.makeText(getActivity(), "You selected: " + group3.getText(), Toast.LENGTH_LONG).show();
        FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference().child("groups" + "/" + groupCodes.get(2)));
        FirebaseData.firebaseData.setFridgeGroupName(group3.getText().toString());
        FirebaseData.firebaseData.setFridgeCode(groupCodes.get(2));
        FirebaseData.firebaseData.setGroupIndexSelected(2);
    }

    private void createQRCode(String friendCode){
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        Bitmap bitmap;
        QRGEncoder qrgEncoder;
        final View friendCodePopView = getLayoutInflater().inflate(R.layout.friend_group_code, null);
        ImageView qrView = friendCodePopView.findViewById(R.id.qrView);
        TextView qrText = friendCodePopView.findViewById(R.id.qrCode);
        Button qrClose = (Button)friendCodePopView.findViewById(R.id.qrClose);
        WindowManager manager = (WindowManager) requireActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        qrgEncoder = new QRGEncoder(friendCode, null, QRGContents.Type.TEXT, dimen);

        bitmap = qrgEncoder.getBitmap();
        // the bitmap is set inside our image
        // view using .setimagebitmap method.


        qrView.setImageBitmap(bitmap);
        qrText.setText(friendCode);

        dialogBuilder.setView(friendCodePopView);
        dialog = dialogBuilder.create();
        qrClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Log.d("HomePage", "End of button press");




    }


    //TODO delete the personal lists from database
    // if the user has selected a group that you delete, need to set it to null
    private void leaveGroup(int groupIndex){
        int groupIndexSelected = FirebaseData.firebaseData.getGroupIndexSelected();
        if(groupIndex == groupIndexSelected){
            FirebaseData.firebaseData.setFridgeCode(null);
            FirebaseData.firebaseData.updateFridgeGroup(true);
        }
        String code = groupCodes.remove(groupIndex);
        groupList.remove(groupIndex);
        FirebaseData.firebaseData.getMyUserRef().child("groups").setValue(groupCodes);

        updateUI();
    }
    //General Issues:
    /*
    App will stop responding when multiple groups are added. UI doesn't update when user first logs in (Maybe create another method for that.
    Need to check if firebase data is in the right path
    When it goes back to Home, UI is gone
     */
}