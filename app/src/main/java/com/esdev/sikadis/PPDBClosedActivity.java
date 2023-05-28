package com.esdev.sikadis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.esdev.sikadis.retrofit.ApiClient;
import com.esdev.sikadis.retrofit.Api;
import com.esdev.sikadis.responses.PpdbStatusResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PPDBClosedActivity extends AppCompatActivity {

    private TextView textViewYear;

    private Api apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppdb_closed);

        textViewYear = findViewById(R.id.textView);

        fetchDataFromApi();

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void fetchDataFromApi() {
        apiService = ApiClient.getApiClient().create(Api.class);
        Call<PpdbStatusResponse> call = apiService.getPpdbStatus();

        call.enqueue(new Callback<PpdbStatusResponse>() {
            @Override
            public void onResponse(Call<PpdbStatusResponse> call, Response<PpdbStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PpdbStatusResponse.PpdbStatusData data = response.body().getData();
                    if (data != null) {
                        String academicYear = data.getAcademicYear();
                        textViewYear.setText("Pendaftaran Peserta Didik Baru " + academicYear);
                    }
                } else {
                    Toast.makeText(PPDBClosedActivity.this, "Gagal mengambil data dari API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PpdbStatusResponse> call, Throwable t) {
                Toast.makeText(PPDBClosedActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
