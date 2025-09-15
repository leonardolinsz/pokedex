package com.example.conexaoapi.api;

import com.example.conexaoapi.model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotoApi {
    @GET("/v2/list?limit=10")
    Call<List<Photo>> getAllPhotos();

    @GET("/v2/list")
    Call<List<Photo>> getPhoto(@Query("limit") int limit);

}
