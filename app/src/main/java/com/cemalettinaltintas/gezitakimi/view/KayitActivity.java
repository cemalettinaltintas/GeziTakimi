package com.cemalettinaltintas.gezitakimi.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cemalettinaltintas.gezitakimi.R;
import com.cemalettinaltintas.gezitakimi.databinding.ActivityKayitBinding;
import com.cemalettinaltintas.gezitakimi.fragments.EngBilgiFragment;
import com.cemalettinaltintas.gezitakimi.fragments.FotoKayitFragment;
import com.cemalettinaltintas.gezitakimi.fragments.TrBilgiFragment;

public class KayitActivity extends AppCompatActivity {
    private ActivityKayitBinding kayitBinding;
    private int pageNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        kayitBinding=ActivityKayitBinding.inflate(getLayoutInflater());
        View view=kayitBinding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pageNumber=0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FotoKayitFragment fotoKayitFragment = new FotoKayitFragment();
        fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,fotoKayitFragment).commit();

        kayitBinding.buttonIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(pageNumber);
                if (pageNumber==0){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TrBilgiFragment trBilgiFragment = new TrBilgiFragment();
                    fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,trBilgiFragment).commit();
                    kayitBinding.buttonIleri.setText("İleri");
                }else if (pageNumber==1){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    EngBilgiFragment engBilgiFragment = new EngBilgiFragment();
                    fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,engBilgiFragment).commit();
                    kayitBinding.buttonIleri.setText("Kaydet");
                } else if (pageNumber==2) {
                    //Alert dialog çıkacak
                    AlertDialog.Builder aleDialog=new AlertDialog.Builder(KayitActivity.this);
                    aleDialog.setTitle("Kayıt");
                    aleDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        //Hiç bir şey yapmayacak
                        }
                    });
                    aleDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Veriler veritabanına kaydedilecek.
                        }
                    });
                    aleDialog.show();
                }

                if(pageNumber<2){
                    pageNumber++;
                }
            }
        });
        kayitBinding.buttonGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(pageNumber);
                if (pageNumber>0){
                    pageNumber--;
                }
                else
                {
                    //Çıkış gerektiği zamanlarda AlertDiyalog ile kullanıcının çıkışı onaylaması teyit edilir.
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(KayitActivity.this);
                    alertDialog.setTitle("ÇIKIŞ");
                    alertDialog.setMessage("Çıkmak İstiyor Musunuz? ");
                    alertDialog.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            Intent intent = new Intent(KayitActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
                if(pageNumber==0){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FotoKayitFragment fotoKayitFragment = new FotoKayitFragment();
                    fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,fotoKayitFragment).commit();
                    kayitBinding.buttonIleri.setText("İleri");
                }
                else if(pageNumber==1){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TrBilgiFragment trBilgiFragment = new TrBilgiFragment();
                    fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,trBilgiFragment).commit();
                    kayitBinding.buttonIleri.setText("İleri");
                }
            }
        });
    }
}