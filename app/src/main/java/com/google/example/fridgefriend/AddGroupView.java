package com.google.example.fridgefriend;

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
        EditText fieldG = (EditText) findViewById(R.id.existingGroup);
        EditText fieldNew = (EditText) findViewById(R.id.addGroup);

        enterG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        enterNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });






    }
}
