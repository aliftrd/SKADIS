package com.esdev.sikadis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageButton;

import java.util.Calendar;

public class PendaftaranSiswaActivity extends AppCompatActivity {

    private EditText tanggalLahireditText;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_siswa);

        ImageButton backButton = findViewById(R.id.imageView3);
        backButton.setOnClickListener(view -> onBackPressed());

        tanggalLahireditText = findViewById(R.id.tanggal_lahir_edittext);

        calendar = Calendar.getInstance();

        tanggalLahireditText.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(PendaftaranSiswaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tanggalLahireditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        }));
        Spinner spinnerAgama = findViewById(R.id.spinner_agama);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.religion_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgama.setAdapter(adapter);
    }
}