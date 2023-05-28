package com.esdev.sikadis.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.esdev.sikadis.PPDBClosedActivity;
import com.esdev.sikadis.PendaftaranSiswaActivity;
import com.esdev.sikadis.R;
import com.esdev.sikadis.activities.InfoPPDBActivity;
import com.esdev.sikadis.responses.PpdbStatusResponse;
import com.esdev.sikadis.retrofit.Api;
import com.esdev.sikadis.retrofit.ApiClient;
import com.esdev.sikadis.retrofit.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendaftaranFragment extends Fragment {

    private CardView cv1; // Updated variable type
    private CardView cv2; // Added variable for "Panduan PPDB" card
    private Api apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendaftaran, container, false);

        apiService = ApiClient.getApiClient().create(Api.class);
        cv1 = view.findViewById(R.id.cv1); // Updated ID
        cv2 = view.findViewById(R.id.cv2); // Added ID for "Panduan PPDB" card

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPpdbStatusFromApi();
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InfoPPDBActivity.class));
            }
        });

        return view;
    }

    private void getPpdbStatusFromApi() {
        Call<PpdbStatusResponse> call = apiService.getPpdbStatus();
        call.enqueue(new Callback<PpdbStatusResponse>() {
            @Override
            public void onResponse(Call<PpdbStatusResponse> call, Response<PpdbStatusResponse> response) {
                if (response.isSuccessful()) {
                    PpdbStatusResponse ppdbStatusResponse = response.body();
                    if (ppdbStatusResponse != null && ppdbStatusResponse.isSuccess()) {
                        PpdbStatusResponse.PpdbStatusData ppdbStatusData = ppdbStatusResponse.getData();
                        int status = ppdbStatusData.getStatus();
                        int ppdb = ppdbStatusData.getPpdb();

                        if (status == 1) {
                            if (ppdb == 1) {
                                startActivity(new Intent(getActivity(), PendaftaranSiswaActivity.class));
                            } else {
                                startActivity(new Intent(getActivity(), PPDBClosedActivity.class));
                            }
                        } else {
                            startActivity(new Intent(getActivity(), PPDBClosedActivity.class));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PpdbStatusResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Gagal memuat status PPDB", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
