package com.zero07.rssb.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zero07.rssb.R;
import com.zero07.rssb.databinding.ActivityPhotoBinding;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private ActivityPhotoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        binding = ActivityPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

/******** Start Admob Ads Request *******/
//        //   Admob Ads Request
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);
//
//        binding.adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.e(TAG, "Error while loading ads: " + loadAdError.getMessage());
//                binding.adView.loadAd(adRequest);
//            }
//        });
/******** End Admob Ads Request *******/


    }
}