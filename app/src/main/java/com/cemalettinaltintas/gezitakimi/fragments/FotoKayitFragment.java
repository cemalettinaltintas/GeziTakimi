package com.cemalettinaltintas.gezitakimi.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cemalettinaltintas.gezitakimi.R;
import com.cemalettinaltintas.gezitakimi.databinding.FragmentFotoKayitBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;


public class FotoKayitFragment extends Fragment {
    ActivityResultLauncher<String> permissionResultLauncher;//izin için
    ActivityResultLauncher<Intent> activityResultLauncher;//galeriye gitmek için
    public static Uri secilenGorsel;
    Bitmap secilenBitmap;
    private FragmentFotoKayitBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLaunher();
    }

    private void registerLaunher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if (o.getResultCode() == AppCompatActivity.RESULT_OK) {
                    Intent intentFromResult = o.getData();
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.getData();
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                //Yeni yöntem
                                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), secilenGorsel);
                                secilenBitmap = ImageDecoder.decodeBitmap(source);
                                binding.imageviewFotoKayit.setImageBitmap(secilenBitmap);
                            } else {
                                //Eski yöntem
                                secilenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), secilenGorsel);
                                binding.imageviewFotoKayit.setImageBitmap(secilenBitmap);
                            }
                        } catch (IOException e) {
                            e.getLocalizedMessage();
                        }
                    }
                }
            }
        });
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {
                if (o) {
                    //izin verildi
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                } else {
                    //izin verilmedi
                    Toast.makeText(requireActivity(), "İzin verilmedi!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFotoKayitBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.imageviewFotoKayit.setOnClickListener(this::gorselSec);
        return view;
    }

    public void gorselSec(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                //izin istenecek
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view, "Galeriye ulaşıp görsel seçmemiz lazım!", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //izin istenecek
                            permissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                } else {
                    //izin istenecek
                    permissionResultLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            } else {
                //izin verilmiş, galeriye gidilecek
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //İzin verilmemiş, izin istememiz gerekiyor.
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Snacbar gösterebiliriz. Kullanıcıdan neden izin istediğimizi bir kez daha söyleyerek izin istememiz lazım.
                    Snackbar.make(view, "Galeriye ulaşıp görsel seçmemiz lazım!", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //İzin isteyeceğiz.
                            permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                } else {
                    //İzin isteyeceğiz
                    permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                //İzin verilmiş, galeriye gidebilirim.
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }

    }
}