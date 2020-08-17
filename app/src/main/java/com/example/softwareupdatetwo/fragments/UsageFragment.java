package com.example.softwareupdatetwo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.softwareupdatetwo.R;


public class UsageFragment extends Fragment {

    public UsageFragment() {
        // Required empty public constructor
    }

    public static UsageFragment newInstance( ) {
        return new UsageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_usage, container, false);
    }
}