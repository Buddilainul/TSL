package com.buddila.tsl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LogInScreen extends AppCompatActivity {

    //Declaring variable
    Button loginBtn;
    EditText editTextEmail, editTextPassword;
    ProgressBar loginLoading;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        //Transparent status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Initialise variable
        backBtn = findViewById(R.id.backbutton);
        loginBtn = findViewById(R.id.login_btn);
        editTextEmail = findViewById(R.id.emailinput);
        editTextPassword = findViewById(R.id.passwordlinput);
        loginLoading = findViewById(R.id.progressBarLogin);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLoading.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LogInScreen.this, "Enter email", Toast.LENGTH_SHORT).show();
                    loginLoading.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LogInScreen.this, "Enter password", Toast.LENGTH_SHORT).show();
                    loginLoading.setVisibility(View.GONE);
                    return;
                }

                //Firebase Login account
                fAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loginLoading.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LogInScreen.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loginLoading.setVisibility(View.GONE);
                        checkUserType(authResult.getUser().getUid());
                    }
                });
            }
        });
    }
    private void checkUserType(String uid) {
        DocumentReference df = fStore.collection("UserType").document(uid);
        //Extract data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onComplete" + documentSnapshot.getData());
                //identify the user type
                if(Objects.equals(documentSnapshot.getString("UserType"), "User")){
                    //User login
                    openHomeScreen();
                    finish();
                }else {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "Wrong User Type",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openHomeScreen(){
        Intent intent = new Intent (this, HomeScreen.class);
        startActivity(intent);
    }
    public void openMain(){
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}