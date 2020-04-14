package com.example.week09;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.week09.Entities.Coin;
import com.example.week09.Entities.CoinLoreResponse;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private Coin mCoin;

    public DetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().containsKey(ARG_ITEM_ID)) {
            new DetailFragmentTask().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        // updateUi();
        return rootView;
    }

    private void updateUi() {
        View rootView = getView();
        if(mCoin != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            ((TextView) rootView.findViewById(R.id.tvName)).setText(mCoin.getName());
            ((TextView) rootView.findViewById(R.id.tvSymbol)).setText(mCoin.getSymbol());
            ((TextView) rootView.findViewById(R.id.tvValueField)).setText(formatter.format(Double.valueOf(mCoin.getPriceUsd())));
            ((TextView) rootView.findViewById(R.id.tvChange1hField)).setText(mCoin.getPercentChange1h() + " %");
            ((TextView) rootView.findViewById(R.id.tvChange24hField)).setText(mCoin.getPercentChange24h() + " %");
            ((TextView) rootView.findViewById(R.id.tvChange7dField)).setText(mCoin.getPercentChange7d() + " %");
            ((TextView) rootView.findViewById(R.id.tvMarketcapField)).setText(formatter.format(Double.valueOf(mCoin.getMarketCapUsd())));
            ((TextView) rootView.findViewById(R.id.tvVolumeField)).setText(formatter.format(Double.valueOf(mCoin.getVolume24())));
            ((ImageView) rootView.findViewById(R.id.ivSearch)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchCoin(mCoin.getName());
                }
            });
        }
    }

    private void searchCoin(String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + name));
        startActivity(intent);
    }

    private class DetailFragmentTask extends AsyncTask<Void, Integer, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.coinlore.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CoinService service = retrofit.create(CoinService.class);
            Call<CoinLoreResponse> coinsCall = service.getCoins();
            coinsCall.enqueue(new Callback<CoinLoreResponse>() {
                @Override
                public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response) {
                    List<Coin> coins = response.body().getData();
                    for(Coin coin : coins) {
                        if(coin.getId().equals(getArguments().getString(ARG_ITEM_ID))) {
                            mCoin = coin;
                            break;
                        }
                    }
                    updateUi();
                    DetailFragment.this.getActivity().setTitle(mCoin.getName());
                }

                @Override
                public void onFailure(Call<CoinLoreResponse> call, Throwable t) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            updateUi();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
