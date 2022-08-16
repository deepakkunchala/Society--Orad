package com.example.societyorad.ui.volunteers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.societyorad.R;
import com.example.societyorad.VolunteersAdapter;
import com.example.societyorad.databinding.FragmentDonateBinding;
import com.example.societyorad.databinding.FragmentVolunteersBinding;
import com.example.societyorad.ui.donate.DonateFragment;
import com.example.societyorad.volunteersmodel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class VolunteersFragment extends Fragment {


    private FragmentVolunteersBinding binding;
    public static VolunteersFragment newInstance() {
        return new VolunteersFragment();
    }
    RecyclerView recyclerView;
    VolunteersAdapter adapter ;
    SearchView searchView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVolunteersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        searchView= binding.volunteersSearch;
        recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        FirebaseRecyclerOptions<volunteersmodel> options = new FirebaseRecyclerOptions.Builder<volunteersmodel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("volunteer").startAt("yes").endAt("pending\uf8ff"), volunteersmodel.class)

                .build();
        adapter = new VolunteersAdapter(options);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processsearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processsearch(newText);
                return false;
            }
        });








        return root;
    }

    private void processsearch(String s){

        FirebaseRecyclerOptions<volunteersmodel> options = new FirebaseRecyclerOptions.Builder<volunteersmodel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("pincode").startAt(s).endAt(s+"\uf8ff"), volunteersmodel.class)
                .build();
        adapter = new VolunteersAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}