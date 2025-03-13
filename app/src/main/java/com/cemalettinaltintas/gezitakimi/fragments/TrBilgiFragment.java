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
import com.cemalettinaltintas.gezitakimi.view.KayitActivity;

public class TrBilgiFragment extends Fragment {
    public static EditText yerAdi,ulkeAdi,sehirAdi,tarihce,hakkindaGiris;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tr_bilgi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yerAdi = view.findViewById(R.id.editText_NameInput);
        ulkeAdi = view.findViewById(R.id.editText_CountryInput);
        sehirAdi = view.findViewById(R.id.editText_CityInput);
        tarihce = view.findViewById(R.id.editText_HistoryInput);
        hakkindaGiris = view.findViewById(R.id.editText_AboutInput);
        veriEkranaGetir();
    }
    public void veriEkranaGetir(){
        if(!KayitActivity.yerAdiBilgi.equals("")){
            yerAdi.setText(KayitActivity.yerAdiBilgi);
        }
        if(!KayitActivity.ulkeAdiBilgi.equals("")){
            ulkeAdi.setText(KayitActivity.ulkeAdiBilgi);
        }
        if(!KayitActivity.sehirAdiBilgi.equals("")){
            sehirAdi.setText(KayitActivity.sehirAdiBilgi);
        }
        if(!KayitActivity.tarihceBilgi.equals("")){
            tarihce.setText(KayitActivity.tarihceBilgi);
        }
        if(!KayitActivity.hakkindaBilgi.equals("")){
            hakkindaGiris.setText(KayitActivity.hakkindaBilgi);
        }
    }
}