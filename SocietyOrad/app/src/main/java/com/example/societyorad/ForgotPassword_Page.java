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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword_Page extends AppCompatActivity {

    TextView jForgot_Back;
    EditText jForgot_Mail;
    Button jForgot_Submit;
    String jForgot_EMail,RetrivedPassword, UserKey;
    ProgressDialog fetchuserdialog , SendmailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);
        jForgot_Back=findViewById(R.id.Forgot_Back);
        jForgot_Mail=findViewById(R.id.Forgot_mail);
        jForgot_Submit=findViewById(R.id.Forgot_Submit);

        jForgot_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPassword_Page.this, "Taking Back", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ForgotPassword_Page.this,Sign_In_Page.class);
                startActivity(intent);
            }
        });
        jForgot_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jForgot_EMail=jForgot_Mail.getText().toString().trim();
                if(jForgot_EMail.isEmpty()){
                    Toast.makeText(ForgotPassword_Page.this, "Email Field Cannot Be empty", Toast.LENGTH_SHORT).show();
                    jForgot_Mail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(jForgot_EMail).matches()){
                    Toast.makeText(ForgotPassword_Page.this, "Please Enter a Valid Emailid", Toast.LENGTH_SHORT).show();
                }
                else {
                    SearchUser();
                }
            }
        });
    }

    private void SearchUser() {
        fetchuserdialog = ProgressDialog.show(ForgotPassword_Page.this,"Please Wait","Verifying User",true,false);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        Query checkUsermail = reference.orderByChild("email").equalTo(jForgot_EMail);
        checkUsermail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot childsnapshot :snapshot.getChildren()){
                        UserKey = childsnapshot.getKey();
                    }
                    fetchuserdialog.dismiss();
                    RetrivedPassword= snapshot.child(UserKey).child("password").getValue(String.class);
                    SendMail();

                }
                else{
                    Toast.makeText(ForgotPassword_Page.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                    fetchuserdialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                fetchuserdialog.dismiss();
                Toast.makeText(ForgotPassword_Page.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void SendMail() {
        SendmailDialog = ProgressDialog.show(ForgotPassword_Page.this,"Please Wait","Sending Password to Mail",true,false);
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("testingorad@gmail.com","Triplektubers@976");
            }
        });





        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("testingorad@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO,
                    InternetAddress.parse(jForgot_EMail));
            message.setSubject("Recovery Of Password");
            message.setText("the Password Of your Account is " + RetrivedPassword);
           new ForgotPassword_Page.SendpasswordtoMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }



    }


    private class SendpasswordtoMail extends AsyncTask<MimeMessage,String,String> {


        @Override
        protected void onPreExecute() {
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

                Toast.makeText(ForgotPassword_Page.this, "Password Sent Successfully to your Registered Email Id", Toast.LENGTH_SHORT).show();
                SendmailDialog.dismiss();
                Intent intent = new Intent(ForgotPassword_Page.this,Sign_In_Page.class);
                startActivity(intent);
                finish();




            }
            else{
                Toast.makeText(ForgotPassword_Page.this, "Something went Wrong ! Please try Again", Toast.LENGTH_SHORT).show();
                SendmailDialog.dismiss();

            }
        }


    }
}