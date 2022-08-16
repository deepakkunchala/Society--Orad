package com.example.societyorad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Registration_Page extends AppCompatActivity {

    EditText jFullName,jEmail,jMobile ,jSetPassword,jResetPassword;
    TextView jback , jSignin;
    Button jSendOTP;
    Random rnd = new Random();
    int motp = rnd.nextInt(899999)+100000;
    public ProgressDialog progressDialog1 ,progressDialog ;
    String FullName , Email, Mobile , SetPassword , RetypePassword,senderemail,senderpassword, MailOtp
            ,MobileOtpid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        jback=findViewById(R.id.Registration_Back);
        jFullName=findViewById(R.id.Registration_FullName);
        jEmail=findViewById(R.id.Registration_Email);
        jMobile=findViewById(R.id.Registration_Mobile);
        jSetPassword=findViewById(R.id.Registration_Set_Password);
        jResetPassword=findViewById(R.id.Registration_Retype_Password);
        jSendOTP=findViewById(R.id.Registration_SendOTP);
        jSignin=findViewById(R.id.Registration_SignIn);
        senderemail="testingorad@gmail.com";
        senderpassword="Karanam@2010";
        MailOtp= Integer.toString(motp);
        jSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_Page.this,Sign_In_Page.class);
                startActivity(intent);
                finish();
            }
        });
        jback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Registration_Page.this, "Sending Back", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registration_Page.this,Welcome_Page.class);
                startActivity(intent);
                finish();
            }
        });
        jSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                FullName = jFullName.getText().toString().trim();
                Email=jEmail.getText().toString().trim();
                Mobile=jMobile.getText().toString().trim();
                SetPassword=jSetPassword.getText().toString().trim();
                RetypePassword=jResetPassword.getText().toString();

                char[] array = SetPassword.toCharArray();
                int numbers = 0;
                int splchars = 0;
                int space =0;
                for (char i : array) {
                    if (i == '0' || i == '1' || i == '2' || i == '3' || i == '4' || i == '5' || i == '6' || i == '7' || i == '8' || i == '9') {

                        numbers += 1;
                    } else if (i == '!' || i == '@' || i == '#' || i == '$' || i == '%' || i == '&' || i == '*') {
                        splchars += 1;
                    }
                    else if(i == ' '){
                        space +=1;

                    };
                }

                if(FullName.isEmpty()||Email.isEmpty()||Mobile.isEmpty()||SetPassword.isEmpty()||RetypePassword.isEmpty()){
                    Toast.makeText(Registration_Page.this, "Please Fill All The Credentials", Toast.LENGTH_SHORT).show();
                    jFullName.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(Registration_Page.this, "Please enter Valid Email Address.", Toast.LENGTH_SHORT).show();
                    jEmail.requestFocus();
                }
                else if(!(Patterns.PHONE.matcher(Mobile).matches()&&Mobile.length()==10)){
                    Toast.makeText(Registration_Page.this, "Please enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    jMobile.requestFocus();
                }
                else if(array.length < 7 || numbers < 1 || splchars < 1 || array.length > 14 || space>0){
                    Toast.makeText(Registration_Page.this, "Password Doesn't meet the requirements", Toast.LENGTH_SHORT).show();
                    jSetPassword.requestFocus();
                }
                else if(!SetPassword.contentEquals(RetypePassword)){
                    Toast.makeText(Registration_Page.this, "Please Make Sure both the passeords are correct", Toast.LENGTH_SHORT).show();
                    jResetPassword.requestFocus();
                }

                else {
                    existinguser();

                }


            }
        });

    }







    private void existinguser() {
        progressDialog1 = ProgressDialog.show(Registration_Page.this,"Please Wait","Processing request",true,false);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Query checkUsermob = reference.orderByChild("phone").equalTo(Mobile);
        Query checkUsermail = reference.orderByChild("email").equalTo(Email);
        checkUsermail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(Registration_Page.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                    progressDialog1.dismiss();
                    jEmail.requestFocus();
                }
                else{
                    checkUsermob.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(Registration_Page.this, "Entered Mobile Already Exists.", Toast.LENGTH_SHORT).show();
                                progressDialog1.dismiss();
                                jMobile.requestFocus();
                            }
                            else {
                                progressDialog1.dismiss();
                                Intent intent =new Intent(Registration_Page.this,OTP_Page.class);
                                intent.putExtra("sentmailotp",MailOtp);
                                intent.putExtra("sentmobotpid",MobileOtpid);
                                intent.putExtra("FullName",FullName);
                                intent.putExtra("Email",Email);
                                intent.putExtra("Mobile",Mobile);
                                intent.putExtra("Password",SetPassword);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog1.dismiss();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Registration_Page.this, "error", Toast.LENGTH_SHORT).show();
                progressDialog1.dismiss();

            }
        });



    }

    private void SendOTP() {
        //progressDialog.show(Registration_Page.this,"Please Wait","Sending OTP",true,false);
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderemail,senderpassword);
            }
        });





        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(senderemail));
            message.setRecipients(MimeMessage.RecipientType.TO,
                    InternetAddress.parse(Email));
            message.setSubject("Verification of E-mail");
            message.setText("Hello "+ FullName + ". \n"+"The secret 6digit OTP to verify Your Email is " + MailOtp +  " .\n" + "Have a Nice Day");
            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }





    }

    private class SendMail extends AsyncTask<MimeMessage,String,String> {


        @Override
        protected void onPreExecute() {
            progressDialog= ProgressDialog.show(Registration_Page.this,"Please Wait","Sending the OTP ",true,false);
        }

        @Override
        protected String doInBackground(MimeMessage... mimeMessages) {
            try {
                Transport.send(mimeMessages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "failed";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("Success")){

                SendMobileOTP();



            }
            else{
                Toast.makeText(Registration_Page.this, "Something went Wrong ! Please check your Email", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }


    }

    private void SendMobileOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + Mobile, 20, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                MobileOtpid =s;
                progressDialog.dismiss();
                Intent intent =new Intent(Registration_Page.this,OTP_Page.class);
                intent.putExtra("sentmailotp",MailOtp);
                intent.putExtra("sentmobotpid",MobileOtpid);
                intent.putExtra("FullName",FullName);
                intent.putExtra("Email",Email);
                intent.putExtra("Mobile",Mobile);
                intent.putExtra("Password",SetPassword);
                startActivity(intent);

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                progressDialog.dismiss();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) { Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
               progressDialog.dismiss();
            }
        });

    }

}