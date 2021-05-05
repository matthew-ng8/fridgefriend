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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class HomePage extends AppCompatActivity {

    private static final int BARCODE_REQUEST = 10;
    private TextView text, test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Button showQr = (Button)findViewById(R.id.qrButton);
        Button scanCode = (Button)findViewById(R.id.qrScan);
        text = (TextView)findViewById(R.id.textView2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        text.setText("HOME PAGE");
        final Context homePageContext = getApplicationContext();

        showQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createQRCode();
            }
        });
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
            }
        });

    }

    /** TODO: resize the dimensions of the QR Code to matech the image view
     *
     */
    private void createQRCode(){
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        Bitmap bitmap;
        String friendCode = "QWERTY";
        QRGEncoder qrgEncoder;
        final View friendCodePopView = getLayoutInflater().inflate(R.layout.friend_group_code, null);
        ImageView qrView = friendCodePopView.findViewById(R.id.qrView);
        TextView qrText = friendCodePopView.findViewById(R.id.qrCode);
        Button qrClose = (Button)friendCodePopView.findViewById(R.id.qrClose);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
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

    private void scanQRCode(){

        Intent intent = new Intent(HomePage.this,CameraView.class );

        startActivityForResult(intent, BARCODE_REQUEST);

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
