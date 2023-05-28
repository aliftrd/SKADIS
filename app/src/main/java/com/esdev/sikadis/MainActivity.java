package com.esdev.sikadis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.esdev.sikadis.fragment.BerandaFragment;
import com.esdev.sikadis.fragment.LayananFragment;
import com.esdev.sikadis.fragment.PendaftaranFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.menu_beranda:
                        selectedFragment = new BerandaFragment();
                        break;
                    case R.id.menu_pendaftaran:
                        selectedFragment = new PendaftaranFragment();
                        break;
                    case R.id.menu_layanan:
                        selectedFragment = new LayananFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, selectedFragment).commit();
                }

                return true;
            }
        });


        // default fragment saat activity pertama kali dibuka
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new BerandaFragment()).commit();
    }
}
