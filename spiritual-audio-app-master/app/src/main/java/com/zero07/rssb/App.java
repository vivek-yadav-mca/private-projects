package com.zero07.rssb;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.zero07.rssb.activity.MainActivity;

public class App extends Application {

    Context context;
    public static IronSourceBannerLayout ironSourceBanner;

//    public App(Context context) {
//        this.context = context;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        IronSource.init((Activity) context, getString(R.string.ironSource_app_id), IronSource.AD_UNIT.BANNER);
        ironSourceBanner = IronSource.createBanner((Activity) context, ISBannerSize.BANNER);
//        binding.mainAdContainer.addView(ironSourceBanner);

        ironSourceBanner.setBannerListener(new IronsourceBannerListener(this));
//        IronSource.loadBanner(ironSourceBanner);

    }

    public void showIronsourceBanner(FrameLayout adContainer, Context context) {
        this.context = context;

        adContainer.removeAllViews();

        adContainer.addView(ironSourceBanner);
        IronSource.loadBanner(ironSourceBanner);
    }
}
