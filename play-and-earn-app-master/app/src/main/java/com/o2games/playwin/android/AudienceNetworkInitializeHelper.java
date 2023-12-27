package com.o2games.playwin.android;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

import static com.facebook.ads.BuildConfig.DEBUG;

public class AudienceNetworkInitializeHelper implements AudienceNetworkAds.InitListener {

    /**
     * It's recommended to call this method from Application.onCreate().
     * Otherwise you can call it from all Activity.onCreate()
     * methods for Activities that contain ads.
     *
     * @param context Application or Activity.
     */
    static void initialize(Context context) {
        if (!AudienceNetworkAds.isInitialized(context)) {
            if (DEBUG) {          // might create problem
                AdSettings.turnOnSDKDebugger(context);
            }

            AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(new AudienceNetworkInitializeHelper())
                    .initialize();
        }
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        Log.d(AudienceNetworkAds.TAG, result.getMessage());
    }
}


/**** FAN Detail ****/
// ForceUpdateRemoteConfig ID : 368290494675359
// Property ID : 899932714256887
// System User Access Token : EAAFO9VAbZBZA8BAGYjy6aewHMBUpb07oNvgAayZBQkP9ex9xDRHkAB3pZAY8tlrZC9Lms1JGLBjinpZBvYfMo4WqYvjJ3bDgXl5tB4FFQPJ6VKVGoG4gDDsbpWNkRXYYR4qaIPyrpFeVZABvZB1ZCDZBaOBRDhclxtBthwdOvHzmuG3djaygHKOuMVkWzC72WJ4A7dZBSZCDpoh1GgZDZD
// System User Access Token 2nd : EAAFO9VAbZBZA8BAIxC2sZAUTcftTcBH1b5RKB2mRizWnYpt2LI0SGIAXHZBWev14kbQ6Rdcv0aci2bPUe8OlMt50hOi6p61KWUemiZBaJ4T3UAd47OrDcW2vGTqenqZBZA257mzx0kmG4kzkzYneiUXNRw7omIZAjDb0w7PbFmi5CEU1Xw5hg8HLRzo9GOLBuUlEumggYgn3AAZDZD
// Rewarded Placement : 368290494675359_368291821341893
// Banner Placement : 368290494675359_368298551341220

/******** Facebook Audience Network
 *
 * Banner all : 368290494675359_368298551341220
 *
// Daily reward : 368290494675359_368300781340997

 // golden flip : 368290494675359_368300918007650
 // golden scratch : 368290494675359_368301101340965
 // golden spin : 368290494675359_368301191340956

 // normal flip : 368290494675359_368301308007611
 // normal scratch : 368290494675359_368301388007603
 // normal Spin : 368290494675359_368301431340932

*********/

/******** MoPub
 *
 * Banner all : 2e0954ce227c4713b430ffa7165f3005
 *
 // Daily reward : fce3fb68efef45c1a51b5eba35decb49

 // golden flip : e4765853bc684f05b89bfaf1b85e3dad
 // golden scratch : 995de2e36a264adc922a1978372b21c7
 // golden spin : 995de2e36a264adc922a1978372b21c7

 // normal flip : 8323054635e044ef84a1712fbd6969f1
 // normal scratch : 4ff201db6eab4171a97ac0854b961e81
 // normal Spin : 92a2a3e6dbcd4d5c917108701b06e2c7

 *********/

/******** InMobi
 *
 * Account ID : ae9783fdca644c348ab4ca9b4bdd36d4
 * ForceUpdateRemoteConfig ID : 1629066343090
 * ForceUpdateRemoteConfig Key : 1629066343090
 *
 // BANNER all : 1629204892972

 // Daily reward : 1631366302586

 // golden flip : 1631872685019
 // golden scratch : 1631777928594
 // golden spin : 1630647529120

 // normal flip : 1628396509938
 // normal scratch : 1629350807485
 // normal Spin : 1628342074063

 *********/