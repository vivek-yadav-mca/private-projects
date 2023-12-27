package com.zero07.rssb;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMBannerAdSizes;

public class AdsActivity extends AppCompatActivity {

//    AdView fbAdView_50;
//    AdView fbAdView_50x1;
//    AdView fbAdView_50x2;
//    AdView fbAdView_50x3;
//    AdView fbAdView_50x4;
//    AdView fbAdView_50x5;
//
//    AdView fbAdView_250;
    AdManagerAdView adManagerAdView;
    AdManagerAdView adView;
    TMBannerAdView tapdaqBanner;

    FrameLayout adContainerLayout50x3, adContainerLayout50x4;
    AdView testAdView;

//    MaxRewardedAd appLovinRewardedAdView;

//    private MaxAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        Button rewardedAdButton = findViewById(R.id.rewarded_ad_button);

        MobileAds.initialize(this);

        adManagerAdView = findViewById(R.id.adManagerAdView);
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
//        adManagerAdView.loadAd(adRequest);

        adView = new AdManagerAdView(this);
        adContainerLayout50x3 = findViewById(R.id.ads_activity_banner_50x3);
        adContainerLayout50x3.addView(adView);
        adView.setAdUnitId("/6499/example/banner");
        AdSize adaptiveSize = getAdSize();
        adView.setAdSizes(adaptiveSize, AdSize.BANNER);
        AdManagerAdRequest adRequest1 = new AdManagerAdRequest.Builder().build();
//        adView.loadAd(adRequest1);

//        TMBannerAdView ad = (TMBannerAdView) findViewById(R.id.tapdaqBanner);
//        ad.load(this, "<INSERT_PLACEMENT_TAG>", TMBannerAdSizes.STANDARD, new TapdaqBannerListener());

        tapdaqBanner = new TMBannerAdView(this);
        adContainerLayout50x4 = findViewById(R.id.ads_activity_banner_50x4);
        adContainerLayout50x4.addView(tapdaqBanner);
        tapdaqBanner.load(AdsActivity.this, "banner_main", TMBannerAdSizes.STANDARD, new TapdaqBannerListener());

        rewardedAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadApplovinRewardedAd();
            }
        });

/*** MoPub SDK with new method ***/
//        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("ab584f170a8a4f7ea163ad07679f09cb")
//                /*.withMediationSettings("MEDIATION_SETTINGS")
//                .withAdditionalNetworks(CustomAdapterConfiguration.class.getName())
//                .withMediatedNetworkConfiguration(CustomAdapterConfiguration1.class.getName(), mediatedNetworkConfiguration)
//                .withMediatedNetworkConfiguration(CustomAdapterConfiguration2.class.getName(), mediatedNetworkConfiguration)
//                .withMediatedNetworkConfiguration(CustomAdapterConfiguration1.class.getName(), mediatedNetworkConfiguration1)
//                .withMediatedNetworkConfiguration(CustomAdapterConfiguration2.class.getName(), mediatedNetworkConfiguration2)
//                .withLogLevel(MoPubLog.LogLevel.Debug)*/
//                .withLegitimateInterestAllowed(false)
//                .build();
//
//        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());

/****    Intializing Mobile Ads SDK ****/

//        SdkConfiguration.Builder sdkConfiguration = new SdkConfiguration.Builder("ab584f170a8a4f7ea163ad07679f09cb");
//        MoPub.initializeSdk(this, sdkConfiguration.build(), initSdkListener());

/****** MoPub Ad Code ******/
    /*** Normal Banner ***/
//    moPubView = findViewById(R.id.mopubAdView);
//    moPubView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
//    moPubView.loadAd();
//
//    moPubView1 = findViewById(R.id.mopubAdView1);
//    moPubView1.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
//    moPubView1.loadAd();

//        loadMoPubBanner();
//        loadMoPubBanner1();


/**** MoPub Ad Code ****/


/**** AppLovin Ad Code ****/
//        AppLovinAdView testBanner = new AppLovinAdView(AppLovinAdSize.BANNER, this);
//        ViewGroup rootView = findViewById(R.id.ads_activity_banner_50x2);
//        rootView.addView(testBanner);
//        testBanner.loadNextAd();

//        MaxAdView adView1 = new MaxAdView("260a1dfd31fb2239", this );
//        ViewGroup rootView1 = findViewById(R.id.ads_activity_banner_50x3);
//        rootView1.addView(adView1);
//        adView1.loadAd();
//
//        testAdaptiveBanner();

//        adView = new MaxAdView( "YOUR_AD_UNIT_ID", this );
//        adView.setListener( this );
//
/**** AppLovin Adaptive Banner Code ****/
//        // Stretch to the width of the screen for banners to be fully functional
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        // Get the adaptive banner height.
//        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
//        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
//
//        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
//        adView.setExtraParameter( "adaptive_banner", "true" );
//
//        // Set background or background color for banners to be fully functional
//        adView.setBackgroundColor( R.color.background_color );
//
//        ViewGroup rootView = findViewById( android.R.id.content );
//        rootView.addView( adView );
//
//        // Load the ad
//        adView.loadAd();
/**** AppLovin Ad Code ****/

/******* Admob Adaptive Banner Code ********/
//        adContainerView = findViewById(R.id.adView_frameLayout);
//        // Step 1 - Create an AdView and set the ad unit ID on it.
//        testAdView = new AdView(this);
//        testAdView.setAdUnitId(getString(R.string.main_activity_admob_unit_50));
//        adContainerView.addView(testAdView);
//        loadBanner();

/**** Instead of creating another method you can use following method ****/
//        bookAdView = new AdView(this);
//        bookAdView.setAdUnitId(getString(R.string.book_activity_admob_unit_50));
//        bookAdView.setAdSize(getAdSize());
//        binding.bookAdContainer.addView(bookAdView);
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        bookAdView.loadAd(adRequest);
        /******* Admob Adaptive Banner Code ********/

        /******* Facebook Audience Network ********/

//        "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
//        320x250 1494337534232353_1494486360884137

//        fbAdView_50 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//        fbAdView_50x1 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//        fbAdView_50x2 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//        fbAdView_50x3 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//        fbAdView_50x4 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//        fbAdView_50x5 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//
//        fbAdView_250 = new AdView(this, "1494337534232353_1494486360884137", AdSize.RECTANGLE_HEIGHT_250);
//
///*** Find the Ad Container ***/
//        LinearLayout adContainer_50 = (LinearLayout) findViewById(R.id.ads_activity_banner_50);
//        LinearLayout adContainer_50x1 = (LinearLayout) findViewById(R.id.ads_activity_banner_50x1);
//        LinearLayout adContainer_50x2 = (LinearLayout) findViewById(R.id.ads_activity_banner_50x2);
//        LinearLayout adContainer_50x3 = (LinearLayout) findViewById(R.id.ads_activity_banner_50x3);
//        LinearLayout adContainer_50x4 = (LinearLayout) findViewById(R.id.ads_activity_banner_50x4);
//        LinearLayout adContainer_50x5 = (LinearLayout) findViewById(R.id.ads_activity_banner_50x5);
//
//        LinearLayout adContainer_250 = (LinearLayout) findViewById(R.id.ads_activity_banner_250);
//
///*** Add the ad view to your activity layout ***/
//        adContainer_50.addView(fbAdView_50);
//        adContainer_50x1.addView(fbAdView_50x1);
//        adContainer_50x2.addView(fbAdView_50x2);
//        adContainer_50x3.addView(fbAdView_50x3);
//        adContainer_50x4.addView(fbAdView_50x4);
//        adContainer_50x5.addView(fbAdView_50x5);
//
//        adContainer_250.addView(fbAdView_250);
//
///*** Request an ad ***/
//        fbAdView_50.loadAd();
//        fbAdView_50x1.loadAd();
//        fbAdView_50x2.loadAd();
//        fbAdView_50x3.loadAd();
//        fbAdView_50x4.loadAd();
//        fbAdView_50x5.loadAd();
//
//        fbAdView_250.loadAd();
/******* Facebook Audience Network ********/

//        AdListener adListener = new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Request an ad
//                fbAdView.loadAd(fbAdView.buildLoadAdConfig().withAdListener(adListener).build());
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };

    }

/************ MoPub SDK Listener Code ***********/
//    private SdkInitializationListener initSdkListener() {
//        return new SdkInitializationListener() {
//            @Override
//            public void onInitializationFinished() {
//                /* MoPub SDK initialized.*/
//                Toast.makeText(AdsActivity.this, "SDK Initialized", Toast.LENGTH_LONG).show();
//                loadMoPubBanner_FL();
//                loadMoPubBanner_D();
//            }
//        };
//    }
/************ MoPub SDK Listener Code ***********/

    private void loadMoPubBanner_FL(){
//        moPubView_FL = findViewById(R.id.mopubAdViewFL);
//        moPubView_FL.setAdUnitId(Constants.MOPUB_MAIN_BANNER);
//        moPubView_FL.loadAd();
    }

    private void loadMoPubBanner_D(){
//    moPubView_D = findViewById(R.id.mopubAdViewDirectView);
//    moPubView_D.setAdUnitId(Constants.MOPUB_MAIN_BANNER);
//    moPubView_D.loadAd();
    }


    private void loadApplovinRewardedAd() {
//        appLovinRewardedAdView = MaxRewardedAd.getInstance("dc05b818c90cd225", this);
//        appLovinRewardedAdView.loadAd();
//
//        appLovinRewardedAdView.setListener(new MaxRewardedAdListener() {
//            @Override
//            public void onRewardedVideoStarted(MaxAd ad) {
//            }
//            @Override
//            public void onRewardedVideoCompleted(MaxAd ad) {
//            }
//            @Override
//            public void onUserRewarded(MaxAd ad, MaxReward reward) {
//            }
//            @Override
//            public void onAdLoaded(MaxAd ad) {
//                if (appLovinRewardedAdView.isReady()) {
//                    appLovinRewardedAdView.showAd();
//                }
//            }
//            @Override
//            public void onAdDisplayed(MaxAd ad) {
//            }
//            @Override
//            public void onAdHidden(MaxAd ad) {
//            }
//            @Override
//            public void onAdClicked(MaxAd ad) {
//            }
//            @Override
//            public void onAdLoadFailed(String adUnitId, MaxError error) {
//            }
//            @Override
//            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//            }
//        });


    }

//    private void testApplovinAdaptiveBanner() {
//        MaxAdView adView = new MaxAdView("7790499f48bf636e", this );
//
//        // Stretch to the width of the screen for banners to be fully functional
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        // Get the adaptive banner height.
//        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
//        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
//
//        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
//        adView.setExtraParameter( "adaptive_banner", "true" );
//
//        ViewGroup rootView2 = findViewById(R.id.ads_activity_banner_50x4);
//        rootView2.addView(adView);
//
//        adView.loadAd();
//    }

//    private void loadBanner() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        AdSize adSize = getAdSize();
//
//        // Step 4 - Set the adaptive ad size on the ad view.
//        testAdView.setAdSize(adSize);
//
//        // Step 5 - Start loading the ad in the background.
//        testAdView.loadAd(adRequest);
//    }

//    private AdSize getAdSize() {
//        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//
//        float widthPixels = outMetrics.widthPixels;
//        float density = outMetrics.density;
//
//        int adWidth = (int) (widthPixels / density);
//
//        // Step 3 - Get adaptive ad size and return for setting on the ad view.
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
//    }

    /****  AdManager Size getting  ****/
    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        moPubView.destroy();
    }


//    private void createBannerAd
//    {
//        adView = new MaxAdView( "YOUR_AD_UNIT_ID", this );
//        adView.setListener( this );
//
//        // Stretch to the width of the screen for banners to be fully functional
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        // Banner height on phones and tablets is 50 and 90, respectively
//        int heightPx = getResources().getDimensionPixelSize( R.dimen.banner_height );
//
//        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
//
//        // Set background or background color for banners to be fully functional
//        adView.setBackgroundColor( R.color.background_color );
//
//        ViewGroup rootView = findViewById( android.R.id.content );
//        rootView.addView( adView );
//
//        // Load the ad
//        adView.loadAd();
//    }


}