package com.google.example.fridgefriend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Point;
import android.os.Bundle;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

//TODO consolidate checkig if a group exists or not into one method
public class AddGroup extends AppCompatActivity {

    private static final int BARCODE_REQUEST = 10;
    private TextView text, test;
    private EditText addGroup;
    private boolean checked = false;
    private String idOneString = "";
    private Button scanExistQR;
    private ArrayList<String> groupList;
    private CoordinatorLayout mCoordLayout;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        activity = this;
        checked = false;
        idOneString = new String();
        //Button showQr = (Button)findViewById(R.id.qrButton);
        //Button scanCode = (Button)findViewById(R.id.qrScan);
        addGroup = (EditText) findViewById(R.id.addGroup);
        Button newGroup = (Button)findViewById(R.id.newGroup);
        Button backButton = (Button)findViewById(R.id.backButton3);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        scanExistQR= (Button)findViewById(R.id.scanExistQR);
        final Context homePageContext = getApplicationContext();
        mCoordLayout = findViewById(R.id.snackbarCoord);
        groupList = new ArrayList<>();
        fillListFromDatabase();
        //figure out how to make a unique string with the field text
        //ex: addGroup.getText()+"#"
        //attach a unique 4 digit number
        //actually, we'll probably use a uuid and the toString() function

        //game plan:
        //generate a code: UUID.randomuuid(); and then check it with firebase if it already exists
        //if it does, generate again (make a function to do the generate and return a boolean
        //if it's true from the method and pass it through createQR code
        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUUID(addGroup.getText().toString());
            }
        });

        /*
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
            }
        });

         */
        scanExistQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CameraView.class);

                intent.putExtra("isQrScan", true);
                startActivityForResult(intent, CameraView.QR_SCAN);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /** takes a groupName, adds the group to the user's list and the map of all groups
     *  A group name can be existing or creating a new group
     *  TODO rename this method to something more descriptive
     * @param groupName name of the group to be added to the user's groups list
     */
    private void  checkUUID(final String groupName){
        //this is for show QR Code

        // so the code here needs to be changed so that
        //grab user's groups from firebase, which is a uuid.toString();
        //,child("groups") then use on data change to find it. If there isn't, send out a toast.

            //UUID idOne = UUID.randomUUID();
            //idOneString = idOne.toString();

        //unique string for QR code



            final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("groupsMap");
            final DatabaseReference dataRefUser = FirebaseData.firebaseData.getMyUserRef();

            //for all groups possible

            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(groupList.size() >= MainActivity.MAX_GROUP_SIZE) {
                        Snackbar snackbar = Snackbar.make(mCoordLayout, "You have exceeded the maximum number of " + MainActivity.MAX_GROUP_SIZE + " groups, please delete one to add a new one", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }else{
                    String code = "";
                    if (!snapshot.exists()) {
                        //There's no map at all on groupsMap
                        HashMap<String, String> map = new HashMap<>();
                        code = generateUniqueCode(groupName);
                        map.put(code, groupName);
                        dataRef.setValue(map);

                    } else {
                        if(snapshot.getValue() instanceof HashMap) {
                            HashMap groupMap = (HashMap) snapshot.getValue();
                            code = "";
                            do{//looks for a unique code not already on the map
                                code = generateUniqueCode(groupName);
                            }
                            while(groupMap.containsKey(code));
                            groupMap.put(code, groupName);
                            dataRef.setValue(groupMap);//put the update map back into database with new user

                        }
                    }

                        groupList.add(code);
                        dataRefUser.child("groups").setValue(groupList);//put the list of groups back on users's place
                        Snackbar snackbar = Snackbar.make(mCoordLayout, groupName + " has been added to your groups!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        createQRCode(code);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private String generateUniqueCode(String groupName){
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toUpperCase();
        Date currentTime = Calendar.getInstance().getTime();
        long time = currentTime.getTime();
        String timeS = String.valueOf(time);
        String code = username.substring(0,2) + groupName.toUpperCase().substring(0,2) + timeS.substring(timeS.length() -3);
         return code;
    }

    private void fillListFromDatabase() {
        final DatabaseReference dataRefUser = FirebaseData.firebaseData.getMyUserRef().child("groups");;
        //groupList
        dataRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue();
                if(value instanceof ArrayList){
                    groupList = (ArrayList) value;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    /** TODO: resize the dimensions of the QR Code to matech the image view
     *
     */
    //input has to be passed users
    private void createQRCode(String input){
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Bitmap bitmap;
        QRGEncoder qrgEncoder;


        ImageView qrShow = (ImageView)findViewById(R.id.qrShow);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        qrgEncoder = new QRGEncoder(input, null, QRGContents.Type.TEXT, dimen);

        bitmap = qrgEncoder.getBitmap();
        // the bitmap is set inside our image
        // view using .setimagebitmap method.


        qrShow.setImageBitmap(bitmap);
    }


    /**
     *  TODO add error checking when adding group incase user error when manual entry
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String friendCode  = null;
        if(requestCode == CameraView.QR_SCAN && resultCode == RESULT_OK){
            Barcode barcode = data.getParcelableExtra("barcodeTag");
            friendCode = barcode.displayValue.toUpperCase();

        }
        else if(requestCode == CameraView.QR_SCAN && resultCode == FridgeList.MANUAL_ENTRY){
            Bundle bundle = data.getExtras();
            //String text = data.getStringExtra("com.example.testing.Product");
            //String text = (String)bundle.getString("Product");
           friendCode = data.getStringExtra("Product").toUpperCase();
        }

        if(friendCode == null){
            Snackbar snackbar = Snackbar.make(mCoordLayout, "An error has occurred trying to add this group, please try again", Snackbar.LENGTH_LONG);
            snackbar.show();
        }else {
            if (groupList.size() >= MainActivity.MAX_GROUP_SIZE) {
                Snackbar snackbar = Snackbar.make(mCoordLayout, "You have exceeded the maximum number of " + MainActivity.MAX_GROUP_SIZE + " groups, please delete one to add a new one", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                //check if already in the list, if not add it
                if (!groupList.contains(friendCode)) {
                    groupList.add(friendCode);
                    FirebaseData.firebaseData.getMyUserRef().child("groups").setValue(groupList);
                    Snackbar snackbar = Snackbar.make(mCoordLayout, "Successfully been added", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordLayout, "You're already in this group!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        }

    }
}
