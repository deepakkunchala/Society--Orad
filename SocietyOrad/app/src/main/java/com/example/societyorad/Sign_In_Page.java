package com.example.societyorad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;

public class Sign_In_Page extends AppCompatActivity {
    EditText jSignin_Email, jSignin_Password;
    TextView jSignin_Back,jSignin_forgotPassword,jSignin_register;
    Button jSignin_Signin;
    ImageView jSignin_show, jSignin_hide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);
        jSignin_Email = findViewById(R.id.Signin_Email);
        jSignin_Back = findViewById(R.id.Signin_Back);
        jSignin_forgotPassword=findViewById(R.id.Signin_forgot_password);
        jSignin_Signin =findViewById(R.id.Signin_Signin);
        jSignin_show = findViewById(R.id.Signin_show);
        jSignin_hide = findViewById(R.id.Signin_hide);
        jSignin_Password = findViewById(R.id.Signin_Password);
        jSignin_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In_Page.this,ForgotPassword_Page.class);
                startActivity(intent);
            }
        });
        jSignin_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In_Page.this,Welcome_Page.class);
                startActivity(intent);
                finish();
            }
        });
        jSignin_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jSignin_Password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                jSignin_hide.setVisibility(View.VISIBLE);
                jSignin_hide.setClickable(true);
                jSignin_show.setClickable(false);
                jSignin_show.setVisibility(View.INVISIBLE);

            }
        });
        jSignin_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jSignin_Password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                jSignin_hide.setVisibility(View.INVISIBLE);
                jSignin_hide.setClickable(false);
                jSignin_show.setClickable(true);
                jSignin_show.setVisibility(View.VISIBLE);

            }
        });
        jSignin_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jSignin_Email.getText().toString().trim().isEmpty()||jSignin_Password.getText().toString().trim().isEmpty()){
                    Toast.makeText(Sign_In_Page.this, "Please fill all the Credentials", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher (jSignin_Email.getText().toString().trim()).matches()){
                    Toast.makeText(Sign_In_Page.this, "Please enter Valid Email Id", Toast.LENGTH_SHORT).show();
                    jSignin_Email.requestFocus();
                }
                else {
                    verify_Signin();
                }
            }
        });



    }

    private void verify_Signin() {
        String mail,pass;
        mail= jSignin_Email.getText().toString().trim();
        pass = jSignin_Password.getText().toString().trim();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Query checkUsermail = reference.orderByChild("email").equalTo(mail);
        checkUsermail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UserKey=
                        "MhNcWnDG6POykdjy49-";

                if(snapshot.exists()){
                        for (DataSnapshot childsnapshot :snapshot.getChildren()){
                             UserKey = childsnapshot.getKey();
                        }
                        String Retrived_Password = snapshot.child(UserKey).child("password").getValue(String.class);


                    if(Retrived_Password.equals(pass)) {
                        SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("dataexists","true");
                        editor.putString("UserKey",UserKey);
                        editor.putString("email",snapshot.child(UserKey).child("email").getValue(String.class));
                        editor.putString("mobile",snapshot.child(UserKey).child("phone").getValue(String.class));
                        editor.putString("name",snapshot.child(UserKey).child("name").getValue(String.class));
                        editor.putString("Password",snapshot.child(UserKey).child("password").getValue().toString());
                        editor.putString("volunteer",snapshot.child(UserKey).child("volunteer").getValue().toString());
                        editor.apply();
                        Toast.makeText(Sign_In_Page.this, "Signin Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Sign_In_Page.this,MainActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                    else {
                        Toast.makeText(Sign_In_Page.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Sign_In_Page.this, "User not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}