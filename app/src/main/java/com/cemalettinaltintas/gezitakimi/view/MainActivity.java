package com.cemalettinaltintas.gezitakimi.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cemalettinaltintas.gezitakimi.R;
import com.cemalettinaltintas.gezitakimi.databinding.ActivityMainBinding;
import com.cemalettinaltintas.gezitakimi.model.GeziRehberBilgileri;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    int dilSecim=0;
    Map<String,Object> mapData; //Çekilecek veriler bu Map'te tutulur.
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    ArrayList<GeziRehberBilgileri> yerler;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        yerler = new ArrayList<>();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        dilSecim=intent.getIntExtra("dilSecimi",0);
        verileriAlTr(); //Verilerin veri tabanından getirileceği metottur.

        //arrayAdapter.notifyDataSetChanged();
        binding.listViewYerler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //finish();
                Intent intentToDetail=new Intent(MainActivity.this, GeziRehberiActivity.class);
                intentToDetail.putExtra("geziYerleriDetay",yerler.get(i));
                intentToDetail.putExtra("dilSecimi",dilSecim);
                startActivity(intentToDetail);
            }
        });

    }

    private void verileriAlTr() {
        yerler.clear();
        firebaseFirestore.collection("YerKayit").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if (value!=null){
                    for (DocumentSnapshot snapshot:value.getDocuments()){
                        mapData= snapshot.getData();
                        //Firebase içindeki alan adları ile veriler çekilir.
                        String yerAdi = (String) mapData.get("adi");
                        String ulkeAdi = (String) mapData.get("ulkesi");
                        String sehirAdi = (String) mapData.get("sehir");
                        String tarihce = (String) mapData.get("tarihce");
                        String hakkinda = (String) mapData.get("hakkinda");
                        String placeName = (String) mapData.get("placeName");
                        String countryName = (String) mapData.get("countryName");
                        String cityName = (String) mapData.get("cityName");
                        String history = (String) mapData.get("historyInfo");
                        String about = (String) mapData.get("aboutInfo");
                        String imageURL = (String) mapData.get("gorselURL");
                        GeziRehberBilgileri geziRehberBilgileri = new GeziRehberBilgileri(imageURL,yerAdi,ulkeAdi,sehirAdi,tarihce,hakkinda,placeName,countryName,cityName,history,about);
                        yerler.add(geziRehberBilgileri);

                    }

                    if (dilSecim==0){
                        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, yerler.stream().map(yerlerTr -> yerlerTr.yerAdi).collect(Collectors.toList()));
                        //Burada yerler içindeki verilerden sadece yerAdi bilgisinin ekranda gösterilmesi istenir.
                        binding.listViewYerler.setAdapter(arrayAdapter);
                    }else{
                        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, yerler.stream().map(yerlerTr-> yerlerTr.placeName).collect(Collectors.toList()));
                        //Burada yerler içindeki verilerden sadece placeName bilgisinin ekranda gösterilmesi istenir.
                        binding.listViewYerler.setAdapter(arrayAdapter);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.gezi_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.girisYap){

        } else if (item.getItemId()==R.id.geziEkle) {
            Intent intent=new Intent(MainActivity.this, KayitActivity.class);
            startActivity(intent);
        } else if (item.getItemId()==R.id.dilSecimi) {
            if (dilSecim==0){
                dilSecim=1;
                item.setTitle("Türkçe");
            }else{
                dilSecim=0;
                item.setTitle("English");
            }

        } else if (item.getItemId()==R.id.geziSil) {

        }
        return super.onOptionsItemSelected(item);
    }
}