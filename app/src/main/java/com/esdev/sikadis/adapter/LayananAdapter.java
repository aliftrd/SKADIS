package com.esdev.sikadis.adapter;

import android.widget.TextView;

import com.esdev.sikadis.responses.LayananResponse;
import com.esdev.sikadis.retrofit.Api;
import com.esdev.sikadis.retrofit.ApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LayananAdapter {
    private static final String BASE_URL = "https://aliftrd.my.id/api/v1/"; // Ganti dengan URL API yang sesuai
    private TextView contentTextView;
    private Api api;

    public LayananAdapter() {
        Retrofit retrofit = ApiClient.getApiClient();

        api = retrofit.create(Api.class);
    }

    public void getMeta(final LayananCallback callback) {
        Call<LayananResponse> call = api.getMeta();
        call.enqueue(new Callback<LayananResponse>() {
            @Override
            public void onResponse(Call<LayananResponse> call, Response<LayananResponse> response) {
                if (response.isSuccessful()) {
                    LayananResponse layananResponse = response.body();
                    if (layananResponse != null) {
                        LayananResponse.LayananData layananData = layananResponse.getData();
                        callback.onSuccess(layananData);
                    } else {
                        callback.onError("Response body is null");
                    }
                } else {
                    callback.onError("Response is not successful");
                }
            }

            @Override
            public void onFailure(Call<LayananResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    public interface LayananCallback {
        void onSuccess(LayananResponse.LayananData layananData);

        void onError(String message);
    }
}
