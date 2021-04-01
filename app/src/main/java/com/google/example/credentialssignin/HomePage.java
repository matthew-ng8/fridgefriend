package com.google.example.credentialssignin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import org.w3c.dom.Text;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Button showQr = (Button)findViewById(R.id.qrButton);
        TextView text = (TextView)findViewById(R.id.textView2);

        text.setText("HOME PAGE");
        final Context homePageContext = getApplicationContext();

        showQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createQRCode();
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

}
