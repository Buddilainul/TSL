package com.buddila.tsl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlanATripFirstScreen extends AppCompatActivity {

    //Declaring variable
    ImageButton backBtn, userDetails;
    FloatingActionButton fab;
    EditText newTrip;
    Button creatBtn;
    Dialog popUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_atrip_first_screen);

        //Transparent status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Initialize variable
        backBtn = findViewById(R.id.backbutton);
        userDetails = findViewById(R.id.userdata);
        fab = findViewById(R.id.fab);
        popUpDialog = new Dialog(this);


        //Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //user details screen open
        userDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserDetailsScreen();
            }
        });
        //open pop up new trip plan window
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpWindow();
            }
        });


    }

    private void openPopUpWindow() {
        popUpDialog.setContentView(R.layout.add_new_trip);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(popUpDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        popUpDialog.getWindow().setAttributes(layoutParams);

        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();

        newTrip  = popUpDialog.findViewById(R.id.newTrip);
        creatBtn  = popUpDialog.findViewById(R.id.creatbtn);

        creatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // New trip creating process
            }
        });
    }

    public void openUserDetailsScreen(){
        Intent intent = new Intent (this, UserDetailsScreen.class);
        startActivity(intent);
    }

}