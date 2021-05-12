package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/** TODO: implement the spinner onitem selected stuff, style the Spinner more accurately, maybe move some items into onCreateView
 *
 */
public class navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //BottomNavigationView.OnNavigationItemSelectedListener
    private NavController navController;
    private Toolbar toolbar;
    private Spinner spinner;
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

        spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayList<String> listArray = new ArrayList<>();
        listArray.add("FridgeGroup: Home");
        listArray.add("FridgeGroup: Private");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listArray);
        spinner.setVisibility(View.INVISIBLE);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



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
        if(id == R.id.fridgeList){
            toolbar.setTitle("Fridge List: " + FirebaseData.firebaseData.getFridgeGroupName());
            navController.navigate(R.id.fridgeList);
            spinner.setVisibility(View.VISIBLE);
        }else if(id == R.id.fragment_home_page){
            toolbar.setTitle("Home");
            navController.navigate(R.id.fragment_home_page);
            spinner.setVisibility(View.INVISIBLE);
        }else if(id == R.id.shoppingList){
            toolbar.setTitle("Shopping List: "  + FirebaseData.firebaseData.getFridgeGroupName());
            navController.navigate(R.id.shoppingList);
            spinner.setVisibility(View.VISIBLE);
        }else if(id == R.id.settingsFragment){
            toolbar.setTitle("Settings");
            navController.navigate(R.id.settingsFragment);
            spinner.setVisibility(View.INVISIBLE);
        }

        System.out.println(item);
        System.out.println(id);

        return true;
    }
}
