package com.esdev.sikadis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.esdev.sikadis.adapter.AgamaSpinnerAdapter;
import com.esdev.sikadis.fragment.PendaftaranFragment;
import com.esdev.sikadis.models.Siswa;
import com.esdev.sikadis.responses.AgamaResponse;
import com.esdev.sikadis.retrofit.ApiClient;
import com.esdev.sikadis.retrofit.Api;
import com.esdev.sikadis.responses.RegisterResponse;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.ByteArrayOutputStream;



public class PendaftaranSiswaActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;

    private Api apiService;

    private boolean isImageUploaded = false;

    private DatePickerDialog datePickerDialog;

    private ImageButton upload_image;

    private Uri imageUri;
    private EditText tanggalLahireditText;
    private Calendar calendar;
    private EditText nik_field;
    private EditText mNamaField;
    private EditText tempat_lahir_field;
    private RadioGroup radioGroup;
    private  Spinner spinnerAgama;
    private EditText namaAyahField;
    private EditText pekerjaanAyahField;
    private EditText namaIbuField;
    private EditText alamatField;
    private EditText pekerjaanIbu;
    private EditText namaWali;
    private EditText alamatWali;
    private EditText pekerjaanWali;
    private String gender;

    private String selectedAgama;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_siswa);

        // Inisialisasi EditText
        nik_field = findViewById(R.id.nikField);
        mNamaField = findViewById(R.id.nama_field);
        tempat_lahir_field = findViewById(R.id.tempat_lahir_field);
        radioGroup = findViewById(R.id.gender_radio_group);
        namaAyahField = findViewById(R.id.namaAyahField);
        pekerjaanAyahField = findViewById(R.id.pekerjaanAyahField);
        namaIbuField = findViewById(R.id.namaIbuField);
        pekerjaanIbu = findViewById(R.id.pekerjaanIbuField);
        alamatField = findViewById(R.id.alamatField);
        namaWali = findViewById(R.id.namaWaliField);
        alamatWali = findViewById(R.id.alamatWaliField);
        pekerjaanWali = findViewById(R.id.pekerjaanWaliField);

        Spinner spinnerAgama = findViewById(R.id.spinner_agama);




        // Set listener untuk memeriksa apakah field kosong saat kehilangan fokus
        nik_field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String nik = nik_field.getText().toString().trim();
                    if (nik.isEmpty()) {
                        nik_field.setError("NIK harus diisi");
                    } else if (nik.length() != 16) {
                        nik_field.setError("NIK harus terdiri dari 16 karakter");
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

        // Set listener untuk tombol upload gambar
        upload_image = findViewById(R.id.upload_image);
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndPickImage();
            }
        });

        // Set listener untuk tanggal lahir
        tanggalLahireditText = findViewById(R.id.tanggal_lahir_edittext);
        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        tanggalLahireditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(PendaftaranSiswaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format tanggal sesuai dengan "yyyy-MM-dd"
                        String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        tanggalLahireditText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Set listener untuk tombol daftar
        Button daftarButton = findViewById(R.id.button_lanjutkan);
        daftarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    showConfirmationDialog();
                }
            }
        });



        // Set listener untuk radio button gender
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_laki_laki) {
                    gender = "male";
                } else if (checkedId == R.id.radio_button_perempuan) {
                    gender = "female";
                }
            }
        });

        // Inisialisasi Retrofit API Service
        apiService = ApiClient.getApiClient().create(Api.class);

        Call<AgamaResponse> call = apiService.getAgama();
        call.enqueue(new Callback<AgamaResponse>() {
            @Override
            public void onResponse(Call<AgamaResponse> call, Response<AgamaResponse> response) {
                if (response.isSuccessful()) {
                    AgamaResponse agamaResponse = response.body();

                    if (agamaResponse.isSuccess()) {
                        List<String> agamaList = new ArrayList<>();
                        agamaList.add("");
                        agamaList.addAll(agamaResponse.getReligions());

                        Spinner spinnerAgama = findViewById(R.id.spinner_agama);

                        AgamaSpinnerAdapter adapter = new AgamaSpinnerAdapter(PendaftaranSiswaActivity.this, agamaList);
                        spinnerAgama.setAdapter(adapter);
                    } else {
                        // Handle kesalahan jika diperlukan
                        String errorMessage = agamaResponse.getStatusMessage();
                        // Tampilkan pesan kesalahan
                    }

                }
            }

            @Override
            public void onFailure(Call<AgamaResponse> call, Throwable t) {

            }
        });
    }




    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Pendaftaran");

        builder.setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Panggil metode untuk mengirim permintaan pendaftaran
                sendRegisterRequest();
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Tidak melakukan apa-apa jika tombol Batal ditekan
            }
        });

        builder.show();
    }



    // Memeriksa izin akses penyimpanan dan memilih gambar dari galeri
    private void checkPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(PendaftaranSiswaActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PendaftaranSiswaActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            pickImageFromGallery();
        }
    }

    // Mengambil gambar dari galeri
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    // Menangani hasil pemilihan gambar dari galeri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ImageView imageView = findViewById(R.id.upload_image);
                imageView.setImageBitmap(bitmap);
                isImageUploaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Mengirim permintaan pendaftaran siswa ke server
    private void sendRegisterRequest() {
        // Membuat objek Siswa
        Siswa siswa = new Siswa();
        String nikString = nik_field.getText().toString();
        siswa.setNik(nikString);
        siswa.setFullname(mNamaField.getText().toString());
        siswa.setBirthplace(tempat_lahir_field.getText().toString());
        siswa.setBirthdate(tanggalLahireditText.getText().toString());
        siswa.setGender(gender);
        Spinner spinnerAgama = findViewById(R.id.spinner_agama);
        String selectedAgama = spinnerAgama.getSelectedItem().toString();
        siswa.setReligion(selectedAgama);
        siswa.setFather_name(namaAyahField.getText().toString());
        siswa.setFather_occupation(pekerjaanAyahField.getText().toString());
        siswa.setMother_name(namaIbuField.getText().toString());
        siswa.setMother_occupation(pekerjaanIbu.getText().toString());
        siswa.setAddress(alamatField.getText().toString());
        siswa.setGuardian_name(namaWali.getText().toString());
        siswa.setGuardian_occupation(pekerjaanWali.getText().toString());
        siswa.setGuardian_address(alamatWali.getText().toString());

        MultipartBody.Part filePart = null;
        if (imageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] fileBytes = byteArrayOutputStream.toByteArray();
                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileBytes);
                filePart = MultipartBody.Part.createFormData("files", "image.jpg", fileRequestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Membuat Map<String, RequestBody> untuk menyimpan properti siswa
        Map<String, RequestBody> siswaMap = new HashMap<>();

        // Mengatur setiap properti siswa ke dalam siswaMap dengan menggunakan RequestBody
        siswaMap.put("nik", RequestBody.create(MediaType.parse("text/plain"), siswa.getNik()));
        siswaMap.put("fullname", RequestBody.create(MediaType.parse("text/plain"), siswa.getFullname()));
        siswaMap.put("birthplace", RequestBody.create(MediaType.parse("text/plain"), siswa.getBirthplace()));
        siswaMap.put("birthdate", RequestBody.create(MediaType.parse("text/plain"), siswa.getBirthdate()));
        siswaMap.put("gender", RequestBody.create(MediaType.parse("text/plain"), siswa.getGender()));
        siswaMap.put("religion", RequestBody.create(MediaType.parse("text/plain"), siswa.getReligion()));
        siswaMap.put("father_name", RequestBody.create(MediaType.parse("text/plain"), siswa.getFather_name()));
        siswaMap.put("father_occupation", RequestBody.create(MediaType.parse("text/plain"), siswa.getFather_occupation()));
        siswaMap.put("mother_name", RequestBody.create(MediaType.parse("text/plain"), siswa.getMother_name()));
        siswaMap.put("mother_occupation", RequestBody.create(MediaType.parse("text/plain"), siswa.getMother_occupation()));
        siswaMap.put("address", RequestBody.create(MediaType.parse("text/plain"), siswa.getAddress()));
        siswaMap.put("guardian_name", RequestBody.create(MediaType.parse("text/plain"), siswa.getGuardian_name()));
        siswaMap.put("guardian_occupation", RequestBody.create(MediaType.parse("text/plain"), siswa.getGuardian_occupation()));
        siswaMap.put("guardian_address", RequestBody.create(MediaType.parse("text/plain"), siswa.getGuardian_address()));



        // Mengirim permintaan dengan menggunakan properti "files" dan "siswa" yang telah diatur
        Call<RegisterResponse> call = apiService.submitPpdbData(filePart, siswaMap);


        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PendaftaranSiswaActivity.this);
                        builder.setTitle("Pesan");
                        builder.setMessage(registerResponse.getMessage());

                        // Periksa apakah terdapat kesalahan pada isian NIK
                        Map<String, List<String>> errors = registerResponse.getErrors();
                        if (errors != null && errors.containsKey("nik")) {
                            List<String> nikErrors = errors.get("nik");
                            if (nikErrors != null && !nikErrors.isEmpty()) {
                                String errorMessage = nikErrors.get(0);
                                builder.setMessage(errorMessage);
                            }
                        }

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            finish();
                            }

                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                } else {
                    // Gagal mengirim data
                    Toast.makeText(PendaftaranSiswaActivity.this, "Gagal melakukan pendaftaran!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("Pendaftaran Gagal", "Gagal melakukan pendaftaran!" + "NIK sudah digunakan");



                AlertDialog.Builder builder = new AlertDialog.Builder(PendaftaranSiswaActivity.this);
                builder.setTitle("Pendaftaran Gagal");
                builder.setMessage("NIK sudah digunakan");

                // Tambahkan tombol "OK"
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kode yang akan dijalankan ketika tombol OK ditekan
                    }
                });

                // Tampilkan dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
    }



    private boolean validateInput() {
        if (nik_field.getText().toString().isEmpty()) {
            nik_field.setError("NIK harus diisi");
            return false;
        }

        if (mNamaField.getText().toString().isEmpty()) {
            mNamaField.setError("Nama harus diisi");
            return false;
        }

        if (tempat_lahir_field.getText().toString().isEmpty()) {
            tempat_lahir_field.setError("Tempat lahir harus diisi");
            return false;
        }

        if (tanggalLahireditText.getText().toString().isEmpty()) {
            tanggalLahireditText.setError("Tanggal lahir harus diisi");
            return false;
        }

        if (gender.isEmpty()) {
            // Jika jenis kelamin belum dipilih
            Toast.makeText(this, "Jenis kelamin harus dipilih", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (namaAyahField.getText().toString().isEmpty()) {
            namaAyahField.setError("Nama ayah harus diisi");
            return false;
        }

        if (pekerjaanAyahField.getText().toString().isEmpty()) {
            pekerjaanAyahField.setError("Pekerjaan ayah harus diisi");
            return false;
        }

        if (namaIbuField.getText().toString().isEmpty()) {
            namaIbuField.setError("Nama ibu harus diisi");
            return false;
        }

        if (pekerjaanIbu.getText().toString().isEmpty()) {
            pekerjaanIbu.setError("Pekerjaan ibu harus diisi");
            return false;
        }

        if (alamatField.getText().toString().isEmpty()) {
            alamatField.setError("Alamat harus diisi");
            return false;
        }

        return true;
    }

}
