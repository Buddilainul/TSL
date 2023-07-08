package com.buddila.tsl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen extends AppCompatActivity {

    //Declaring variable
    ImageButton userDetails;
    ImageView planTrip, findPlace, bookCab, bookHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //Transparent status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        userDetails = findViewById(R.id.userdata);
        planTrip = findViewById(R.id.imageView);
        bookCab = findViewById(R.id.imageView3);



        userDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserDetailsScreen();
            }
        });

        planTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlanATripScreen();
            }
        });

        bookCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBookCabScreen();
            }
        });
    }

    public void openUserDetailsScreen(){
        Intent intent = new Intent (this, UserDetailsScreen.class);
        startActivity(intent);
    }
    public void openPlanATripScreen(){
        Intent intent = new Intent (this, PlanATripFirstScreen.class);
        startActivity(intent);
    }
    public void openBookCabScreen(){
        Intent intent = new Intent (this, FindACabFirstScreen.class);
        startActivity(intent);
    }
}