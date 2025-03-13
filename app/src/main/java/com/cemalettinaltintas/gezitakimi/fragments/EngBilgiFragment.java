package com.cemalettinaltintas.gezitakimi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cemalettinaltintas.gezitakimi.R;

public class EngBilgiFragment extends Fragment {
    public static EditText placeName,countryName,cityName,history,aboutInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eng_bilgi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placeName = view.findViewById(R.id.editText_NameInput);
        countryName = view.findViewById(R.id.editText_CountryInput);
        cityName = view.findViewById(R.id.editText_CityInput);
        history = view.findViewById(R.id.editText_HistoryInput);
        aboutInfo = view.findViewById(R.id.editText_AboutInput);
    }
}