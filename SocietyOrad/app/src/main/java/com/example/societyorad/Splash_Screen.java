package com.example.societyorad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        SharedPreferences preferences= getSharedPreferences("Details",MODE_PRIVATE);
        String keepme = preferences.getString("dataexists","false");
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(keepme.equals("true")){
                    Intent intent = new Intent(Splash_Screen.this,MainActivity.class);
                    startActivity(intent);
                }
                else if(keepme.equals("false")){
                Intent intent=new Intent(Splash_Screen.this, Welcome_Page.class);
                startActivity(intent);}
                finish();
            }
        } , 300);
    }
}