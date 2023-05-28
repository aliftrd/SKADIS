package com.esdev.sikadis.retrofit;

import com.esdev.sikadis.responses.LayananResponse;
import com.esdev.sikadis.responses.PostResponse;
import com.esdev.sikadis.responses.PpdbStatusResponse;
import com.esdev.sikadis.responses.SliderResponse;

import retrofit2.Call;
import retrofit2.http.GET;
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
}
