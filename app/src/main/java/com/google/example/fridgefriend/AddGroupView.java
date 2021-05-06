package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddGroupView extends AppCompatActivity {

    private Button enterG, enterNew;
    private EditText fieldG, fieldNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addgroupview);
        Button enterG = (Button) findViewById(R.id.enterGroup);
        Button enterNew = (Button) findViewById(R.id.newGroup);
        final EditText fieldG = (EditText) findViewById(R.id.existingGroup);
        final EditText fieldNew = (EditText) findViewById(R.id.addGroup);

        enterG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent enterIntent = new Intent();

                enterIntent.putExtra("oldG", fieldG.getText().toString());
                setResult(fragment_home_page.EXISTING_ENTRY, enterIntent);
                finish();
            }
        });
        //we should be using the QR code, will be using a string instead for now
        enterNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent();

                newIntent.putExtra("NewG", fieldNew.getText().toString());
                setResult(fragment_home_page.NEW_ENTRY, newIntent);
                finish();
            }
        });


    }


}
