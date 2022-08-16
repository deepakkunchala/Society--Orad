package com.example.societyorad.ui.signout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.societyorad.MainActivity;
import com.example.societyorad.R;
import com.example.societyorad.Welcome_Page;
import com.example.societyorad.databinding.FragmentSignoutBinding;
import com.example.societyorad.ui.home.HomeFragment;
import com.example.societyorad.ui.profile.ProfileFragment;

public class SignoutFragment extends   Fragment   {


    private FragmentSignoutBinding binding;
    

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignoutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to SignOut?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signoutuser();
            }
        })
        .setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);


            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void signoutuser() {

        SharedPreferences preferences =getActivity().getSharedPreferences("Details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("dataexists", "false");
        editor.putString("UserKey", "");
        editor.apply();
        Intent intent = new Intent(getActivity(), Welcome_Page.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}







