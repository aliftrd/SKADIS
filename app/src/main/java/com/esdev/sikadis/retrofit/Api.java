package com.esdev.sikadis.retrofit;

import com.esdev.sikadis.PendaftaranSiswaActivity;
import com.esdev.sikadis.models.Siswa;
import com.esdev.sikadis.responses.AgamaResponse;
import com.esdev.sikadis.responses.LayananResponse;
import com.esdev.sikadis.responses.PostResponse;
import com.esdev.sikadis.responses.PpdbStatusResponse;
import com.esdev.sikadis.responses.RegisterResponse;
import com.esdev.sikadis.responses.SliderResponse;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface Api {
    @GET("posts")
    Call<PostResponse> getPosts();

    @GET
    Call<PostResponse> getPostsByUrl(@Url String url);

    @GET("sliders")
    Call<SliderResponse> getSliders();

    @GET("ppdb")
    Call<PpdbStatusResponse> getPpdbStatus();

    @GET("meta")
    Call<LayananResponse> getMeta();

    @GET("religion")
    Call<AgamaResponse> getAgama();

    @Multipart
    @POST("ppdb")
    Call<RegisterResponse> submitPpdbData(
            @Part MultipartBody.Part image,
            @PartMap Map<String, RequestBody> siswa
    );









}
