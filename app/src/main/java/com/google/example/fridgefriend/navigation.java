package com.google.example.fridgefriend;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/** TODO: implement the spinner onitem selected stuff, style the Spinner more accurately, maybe move some items into onCreateView
 *
 */
public class navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    //BottomNavigationView.OnNavigationItemSelectedListener
    private NavController navController;
    private Toolbar toolbar;
    private Switch aSwitch;
    private boolean usePersonalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);//gets overwritten when i set up the NavigationUI
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();//gets host fragments controller


        //NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //NavigationUI.onNavDestinationSelected()

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

       aSwitch = findViewById(R.id.switchWidget);
       aSwitch.setVisibility(View.INVISIBLE);
       aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usePersonalList = isChecked;
                updateGroupAndSubtext();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     *  Updates the title and navigates to the correct fragment
     *  Spinner is visible in the shopping list and the fridge list
     *  Spinner is invisible in the home and settings fragment
     * @param item the menu item selected
     * @return always returns true right now
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String fridgeGroupName = FirebaseData.firebaseData.getFridgeGroupName();
        String fridgeCode = FirebaseData.firebaseData.getFridgeCode();
        if(fridgeCode == null){
            fridgeGroupName = "None :(";
            toolbar.setSubtitle("Pick a Fridge Group From Home");
        }
        if(id == R.id.fridgeList){
            toolbar.setTitle("Fridge List: " + fridgeGroupName);

            updateGroupAndSubtext();

            navController.navigate(R.id.fridgeList);
            aSwitch.setVisibility(View.VISIBLE);
        }else if(id == R.id.fragment_home_page){
            toolbar.setTitle("Home");
            navController.navigate(R.id.fragment_home_page);
            aSwitch.setVisibility(View.INVISIBLE);
        }else if(id == R.id.shoppingList){
            toolbar.setTitle("Shopping List: "  + fridgeGroupName);
            navController.navigate(R.id.shoppingList);
            aSwitch.setVisibility(View.VISIBLE);
            updateGroupAndSubtext();
        }else if(id == R.id.settingsFragment){
            toolbar.setTitle("Settings");
            navController.navigate(R.id.settingsFragment);
            aSwitch.setVisibility(View.INVISIBLE);
        }

        System.out.println(item);
        System.out.println(id);

        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void updateGroupAndSubtext(){
        if (usePersonalList) {
            toolbar.setSubtitle("Group List");
        } else {
            toolbar.setSubtitle("Personal List");
        }
        FirebaseData.firebaseData.updateFridgeGroup(usePersonalList);

    }
}
