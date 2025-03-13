package com.cemalettinaltintas.gezitakimi.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class KayitActivity extends AppCompatActivity {
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    private ActivityKayitBinding kayitBinding;
    private int pageNumber;
    Uri fotoBilgi; // Fotoğrafın Uri adresi tutulur.
    public static String yerAdiBilgi,ulkeAdiBilgi,sehirAdiBilgi,tarihceBilgi,hakkindaBilgi; //Tr bilgileri tutulur.
    public static String nameInfo, countryInfo, cityInfo, historyInfo, aboutInfo;

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

        fotoBilgi=Uri.EMPTY;
        yerAdiBilgi="";
        ulkeAdiBilgi="";
        sehirAdiBilgi="";
        tarihceBilgi="";
        hakkindaBilgi="";
        nameInfo="";
        countryInfo="";
        cityInfo="";
        historyInfo="";
        aboutInfo="";

        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference=firebaseStorage.getReference();

        pageNumber=0;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FotoKayitFragment fotoKayitFragment = new FotoKayitFragment();
        fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,fotoKayitFragment).commit();

        kayitBinding.buttonIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageNumber==0){
                    fotoBilgi = FotoKayitFragment.secilenGorsel;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TrBilgiFragment trBilgiFragment = new TrBilgiFragment();
                    fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,trBilgiFragment).commit();
                    kayitBinding.buttonIleri.setText("İleri");
                }else if (pageNumber==1){
                    yerAdiBilgi = TrBilgiFragment.yerAdi.getText().toString();
                    ulkeAdiBilgi = TrBilgiFragment.ulkeAdi.getText().toString();
                    sehirAdiBilgi = TrBilgiFragment.sehirAdi.getText().toString();
                    tarihceBilgi = TrBilgiFragment.tarihce.getText().toString();
                    hakkindaBilgi = TrBilgiFragment.hakkindaGiris.getText().toString();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    EngBilgiFragment engBilgiFragment = new EngBilgiFragment();
                    fragmentTransaction.replace(R.id.constraintLayout_Sayfalar,engBilgiFragment).commit();
                    kayitBinding.buttonIleri.setText("Kaydet");

                } else if (pageNumber==2) {
                    nameInfo = EngBilgiFragment.placeName.getText().toString();
                    countryInfo = EngBilgiFragment.countryName.getText().toString();
                    cityInfo = EngBilgiFragment.cityName.getText().toString();
                    historyInfo = EngBilgiFragment.history.getText().toString();
                    aboutInfo = EngBilgiFragment.aboutInfo.getText().toString();

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
                            if (fotoBilgi!=Uri.EMPTY){
                                UUID uuid = UUID.randomUUID(); //Rastgele bir kimlik numarası oluşturulup fotoğrafa bu isim verilir.
                                String imageName = "images/"+uuid+".jpg";
                                storageReference.child(imageName).putFile(fotoBilgi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //Fotoğraf kaydı başarılı olursa veriler veri tabanına alınır.
                                        System.out.println("Foto Kayit Başarılı");
                                        StorageReference yeniReferans = firebaseStorage.getReference(imageName);
                                        yeniReferans.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String downloadUrl=uri.toString();
                                                HashMap<String,Object> yerData = new HashMap<>();
                                                yerData.put("adi",yerAdiBilgi);
                                                yerData.put("ulkesi",ulkeAdiBilgi);
                                                yerData.put("sehir",sehirAdiBilgi);
                                                yerData.put("tarihce",tarihceBilgi);
                                                yerData.put("hakkinda",hakkindaBilgi);
                                                yerData.put("gorselURL",downloadUrl);
                                                yerData.put("placeName",nameInfo);
                                                yerData.put("countryName",countryInfo);
                                                yerData.put("cityName",cityInfo);
                                                yerData.put("historyInfo",historyInfo);
                                                yerData.put("aboutInfo",aboutInfo);
                                                yerData.put("kayitTarihi", FieldValue.serverTimestamp());
                                                firebaseFirestore.collection("YerKayit").add(yerData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Veri kaydı başarılı olursa bildirim gönderilir.
                                                        System.out.println("Veri kaydı başarılı");
                                                        Toast.makeText(getApplicationContext(),"Kayıt Başarılı",Toast.LENGTH_LONG).show();
                                                        finish();
                                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println(e.getLocalizedMessage());
                                                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        });

                                    }
                                });

                            }
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