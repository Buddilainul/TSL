package com.buddila.tsl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {

    //Declaring variable
    Button nextBtn;
    EditText editTextFirstName, editTextLastName, dateFormat, editTextEmail, editTextPassword, editTextPhoneNumber;
    RadioButton maleBtn, femaleBtn;
    ProgressBar registerLoading;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ImageButton backBtn;
    boolean valid;

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        //Transparent status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Initialise variable
        backBtn = findViewById(R.id.backbutton);
        nextBtn = findViewById(R.id.nextbtn_register);
        editTextFirstName = findViewById(R.id.editText1);
        editTextLastName = findViewById(R.id.editText2);
        dateFormat = findViewById(R.id.date_of_birth);
        editTextEmail = findViewById(R.id.editText4);
        editTextPhoneNumber = findViewById(R.id.editText5);
        editTextPassword = findViewById(R.id.editText6);
        registerLoading = findViewById(R.id.progressBarRegister);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Back Button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Date of Birth calendar pop up procedure
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int date = calendar.get(Calendar.DAY_OF_MONTH);

        dateFormat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterScreen.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        dateFormat.setText(date);
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                checkField(editTextFirstName);
                checkField(editTextLastName);
                checkField(dateFormat);
                checkField(editTextEmail);
                checkField(editTextPhoneNumber);
                checkField(editTextPassword);

                if (valid){
                    //openVerification_screen(); this is the one open verification activity
                    registerLoading.setVisibility(View.VISIBLE);
                    String email, password;
                    email = String.valueOf(editTextEmail.getText());
                    password = String.valueOf(editTextPassword.getText());

                    if (TextUtils.isEmpty(email)){
                        Toast.makeText(RegisterScreen.this, "Enter email", Toast.LENGTH_SHORT).show();
                        registerLoading.setVisibility(View.GONE);
                        return;
                    }
                    if (TextUtils.isEmpty(password)){
                        Toast.makeText(RegisterScreen.this, "Enter password", Toast.LENGTH_SHORT).show();
                        registerLoading.setVisibility(View.GONE);
                        return;
                    }

                    //Firebase create account
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            registerLoading.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                Toast.makeText(RegisterScreen.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();

                                DocumentReference df = fStore.collection("UserType").document(user.getUid());
                                Map<String,Object> userInfo = new HashMap<>();
                                userInfo.put("FirstName",editTextFirstName.getText().toString());
                                userInfo.put("LastName",editTextLastName.getText().toString());
                                userInfo.put("BirthDay",dateFormat.getText().toString());
                                userInfo.put("Email",editTextEmail.getText().toString());
                                userInfo.put("PhoneNumber",editTextPhoneNumber.getText().toString());
                                //specify user type
                                userInfo.put("UserType", "User");
                                df.set(userInfo);

                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


    public void openVerification_screen(){
        Intent intent = new Intent (this, VerificationScreen.class);
        startActivity(intent);
    }

    private boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()){
            textField.setError("Please enter data");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    }

}