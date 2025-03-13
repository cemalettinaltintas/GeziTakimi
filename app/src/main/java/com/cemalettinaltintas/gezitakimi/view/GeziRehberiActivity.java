package com.cemalettinaltintas.gezitakimi.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cemalettinaltintas.gezitakimi.R;
import com.cemalettinaltintas.gezitakimi.databinding.ActivityGeziRehberiBinding;
import com.cemalettinaltintas.gezitakimi.model.GeziRehberBilgileri;
import com.squareup.picasso.Picasso;

public class GeziRehberiActivity extends AppCompatActivity {
    private ActivityGeziRehberiBinding activityGeziRehberiBinding;
    int dilSecimi;
    GeziRehberBilgileri geziRehberBilgileri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gezi_rehberi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        geziRehberBilgileri = (GeziRehberBilgileri) intent.getSerializableExtra("geziYerleriDetay");
        dilSecimi = intent.getIntExtra("dilSecimi",0);
        verileriAl();
    }

    private void verileriAl() {
        Picasso.get().load(geziRehberBilgileri.imageID).into(activityGeziRehberiBinding.imageViewGorsel);
        if(dilSecimi==0){
            activityGeziRehberiBinding.textViewYerBigi.setText(geziRehberBilgileri.yerAdi);
            activityGeziRehberiBinding.textViewUlkeBilgi.setText(geziRehberBilgileri.ulkeAdi);
            activityGeziRehberiBinding.textViewSehirBigi.setText(geziRehberBilgileri.sehirAdi);
            activityGeziRehberiBinding.textViewTarihce.setText(geziRehberBilgileri.tarihce);
            activityGeziRehberiBinding.textViewHakkinda.setText(geziRehberBilgileri.hakkinda);
        }else{
            activityGeziRehberiBinding.textViewYerBigi.setText(geziRehberBilgileri.placeName);
            activityGeziRehberiBinding.textViewUlkeBilgi.setText(geziRehberBilgileri.countryName);
            activityGeziRehberiBinding.textViewSehirBigi.setText(geziRehberBilgileri.cityName);
            activityGeziRehberiBinding.textViewTarihce.setText(geziRehberBilgileri.history);
            activityGeziRehberiBinding.textViewHakkinda.setText(geziRehberBilgileri.about);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.dilsecimi_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.geri){

        }
        return super.onOptionsItemSelected(item);
    }
}