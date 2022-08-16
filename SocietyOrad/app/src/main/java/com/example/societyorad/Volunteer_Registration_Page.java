package com.example.societyorad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Volunteer_Registration_Page extends AppCompatActivity {

    TextInputEditText age,gender,Address,pincode,city,state,alternate_mobile,Alternate_email;

    Button cancel,submit;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_registration_page);
        age=findViewById(R.id.VR_Age);
        gender= findViewById(R.id.VR_Gender);
        Address=findViewById(R.id.VR_Address);
        pincode=findViewById(R.id.VR_pincode);
        city=findViewById(R.id.VR_City);
        state=findViewById(R.id.VR_State);
        alternate_mobile=findViewById(R.id.VR_alternatemobile);
        Alternate_email=findViewById(R.id.VR_Alternateemail);
        cancel=findViewById(R.id.cancel);
        submit=findViewById(R.id.submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Volunteer_Registration_Page.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkrequirements();
            }
        });




    }

    private void checkrequirements() {
        if(age.getText().toString().isEmpty()||gender.getText().toString().isEmpty()||Address.getText().toString().isEmpty()||pincode.getText().toString().isEmpty()||city.getText().toString().isEmpty()||state.getText().toString().isEmpty()||alternate_mobile.getText().toString().isEmpty()||Alternate_email.getText().toString().isEmpty()){
            Toast.makeText(Volunteer_Registration_Page.this, "Please fill all the Credentials!", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(age.getText().toString()) < 18 ||Integer.parseInt(age.getText().toString())>60 ){
            Toast.makeText(Volunteer_Registration_Page.this, "Age must be in range of 18 and 60", Toast.LENGTH_SHORT).show();
            age.requestFocus();
        }
        /*else if (!gender.getText().toString().trim().equals("male")||!gender.getText().toString().equals("female")){
            Toast.makeText(Volunteer_Registration_Page.this, "Gender must be either male or female ", Toast.LENGTH_SHORT).show();
            gender.requestFocus();
        }*/
        else if (!(pincode.getText().toString().length()==6)||(pincode.getText().toString().toCharArray()[0]==0)){
            Toast.makeText(Volunteer_Registration_Page.this, "Invalid Pincode!", Toast.LENGTH_SHORT).show();
        }

        else if(!Patterns.PHONE.matcher(alternate_mobile.getText().toString()).matches()){
            Toast.makeText(Volunteer_Registration_Page.this, "please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(Alternate_email.getText().toString()).matches()){
            Toast.makeText(Volunteer_Registration_Page.this, "please enter valid email address", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(Volunteer_Registration_Page.this, "Happy :)", Toast.LENGTH_SHORT).show();
            senddata();
        }
    }

    private void senddata() {
        reference= FirebaseDatabase.getInstance().getReference("Users");
        SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String key = preferences.getString("UserKey"," ");
        reference.child(key).child("age").setValue(age.getText().toString());
        reference.child(key).child("gender").setValue(gender.getText().toString());
        reference.child(key).child("pincode").setValue(pincode.getText().toString());
        reference.child(key).child("city").setValue(city.getText().toString());
        reference.child(key).child("state").setValue(state.getText().toString());
        reference.child(key).child("address").setValue(Address.getText().toString());
        reference.child(key).child("alternatemobile").setValue(alternate_mobile.getText().toString());
        reference.child(key).child("alternatemail").setValue(Alternate_email.getText().toString());
        reference.child(key).child("volunteer").setValue("pending");
        editor.putString("volunteer","pending");
        Toast.makeText(Volunteer_Registration_Page.this, "data updated", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dig = new AlertDialog.Builder(Volunteer_Registration_Page.this)
                .setTitle("Request Sent")
                .setMessage("We have received your request of becoming a volunteer.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Volunteer_Registration_Page.this,MainActivity.class);
                        startActivity(intent);

                    }
                });
        dig.show();




    }
}



