package com.example.conexaoapi.api;

import com.example.conexaoapi.model.Feeds;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FeedApi {

    @POST("/posts")
    Call<Feeds> salvarFeed(@Body Feeds feeds);
}
