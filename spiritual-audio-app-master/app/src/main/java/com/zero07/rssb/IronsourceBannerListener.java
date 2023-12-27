package com.zero07.rssb;

import android.content.Context;
import android.widget.Toast;

import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;

public class IronsourceBannerListener implements BannerListener {

    Context context;

    public IronsourceBannerListener(Context context) {
        this.context = context;
    }

    @Override
    public void onBannerAdLoaded() {
//        Toast.makeText(context.getApplicationContext(), "Banner Loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
//        Toast.makeText(context.getApplicationContext(), "Banner Failed to Load " + ironSourceError, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBannerAdClicked() {

    }

    @Override
    public void onBannerAdScreenPresented() {

    }

    @Override
    public void onBannerAdScreenDismissed() {

    }

    @Override
    public void onBannerAdLeftApplication() {

    }
}
