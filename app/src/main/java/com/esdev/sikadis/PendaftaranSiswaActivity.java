package com.esdev.sikadis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.esdev.sikadis.fragment.PendaftaranFragment;
import java.io.IOException;
import java.util.Calendar;

public class PendaftaranSiswaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView uploadImage;
    private EditText tanggalLahireditText;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private EditText mNamaField;
    private EditText nik_field;
    private EditText tempat_lahir_field;
    private EditText namaAyahField;
    private EditText pekerjaanAyahField;
    private EditText namaIbuField;
    private EditText alamatField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_siswa);

        // Inisialisasi EditText
        mNamaField = findViewById(R.id.nama_field);
        tempat_lahir_field = findViewById(R.id.tempat_lahir_field);
        namaAyahField = findViewById(R.id.namaAyahField);
        namaIbuField = findViewById(R.id.namaIbuField);
        pekerjaanAyahField = findViewById(R.id.pekerjaanAyahField);
        alamatField = findViewById(R.id.alamatField);

        // Set listener untuk memeriksa apakah field kosong saat kehilangan fokus
        mNamaField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mNamaField.getText().toString().isEmpty()) {
                        mNamaField.setError("Nama harus diisi");
                    }
                }
            }
        });

        namaIbuField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (namaIbuField.getText().toString().isEmpty()) {
                        namaIbuField.setError("Nama Ibu harus diisi");
                    }
                }
            }
        });

        namaAyahField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (namaAyahField.getText().toString().isEmpty()) {
                        namaAyahField.setError("Nama Ayah harus diisi");
                    }
                }
            }
        });

        pekerjaanAyahField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pekerjaanAyahField.getText().toString().isEmpty()) {
                        pekerjaanAyahField.setError("Pekerjaan Ayah harus diisi");
                    }
                }
            }
        });

        alamatField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (alamatField.getText().toString().isEmpty()) {
                        alamatField.setError("Alamat harus diisi");
                    }
                }
            }
        });

        tempat_lahir_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (tempat_lahir_field.getText().toString().isEmpty()) {
                        tempat_lahir_field.setError("Tempat lahir harus diisi");
                    }
                }
            }
        });

        // Button arrow kembali
        ImageButton backButton = findViewById(R.id.imageView3);
        backButton.setOnClickListener(view -> onBackPressed());

        uploadImage = findViewById(R.id.upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                uploadImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Button buttonLanjutkan = findViewById(R.id.button_lanjutkan);
        buttonLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNamaField.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (namaIbuField.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Nama Ibu tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (namaAyahField.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Nama Ayah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pekerjaanAyahField.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Pekerjaan Ayah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (alamatField.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tempat_lahir_field.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Tempat lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tanggalLahireditText.getText().toString().isEmpty()) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Tanggal lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioGroup jenisKelaminRadioGroup = findViewById(R.id.gender_radio_group);
                int selectedRadioButtonId = jenisKelaminRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId == -1) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Jenis kelamin tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uploadImage.getDrawable() == null) {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Foto harus diunggah", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(PendaftaranSiswaActivity.this);
                builder.setTitle("Pendaftaran Sukses");
                builder.setMessage("Pendaftaran Anda sudah berhasil.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tindakan yang diambil saat tombol OK pada pop-up di klik
                        dialog.dismiss(); // Menutup pop-up
                        // Melakukan tindakan berikutnya
                        Intent intent = new Intent(PendaftaranSiswaActivity.this, PPDBClosedActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });
    }
}
