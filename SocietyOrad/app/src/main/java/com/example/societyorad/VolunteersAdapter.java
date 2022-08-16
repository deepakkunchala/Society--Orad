package com.example.societyorad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class VolunteersAdapter extends FirebaseRecyclerAdapter<volunteersmodel,VolunteersAdapter.viewholder>  {

    boolean card = false;
    public VolunteersAdapter(@NonNull FirebaseRecyclerOptions<volunteersmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull volunteersmodel model) {

        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.phone.setText(model.getPhone());
        holder.pincode.setText("Pincode:"+model.getPincode());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkuserm = reference.orderByChild("email").equalTo(model.getEmail());
        checkuserm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for (DataSnapshot childsnapshot :snapshot.getChildren()){

                        StorageReference  storageReferenceret;
                        storageReferenceret = FirebaseStorage.getInstance().getReference().child("UsersProfilepics/"+childsnapshot.getKey());
                        try {
                            File file = File.createTempFile(childsnapshot.getKey(),"");
                            storageReferenceret.getFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                                            holder.image.setImageBitmap(bitmap);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Uri uri =Uri.parse("android.resource://com.example.societyorad/drawable/ic_baseline_broken_image_24");
                                            holder.image.setImageURI(uri);

                                        }

                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(card){
            holder.relativeLayout.setBackgroundResource(R.drawable.cardviewdesign);
            card=false;
        }
        else {
            holder.relativeLayout.setBackgroundResource(R.drawable.cardviewdesign2);
            card = true;
        }
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteersfragmentcardview,parent,false);
        return new viewholder(view);
    }

    class viewholder extends RecyclerView.ViewHolder{

        TextView name,email,phone,pincode;
        RelativeLayout relativeLayout ;
        ImageView image;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.mail);
            phone = itemView.findViewById(R.id.phone);
            pincode=itemView.findViewById(R.id.pincode);
            image=itemView.findViewById(R.id.image);
        }
    }



}
