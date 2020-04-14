package com.example.week09;

import com.example.week09.Entities.CoinLoreResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoinService {
    @GET("/api/tickers")
    Call<CoinLoreResponse> getCoins();
}
