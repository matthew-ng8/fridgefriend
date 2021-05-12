package com.google.example.fridgefriend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AddGroup extends AppCompatActivity {

    private static final int BARCODE_REQUEST = 10;
    private TextView text, test;
    private EditText addGroup;
    private boolean checked = false;
    private String idOneString = "";

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

        final Context homePageContext = getApplicationContext();

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


        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent();

                newIntent.putExtra("NewG", addGroup.getText().toString());
                setResult(fragment_home_page.NEW_ENTRY, newIntent);
                finish();
            }
        });

         */

    }

    private void  checkUUID(){
        //this is for show QR Code

        // so the code here needs to be changed so that
        //grab user's groups from firebase, which is a uuid.toString();
        //,child("groups") then use on data change to find it. If there isn't, send out a toast.
        while (!checked) {
            UUID idOne = UUID.randomUUID();
            idOneString = idOne.toString();
            FirebaseData.firebaseData.getFridgeGroup().child("groupID").setValue("groupID");
            //for user
            final DatabaseReference dataRefUser = FirebaseData.firebaseData.getFridgeGroup().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            //for all groups possible
            final DatabaseReference dataRef = FirebaseData.firebaseData.getFridgeGroup().child("groupID");

            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild(idOneString)) {
                        // The child doesn't exist
                        dataRef.child(idOneString).setValue(addGroup.getText());
                        dataRefUser.child(idOneString).setValue(addGroup.getText());

                        createQRCode(idOneString);

                        checked = true;
                    } else {
                        checked = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    /** TODO: resize the dimensions of the QR Code to matech the image view
     *
     */
    private void createQRCode(String input){
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Bitmap bitmap;
        //String friendCode = "QWERTY";
        QRGEncoder qrgEncoder;

        final View friendCodePopView = getLayoutInflater().inflate(R.layout.qrview, null);
        ImageView qrShow = (ImageView)findViewById(R.id.qrShow);
        //TextView qrText = friendCodePopView.findViewById(R.id.qrCode);
        Button qrClose = (Button)findViewById(R.id.close);



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
        //qrText.setText(friendCode);

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

    /*
    private void scanQRCode(){

        //this doesn't go back to fragment main
        Intent intent = new Intent();
        intent.putExtra("getQR", AddGroup.this,CameraView.class);

        setResult(CameraView.QR_SCAN, intent);
        finish();

        //and im confused :(
        startActivityForResult(intent, QR_SCAN);

    }
*/

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
