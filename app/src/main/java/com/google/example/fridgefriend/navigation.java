package com.google.example.fridgefriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //BottomNavigationView.OnNavigationItemSelectedListener
    private NavController navController;
    private Toolbar toolbar;
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



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.fridgeList){
            toolbar.setTitle("Fridge List");
            navController.navigate(R.id.fridgeList);
        }else if(id == R.id.fragment_home_page){
            toolbar.setTitle("Home");
            navController.navigate(R.id.fragment_home_page);
        }else if(id == R.id.shoppingList){
            toolbar.setTitle("Shopping List");
            navController.navigate(R.id.shoppingList);
        }else if(id == R.id.settingsFragment){
            toolbar.setTitle("Settings");
            navController.navigate(R.id.settingsFragment);
        }

        System.out.println(item);
        System.out.println(id);

        return true;
    }
}
