package com.example.societyorad.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.societyorad.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    Uri imageuri , abc;
    static final int RESULT_OK = -1;
    boolean picchanged=false;
    ScrollView scrollView;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference , storageReferenceret;
    String   UserKey, Retrived_Email, Retrived_Mobile, Retrived_FullName;
    ImageView displiayimage,closeimage;
    EditText  jname, jmobile, jemail;
    TextView jusername;
    Button edit;
    Bitmap bitmap;
    ShapeableImageView profilepic;
    boolean editing =false;
    boolean isimage = false;
    DatabaseReference reference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        jemail = binding.ProfileEmail;
        profilepic = binding.profileimage;
        jmobile = binding.ProfileMobile;
        jusername = binding.UserName;
        jname = binding.ProfileFullName;
        edit = binding.ProfileEdit;
        displiayimage=binding.profiedisplayimage;
        closeimage=binding.closeImage;
        scrollView = binding.scrollviewf;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        SharedPreferences preferences = getActivity().getSharedPreferences("Details", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        UserKey = preferences.getString("UserKey", "......");
        jusername.setText(preferences.getString("name","....."));
        jname.setText(preferences.getString("name","....."));
        jemail.setText(preferences.getString("email","....."));
        jmobile.setText(preferences.getString("mobile","....."));
       /* Query checkUsermobile = reference.orderByChild("phone").equalTo(Mobile);
        checkUsermobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Retrived_Password = snapshot.child(Mobile).child("password").getValue(String.class);
                    Retrived_Email = snapshot.child(Mobile).child("email").getValue(String.class);
                    Retrived_Mobile = snapshot.child(Mobile).child("phone").getValue(String.class);
                    Retrived_FullName = snapshot.child(Mobile).child("name").getValue(String.class);
                    jemail.setText(Retrived_Email);
                    jmobile.setText(Retrived_Mobile);
                    jpassword.setText(Retrived_Password);
                    jname.setText(Retrived_FullName);
                    jusername.setText(Retrived_FullName);
                    progressDialog.dismiss();

                }
                else {
                    Toast.makeText(getActivity(), "something went wrong!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override

            public void onCancelled (@NonNull DatabaseError error){
            }
        });*/
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editing==false){
                    AlertDialog.Builder passwordcheck = new AlertDialog.Builder(getActivity());
                    passwordcheck.setTitle("Please enter your password to edit details");
                    EditText pw = new EditText(getActivity());
                    pw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordcheck.setView(pw);
                    passwordcheck.setPositiveButton("check", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(pw.getText().toString().equals(preferences.getString("Password","....."))){
                                jname.setFocusable(true);
                                jmobile.setFocusable(true);
                                jemail.setFocusable(true);
                                jname.setFocusableInTouchMode(true);
                                jmobile.setFocusableInTouchMode(true);
                                jemail.setFocusableInTouchMode(true);
                                edit.setText("SAVE");
                                jname.requestFocus();
                                editing=true;
                            }

                            else{
                                Toast.makeText(getContext(), "Password Incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    passwordcheck.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Nothing happened", Toast.LENGTH_SHORT).show();
                        }
                    });

                passwordcheck.show();

                }
                else{
                    if(picchanged){
                        uploadpic();
                    }


                    checkchanges();



                }
            }
        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editing ){

                    if(!isimage){
                        Toast.makeText(getActivity(), "No image to be displayed!!!" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        displayimage();
                        edit.setClickable(false);
                        Toast.makeText(getActivity(), "Displaying image", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    CropImage.startPickImageActivity(getContext(),ProfileFragment.this);
                }
            }
        });
        closeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displiayimage.setVisibility(View.GONE);
                closeimage.setVisibility(View.GONE);
                edit.setClickable(true);
            }
        });

        storageReferenceret = FirebaseStorage.getInstance().getReference().child("UsersProfilepics/"+UserKey);
        try {
            File file = File.createTempFile(UserKey,"");
            storageReferenceret.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "retreived", Toast.LENGTH_SHORT).show();
                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            isimage=true;
                            profilepic.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void displayimage() {
        displiayimage.setImageBitmap(bitmap);
        displiayimage.setVisibility(View.VISIBLE);
        closeimage.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE&& resultCode== Activity.RESULT_OK){
            Uri crpimageuri = CropImage.getPickImageResultUri(getActivity(),data);
            if(CropImage.isReadExternalStoragePermissionsRequired(getActivity(),crpimageuri)){
                imageuri = crpimageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

            }
            else
            {
                startCrop(crpimageuri);
            }
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode== RESULT_OK){
                abc= result.getUri();
                profilepic.setImageURI(abc);
                picchanged=true;
                Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCrop(Uri crpimageuri) {

        CropImage.activity(crpimageuri)

                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(1,1)
                .start(getContext(),this);
    }

    private void uploadpic() {
        ProgressDialog pd =new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image");
        pd.show();


        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageReference.child("UsersProfilepics/"+UserKey.trim());

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageReference.child("images/UsersProfilepics/"+UserKey.trim());

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        mountainsRef.putFile(abc).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "not Uploaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress =(snapshot.getBytesTransferred()*100.00/snapshot.getTotalByteCount());
                        pd.setMessage("percentage"+(int)progress+"%");
                    }
                });

    }

    private void checkchanges() {
        if(jname.getText().toString().trim().equals(Retrived_FullName)&&jmobile.getText().toString().trim().equals(Retrived_Mobile)&&jemail.getText().toString().trim().equals(Retrived_Email)){
            Toast.makeText(getActivity(), "No Data Changed", Toast.LENGTH_SHORT).show();
            edit.setText("EDIT");
            editing=false;
            jname.setFocusable(false);
            jmobile.setFocusable(false);
            jemail.setFocusable(false);
        }
        else
        {
            checkformat();
        }
    }

    private void checkformat() {
        String FullName = jname.getText().toString().trim();
        String Email=jemail.getText().toString().trim();
        String Mobile=jmobile.getText().toString().trim();



        if(FullName.isEmpty()||Email.isEmpty()||Mobile.isEmpty()){
            Toast.makeText(getActivity(), "Please Fill All The Credentials. \n No changes Made", Toast.LENGTH_SHORT).show();
            jname.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Toast.makeText(getActivity(), "Please enter Valid Email Address.\n no changes Made", Toast.LENGTH_SHORT).show();
            jemail.requestFocus();
        }
        else if(!(Patterns.PHONE.matcher(Mobile).matches()&&Mobile.length()==10)){
            Toast.makeText(getActivity(), "Please enter Valid Mobile Number. \n No changes made", Toast.LENGTH_SHORT).show();
            jmobile.requestFocus();
        }

        else {
            updatedata();

        }

        /*----------------------------------*/
    }

    private void updatedata() {
        reference.child(UserKey).child("name").setValue(jname.getText().toString().trim());
        reference.child(UserKey).child("phone").setValue(jmobile.getText().toString().trim());
        reference.child(UserKey).child("email").setValue(jemail.getText().toString().trim());
        Toast.makeText(getActivity(), "Data Updated Successfully ", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getActivity().getSharedPreferences("Details",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",jemail.getText().toString().trim());
        editor.putString("mobile",jmobile.getText().toString().trim());
        editor.putString("name",jname.getText().toString().trim());
        editor.apply();
        edit.setText("EDIT");
        editing=false;
        jname.setFocusable(false);
        jmobile.setFocusable(false);
        jemail.setFocusable(false);


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}