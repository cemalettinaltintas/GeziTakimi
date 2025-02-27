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

public class MainActivity extends AppCompatActivity {
    int dilSecim=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
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