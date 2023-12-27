package com.zero07.rssb;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

public class TapdaqBannerListener extends TMAdListener {

    Context context;

    @Override
    public void didLoad() {
        Toast.makeText(context, "Ad Loaded", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "Ad Loaded")
        // First banner loaded into view
    }
    @Override
    public void didFailToLoad(TMAdError error) {
        Toast.makeText(context, "Ad FAILED to Load", Toast.LENGTH_SHORT).show();
        // No banners available. View will stop refreshing
    }
    @Override
    public void didRefresh() {
        // Subequent banner loaded, this view will refresh every 30 seconds
    }
    @Override
    public void didFailToRefresh(TMAdError error) {
        // Banner could not load, this view will attempt another refresh every 30 seconds
    }
    @Override
    public void didClick() {
        // User clicked on banner
    }
}
