package com.zero07.rssb;

public class AdNetworkCode {

/***    FAN Mediation Auth to MoPub
 *
 *    System User Token : EAAVPF5HnByEBAAZAfzqyTyZCFaeshiKFGNIlsi4v0OG9gZCF6yzh8aJDpOiZCzoY1KZAiiPgIq4lw8hsypQ25kjWcs2lAlvxgBT94ibAEjrmfm4iDLHgqqZBt6y0lIympjXUZCq4NFcfxRlHFZACJ93F0IQ9XBQVrqVtSk2KsY7CH0TdZAIcL30mZC5CkCOxGj8jiJVw8VZAh7VjgZDZD
 *
 *          Property ID : 3099714830258533
 *
 *          Business ID : 1739347422928066
 *
 *
 *  Repeating
 *
 *  System User Token : EAAVPF5HnByEBAAZAfzqyTyZCFaeshiKFGNIlsi4v0OG9gZCF6yzh8aJDpOiZCzoY1KZAiiPgIq4lw8hsypQ25kjWcs2lAlvxgBT94ibAEjrmfm4iDLHgqqZBt6y0lIympjXUZCq4NFcfxRlHFZACJ93F0IQ9XBQVrqVtSk2KsY7CH0TdZAIcL30mZC5CkCOxGj8jiJVw8VZAh7VjgZDZD
 *  *
 *  *     Property ID : 3099714830258533
 *  *
 *  *     Business ID : 1739347422928066
 *
 *
 *  Placement Name,     Placement ID,       Description,        Ad space,       Notes
 *
 * Satsang_Banner_1,        1494337534232353_1524153357917437   ,  ,RSSB Ad Space,
 * Satsang_Banner_2,        1494337534232353_1524153467917426   ,  ,RSSB Ad Space,
 * Web_View_Banner,         1494337534232353_1524154254584014   ,  ,RSSB Ad Space,
 * Story_Banner,            1494337534232353_1524154134584026   ,  ,RSSB Ad Space,
 * Book_Banner,             1494337534232353_1524153651250741   ,  ,RSSB Ad Space,
 * PDF_View_Banner,         1494337534232353_1524153791250727   ,  ,RSSB Ad Space,
 * Shabad_Banner_2,         1494337534232353_1524152314584208   ,  ,RSSB Ad Space,
 * Main_Banner,             1494337534232353_1494862657513174   ,  ,RSSB Ad Space,
 * Shabad_Banner_1,         1494337534232353_1494486360884137   ,  ,RSSB Ad Space,
 *
 */



/**** Mute Audio of AppLovin Ads ****/
//        Bundle extras = new AppLovinExtras.Builder()
//                .setMuteAudio(true)
//                .build();

/**** AppLovin User Consent ****/
//        appLovinPrivacySettings.setHasUserConsent(true, this);
//        appLovinPrivacySettings.setIsAgeRestrictedUser(true, this);
//        appLovinPrivacySettings.setDoNotSell( true, this );
/**** AppLovin User Consent ****/

/************ AppLovin Adaptive Banner Code ***********/
    private void appLovinAdaptiveBanner1() {
////        7790499f48bf636e Top Ad
////        260a1dfd31fb2239 Bottom Ad
//        MaxAdView adViewTop = new MaxAdView("7790499f48bf636e", this );
//
//        // Stretch to the width of the screen for banners to be fully functional
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        // Get the adaptive banner height.
//        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize( this ).getHeight();
//        int heightPx = AppLovinSdkUtils.dpToPx( this, heightDp );
//
//        adViewTop.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
//        adViewTop.setExtraParameter( "adaptive_banner", "true" );
//
////        ViewGroup rootView1 = findViewById( android.R.id.content );
//        ViewGroup rootView1 = findViewById(R.id.main_ad_container);
//        rootView1.addView(adViewTop);
//
//        adViewTop.loadAd();
    }
/************ AppLovin Adaptive Banner Code ***********/





/******* Facebook Audience Network

     //        "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID"
     //        320 x 50  "1494337534232353_1494862657513174"
     //        320 x 250 "1494337534232353_1494486360884137" ********/

//        fbAdView_50 = new AdView(this, "1494337534232353_1494862657513174", AdSize.BANNER_HEIGHT_50);
//
////        fbAdView_250 = new AdView(this, "1494337534232353_1494486360884137", AdSize.RECTANGLE_HEIGHT_250);
//
///** Find the Ad Container **/
//        LinearLayout adContainer_50 = (LinearLayout) findViewById(R.id.main_banner_50);
////        LinearLayout adContainer_250 = (LinearLayout) findViewById(R.id.main_banner_250);
//
///** Add the ad view to your activity layout **/
//        adContainer_50.addView(fbAdView_50);
////        adContainer_250.addView(fbAdView_250);
//
///** Request an ad **/
//        fbAdView_50.loadAd();
////        fbAdView_250.loadAd();

    /******* FAN Ad Listener ********/
//        AdListener fbAdListener = new AdListener() {
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                fbAdView_50.loadAd();
//            }

/******* Facebook Audience Network ********/

}
