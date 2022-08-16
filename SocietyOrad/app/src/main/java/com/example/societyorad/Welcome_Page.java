package com.example.societyorad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_Page extends AppCompatActivity {
        TextView jMarquee;
        TextView jwebsite;
        Button jregister;
        Button jsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        jMarquee=findViewById(R.id.Welcome_Marquee);
        jregister =findViewById(R.id.Welcome_Register);
        jsignin = findViewById(R.id.Welcome_SignIn);
        jwebsite = findViewById(R.id.Welcome_website);
        jMarquee.setSelected(true);

        jregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Welcome_Page.this, "Sending to Registration Page", Toast.LENGTH_SHORT).show();
                Intent registerintent = new Intent(Welcome_Page.this,Registration_Page.class);
                startActivity(registerintent);
                finish();
            }
        });
        jwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Welcome_Page.this, "Sending to website", Toast.LENGTH_SHORT).show();
            }
        });
        jsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Welcome_Page.this, "Sending to signin page", Toast.LENGTH_SHORT).show();
                Intent signin = new Intent(Welcome_Page.this,Sign_In_Page.class);
                startActivity(signin);
                finish();
            }
        });
    }
}