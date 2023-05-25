package com.esdev.sikadis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import java.io.IOException;
import java.util.Calendar;

public class PendaftaranSiswaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private ImageButton uploadImage;
    private boolean isImageUploaded = false;
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
        nik_field = findViewById(R.id.nikField);

        // Set listener untuk memeriksa apakah field kosong saat kehilangan fokus

        nik_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (nik_field.getText().toString().isEmpty()) {
                        nik_field.setError("NIK harus diisi");
                    }
                }
            }
        });
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

        // Inisialisasi ImageButton
        uploadImage = findViewById(R.id.upload_image);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFilePermission();
            }
        });

        // Inisialisasi EditText untuk tanggal lahir
        tanggalLahireditText = findViewById(R.id.tanggal_lahir_edittext);
        tanggalLahireditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Inisialisasi Calendar untuk tanggal lahir
        calendar = Calendar.getInstance();

        // Inisialisasi Spinner
        Spinner spinner = findViewById(R.id.spinner_agama);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.religion_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Inisialisasi RadioGroup
        RadioGroup radioGroup = findViewById(R.id.gender_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Menangani perubahan pilihan jenis kelamin
                switch (checkedId) {
                    case R.id.radio_button_laki_laki:
                        // Dilakukan ketika memilih jenis kelamin "Laki-laki"
                        break;
                    case R.id.radio_button_perempuan:
                        // Dilakukan ketika memilih jenis kelamin "Perempuan"
                        break;
                }
            }
        });

        // Inisialisasi tombol Simpan
        Button simpanButton = findViewById(R.id.button_lanjutkan);
        simpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Memeriksa apakah semua field telah diisi
                if (isFormValid()) {
                    // Memeriksa apakah jenis kelamin telah dipilih
                    RadioGroup radioGroup = findViewById(R.id.gender_radio_group);
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(PendaftaranSiswaActivity.this, "Harap pilih jenis kelamin", Toast.LENGTH_SHORT).show();
                    } else {
                        // Menampilkan pop up
                        AlertDialog.Builder builder = new AlertDialog.Builder(PendaftaranSiswaActivity.this);
                        builder.setMessage("Apakah data yang anda isi sudah benar?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Pindah ke halaman lain
                                        Intent intent = new Intent(PendaftaranSiswaActivity.this, PPDBClosedActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    Toast.makeText(PendaftaranSiswaActivity.this, "Harap lengkapi semua field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Mengatur tanggal yang dipilih pada EditText
                tanggalLahireditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private boolean isFormValid() {
        // Memeriksa apakah semua field telah diisi
        Boolean isNikValid = !nik_field.getText().toString().isEmpty();
        boolean isNamaValid = !mNamaField.getText().toString().isEmpty();
        boolean isTempatLahirValid = !tempat_lahir_field.getText().toString().isEmpty();
        boolean isTanggalLahirValid = !tanggalLahireditText.getText().toString().isEmpty();
        boolean isNamaAyahValid = !namaAyahField.getText().toString().isEmpty();
        boolean isNamaIbuValid = !namaIbuField.getText().toString().isEmpty();
        boolean isPekerjaanAyahValid = !pekerjaanAyahField.getText().toString().isEmpty();
        boolean isAlamatValid = !alamatField.getText().toString().isEmpty();

        return isNikValid && isNamaValid && isTempatLahirValid && isTanggalLahirValid && isNamaAyahValid &&
                isNamaIbuValid && isPekerjaanAyahValid && isAlamatValid && isImageUploaded;
    }

    private void checkFilePermission() {
        // Memeriksa izin akses file
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permintaan izin akses file
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            // Izin akses file sudah diberikan
            openGallery();
        }
    }

    private void openGallery() {
        // Membuka galeri untuk memilih gambar
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin akses file diberikan
                openGallery();
            } else {
                // Izin akses file ditolak
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Izin akses file ditolak. Anda tidak dapat memilih gambar.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Mengambil URI gambar yang dipilih
            Uri selectedImage = data.getData();
            try {
                // Memeriksa ukuran file
                long fileSizeInBytes = getFileSize(selectedImage);
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;
                if (fileSizeInMB <= 2) {
                    // Memeriksa tipe file
                    String mimeType = getContentResolver().getType(selectedImage);
                    if (mimeType != null && mimeType.startsWith("image/")) {
                        // Mengonversi URI gambar menjadi Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        // Menampilkan gambar yang dipilih pada ImageView
                        ImageView imageView = findViewById(R.id.upload_image);
                        imageView.setImageBitmap(bitmap);
                        isImageUploaded = true;
                    } else {
                        // Tipe file bukan gambar, tampilkan pesan kesalahan
                        Toast.makeText(PendaftaranSiswaActivity.this, "Hanya gambar yang diizinkan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Ukuran file melebihi batas, tampilkan pesan kesalahan
                    Toast.makeText(PendaftaranSiswaActivity.this, "Ukuran file maksimal 2MB", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long getFileSize(Uri uri) {
        String[] fileSizeColumn = {MediaStore.Images.Media.SIZE};
        Cursor cursor = getContentResolver().query(uri, fileSizeColumn, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(fileSizeColumn[0]);
            long fileSize = cursor.getLong(columnIndex);
            cursor.close();
            return fileSize;
        }
        return 0;
    }
}

