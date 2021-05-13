package com.google.example.fridgefriend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Point;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;
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

public class AddGroup extends AppCompatActivity {

    private static final int BARCODE_REQUEST = 10;
    private TextView text, test;
    private EditText addGroup;
    private boolean checked = false;
    private String idOneString = "";
    private Button scanExistQR;
    private String code;
    private ArrayList<String> groupList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        checked = false;
        idOneString = new String();
        //Button showQr = (Button)findViewById(R.id.qrButton);
        //Button scanCode = (Button)findViewById(R.id.qrScan);
        addGroup = (EditText) findViewById(R.id.addGroup);
        Button newGroup = (Button)findViewById(R.id.newGroup);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        scanExistQR= (Button)findViewById(R.id.scanExistQR);
        final Context homePageContext = getApplicationContext();

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
                checkUUID();
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
                Intent newIntent = new Intent();

                newIntent.putExtra("QR", "scan");
                startActivityForResult(newIntent, CameraView.QR_SCAN);
            }
        });

    }

    private void  checkUUID(){
        //this is for show QR Code

        // so the code here needs to be changed so that
        //grab user's groups from firebase, which is a uuid.toString();
        //,child("groups") then use on data change to find it. If there isn't, send out a toast.

            //UUID idOne = UUID.randomUUID();
            //idOneString = idOne.toString();

        //unique string for QR code
            String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toUpperCase();
            String groupname = addGroup.getText().toString().toUpperCase();
            Date currentTime = Calendar.getInstance().getTime();
            long time = currentTime.getTime();
            String timeS = String.valueOf(time);
            code = username.substring(0,2) + groupname.substring(0,2) + timeS.substring(timeS.length() -3);


            final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("groupsMap");
            final DatabaseReference dataRefUser = FirebaseData.firebaseData.getMyUserRef();

            //for all groups possible

            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        // The child doesn't exist
                        HashMap<String, String> entry = new HashMap<>();
                        entry.put(code, addGroup.getText().toString());
                        dataRef.setValue(entry);

                        groupList.add(code);
                        dataRefUser.child("groups").setValue(groupList);;

                        createQRCode(code);
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void fillListFromDatabase() {
        final DatabaseReference dataRefUser = FirebaseData.firebaseData.getMyUserRef();
        //groupList
        dataRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BARCODE_REQUEST && resultCode == RESULT_OK){
            Barcode barcode = data.getParcelableExtra("barcodeTag");
            text.setText(barcode.displayValue);
            Log.d("Testing stuff", barcode.displayValue);
        }

    }
}
