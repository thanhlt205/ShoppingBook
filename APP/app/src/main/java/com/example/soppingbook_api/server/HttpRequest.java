package com.example.soppingbook_api.server;

import static com.example.soppingbook_api.server.ApiService.DATABASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
ApiService apiService;

    public HttpRequest() {
        apiService = new Retrofit.Builder().baseUrl(DATABASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
