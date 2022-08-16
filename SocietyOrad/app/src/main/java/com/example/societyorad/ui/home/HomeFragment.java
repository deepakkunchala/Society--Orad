package com.example.societyorad.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.societyorad.MainActivity;
import com.example.societyorad.R;
import com.example.societyorad.Sign_In_Page;
import com.example.societyorad.Volunteer_Registration_Page;
import com.example.societyorad.databinding.FragmentHomeBinding;
import com.example.societyorad.ui.about_us.About_UsFragment;
import com.example.societyorad.ui.profile.ProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    TextView monthdonationtext, yeardonationtext;
    Button button;



    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        swipeRefreshLayout = binding.homes;
        monthdonationtext = binding.homeMonthdonationText;
        button = binding.homeButton;
        monthdonationtext.setSelected(true);
        yeardonationtext = binding.homeYeardonationText;
        yeardonationtext.setSelected(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                SharedPreferences preferences = getActivity().getSharedPreferences("Details",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                final String[] vstatus= {""};

                final String[] a = new String[1];
                String mail = preferences.getString("email","");
                Query checkuserm = reference.orderByChild("email").equalTo(mail);
                checkuserm.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key=" " ;
                       if(snapshot.exists()){
                           for (DataSnapshot childsnapshot :snapshot.getChildren()){
                               key = childsnapshot.getKey();
                           }
                          editor.putString("volunteer", snapshot.child(key).child("volunteer").getValue(String.class));
                           editor.apply();
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getContext(), preferences.getString("volunteer",""), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkvolunteer();
            }
        });


        return root;
    }

    private void checkvolunteer() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String volunteerstatus = preferences.getString("volunteer", ".....");
        if(volunteerstatus.equals("yes")){
            Toast.makeText(getActivity(), "You are already registered as volunteer! if not, please contact us .", Toast.LENGTH_SHORT).show();
        }
        else if(volunteerstatus.equals("pending")){

            Toast.makeText(getActivity(), "your status is in Pending.please vivit again in couple of days :)", Toast.LENGTH_SHORT).show();
        }
        else if(volunteerstatus.equals("no")){
        Toast.makeText(getActivity(), "sending you", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getContext(), Volunteer_Registration_Page.class);
        startActivity(intent);
    }

}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}