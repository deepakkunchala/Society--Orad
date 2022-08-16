package com.example.societyorad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OTP_Page extends AppCompatActivity {

    ProgressDialog resendmaildialog,resendmobdialog,verifydialog;
    EditText jtext1,jtext2,jtext3,jtext4,jtext5,jtext6,jtext7,jtext8,jtext9,jtext10,jtext11,jtext12;
    Button jVerify;
    TextView jincorrectmobileotp,jincorrectmailotp ,jotpmailtext,jotpmobtext, jotpmailresend , jotpmobileresend, jOTPback;
    String  Userkey , EnteredMobileOTP , EnteredEMailOTP , SentMailOTP , SentMobOTP ;
    String resentMobid = " ";
    String Name,Email,Phone,Password;
    DatabaseReference reference;
    UserHelper userHelper;
    CountDownTimer mobtimer , mailtimer;
    boolean mobiletimerison= false ,mailtimerison = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_page);
        mobtimer = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long l) {
                jotpmobileresend.setText("Didn't Receive OTP? click here in " +l/1000 +" sec");
                mobiletimerison=true;

            }

            @Override
            public void onFinish() {
                jotpmobileresend.setText("Didn't Receive OTP? click here");
               // Toast.makeText(OTP_Page.this, "Done Man", Toast.LENGTH_SHORT).show();
                mobiletimerison = false;

            }
        };
        mobtimer.start();
        mailtimer = new CountDownTimer(120000,1000) {
            @Override
            public void onTick(long l) {
                jotpmailresend.setText("Didn't Receive OTP? click here in " +l/1000 +" sec");
                 mailtimerison=true;

            }

            @Override
            public void onFinish() {
                jotpmailresend.setText("Didn't Receive OTP? click here");
               // Toast.makeText(OTP_Page.this, "Done Man", Toast.LENGTH_SHORT).show();
                mailtimerison = false;

            }
        };
        mailtimer.start();
        jtext1=findViewById(R.id.Text1);
        jtext2=findViewById(R.id.Text2);
        jtext3=findViewById(R.id.Text3);
        jtext4=findViewById(R.id.Text4);
        jtext5=findViewById(R.id.Text5);
        jtext6=findViewById(R.id.Text6);
        jtext7=findViewById(R.id.Text7);
        jtext8=findViewById(R.id.Text8);
        jtext9=findViewById(R.id.Text9);
        jtext10=findViewById(R.id.Text10);
        jtext11=findViewById(R.id.Text11);
        jtext12=findViewById(R.id.Text12);
        jOTPback=findViewById(R.id.OTP_Back);
        jotpmailresend = findViewById(R.id.OTP_mail_resend);
        jotpmobileresend=findViewById(R.id.OTP_mobile_resend);
        jVerify=findViewById(R.id.OTP_Verify);
        jincorrectmailotp=findViewById(R.id.OTP_mail_incorrect);
        jincorrectmobileotp=findViewById(R.id.OTP_mobile_incorrect);
        jotpmobtext=findViewById(R.id.OTP_mobile_otptext);
        jotpmailtext=findViewById(R.id.OTP_mail_otptext);
        jotpmailtext.setText("Enter the OTP sent to the Email\n"+ getIntent().getStringExtra("Email"));
        jotpmobtext.setText("Enter OTP sent to mobile \n +91"+getIntent().getStringExtra("Mobile"));
        userHelper = new UserHelper();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        cursormover();
        jotpmobileresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobiletimerison){
                    Toast.makeText(OTP_Page.this, "Pls wait till the timer is off", Toast.LENGTH_SHORT).show();
                }
                else {
                    resendmobotp();
                    mobtimer.start();

                }



            }
        });
        jOTPback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(OTP_Page.this,Welcome_Page.class);
                startActivity(back);
            }
        });
        jotpmailresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mailtimerison) {
                    Toast.makeText(OTP_Page.this, "Pls wait for the timer to off", Toast.LENGTH_SHORT).show();
                }
                else{

                String resendsenderemail, resendsenderpassword, resendmailemail, resendmailfullname, resendsentmailotp;

                resendsenderemail = "testingorad@gmail.com";
                resendsenderpassword = "Triplektubers@976";
                resendmailemail = getIntent().getStringExtra("Email");
                resendmailfullname = getIntent().getStringExtra("FullName");
                resendsentmailotp = getIntent().getStringExtra("sentmailotp");

                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(resendsenderemail, resendsenderpassword);

                    }
                });


                MimeMessage message = new MimeMessage(session);
                try {
                    message.setFrom(new InternetAddress(resendsenderemail));
                    message.setRecipients(MimeMessage.RecipientType.TO,
                            InternetAddress.parse(resendmailemail));
                    message.setSubject("Verification of E-mail");
                    message.setText("Hello " + resendmailfullname + ". \n" + "The secret 6digit OTP to verify Your Email is " + resendsentmailotp + " .\n" + "Have a Nice Day");
                    new ResendMail().execute(message);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }

             mailtimer.start();
                }
            }
        });

        jVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SentMailOTP = getIntent().getStringExtra("sentmailotp");
                SentMobOTP = getIntent().getStringExtra("sentmobotpid");
                EnteredMobileOTP = jtext7.getText().toString().trim()+jtext8.getText().toString().trim()+jtext9.getText().toString().trim()+jtext10.getText().toString().trim()+jtext11.getText().toString().trim()+jtext12.getText().toString().trim();
                EnteredEMailOTP = jtext1.getText().toString().trim()+jtext2.getText().toString().trim()+jtext3.getText().toString().trim()+jtext4.getText().toString().trim()+jtext5.getText().toString().trim()+jtext6.getText().toString().trim();
                if(jtext1.getText().toString().isEmpty()||jtext2.getText().toString().isEmpty()||jtext3.getText().toString().isEmpty()||jtext4.getText().toString().isEmpty()||jtext12.getText().toString().isEmpty()||jtext5.getText().toString().isEmpty()||jtext6.getText().toString().isEmpty()||jtext7.getText().toString().isEmpty()||jtext8.getText().toString().isEmpty()||jtext9.getText().toString().isEmpty()||jtext10.getText().toString().isEmpty()||jtext11.getText().toString().isEmpty()){
                    Toast.makeText(OTP_Page.this, "Please fill all the Boxes", Toast.LENGTH_SHORT).show();
                }
                else if(!(jtext1.getText().toString().length()==1&&jtext2.getText().toString().length()==1&&jtext3.getText().toString().length()==1&&jtext4.getText().toString().length()==1&&jtext5.getText().toString().length()==1&&jtext6.getText().toString().length()==1&&jtext7.getText().toString().length()==1&&jtext8.getText().toString().length()==1&&jtext9.getText().toString().length()==1&&jtext10.getText().toString().length()==1&&jtext11.getText().toString().length()==1&&jtext12.getText().toString().length()==1)){
                    Toast.makeText(OTP_Page.this, "please fill only one character in eah Box", Toast.LENGTH_SHORT).show();
                }
                else{
                    ValidateOTP();
                }
            }
        });
        SendData();
        Intent intent = new Intent(OTP_Page.this,MainActivity.class);
        startActivity(intent);
        finish();
        jincorrectmobileotp.setVisibility(View.INVISIBLE);
    }

    private void resendmobotp() {



        //Toast.makeText(OTP_Page.this, "ProcessStarted", Toast.LENGTH_SHORT).show();
        resendmobdialog.show(OTP_Page.this,"Please Wait","Resending the OTP to Mobile",true,false);
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + getIntent().getStringExtra("Mobile"), 30, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken ) {
                resentMobid =s;
                resendmobdialog.dismiss();
                Toast.makeText(OTP_Page.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                resendmobdialog.dismiss();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) { Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                resendmobdialog.dismiss();
                Toast.makeText(OTP_Page.this, "Something went Wrong while sending otp", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ValidateOTP() {
        verifydialog = ProgressDialog.show(OTP_Page.this,"Please Wait","Verifying the OTPs",true,false);
        if(SentMailOTP.contentEquals(EnteredEMailOTP)){
            //Toast.makeText(OTP_Page.this, "Email OTP Correct", Toast.LENGTH_SHORT).show();
            Validatemobileotp();
            jincorrectmailotp.setVisibility(View.INVISIBLE);
        }
        else{
            verifydialog.dismiss();
            Toast.makeText(OTP_Page.this, "Email OTP is Incorrect", Toast.LENGTH_SHORT).show();
            jtext1.requestFocus();
            jtext1.setText(null);
            jtext2.setText(null);
            jtext3.setText(null);
            jtext4.setText(null);
            jtext5.setText(null);
            jtext6.setText(null);
            jincorrectmailotp.setVisibility(View.VISIBLE);

        }

    }

    private void Validatemobileotp() {
        String id;
        id=SentMobOTP;
        PhoneAuthCredential phoneAuthCredential =PhoneAuthProvider.getCredential( id,EnteredMobileOTP);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    verifydialog.dismiss();
                    SendData();
                    Intent intent = new Intent(OTP_Page.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    jincorrectmobileotp.setVisibility(View.INVISIBLE);
                    // Toast.makeText(OTPpage.this, "OTPs Correct", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifydialog.dismiss();
                    Toast.makeText(OTP_Page.this, " Mobile OTP incorrect pls enter again", Toast.LENGTH_SHORT).show();
                    jtext7.requestFocus();
                    jtext7.setText(null);
                    jtext8.setText(null);
                    jtext9.setText(null);
                    jtext10.setText(null);
                    jtext11.setText(null);
                    jtext12.setText(null);
                    jincorrectmobileotp.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void SendData() {

        Userkey= UUID.randomUUID().toString();

        Name = getIntent().getStringExtra("FullName");
        Phone = getIntent().getStringExtra("Mobile");
        Password = getIntent().getStringExtra("Password");
        Email = getIntent().getStringExtra("Email");
        userHelper.setEmail(Email);
        userHelper.setName(Name);
        userHelper.setPassword(Password);
        userHelper.setPhone(Phone);
        reference.child(Userkey).setValue(userHelper);
        reference.child(Userkey).child("volunteer").setValue("no");
        RetrieveKey();




    }

    private void RetrieveKey() {
        SharedPreferences preferences = getSharedPreferences("Details",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("dataexists","true");
        editor.putString("UserKey",Userkey);
        editor.putString("email",Email);
        editor.putString("mobile",Phone);
        editor.putString("name",Name);
        editor.putString("Password",Password);
        editor.putString("volunteer","no");
        editor.apply();
    }

    private void cursormover() {
        jtext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext8.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext9.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext10.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext11.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        jtext11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    jtext12.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private class ResendMail extends AsyncTask<MimeMessage,String,String> {


        @Override
        protected void onPreExecute() {
            resendmaildialog.show(OTP_Page.this,"Please Wait","Resending the OTP to Mail",true,false);

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


                resendmaildialog.dismiss();
                Toast.makeText(OTP_Page.this, "Mailsent", Toast.LENGTH_SHORT).show();


            }
            else{
                Toast.makeText(OTP_Page.this, "Mail not Sent", Toast.LENGTH_SHORT).show();
                resendmaildialog.dismiss();
            }
        }


    }
    }
