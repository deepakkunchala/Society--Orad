package com.example.societyorad.ui.about_us;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.societyorad.R;
import com.example.societyorad.databinding.FragmentAboutUsBinding;
import com.example.societyorad.databinding.FragmentDonateBinding;
import com.example.societyorad.ui.donate.DonateFragment;

public class About_UsFragment extends Fragment {



    private FragmentAboutUsBinding binding;
    public static DonateFragment newInstance() {
        return new DonateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}