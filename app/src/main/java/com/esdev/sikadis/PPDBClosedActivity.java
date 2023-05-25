package com.esdev.sikadis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class PPDBClosedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppdb_closed);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(view -> onBackPressed());
    }
}
