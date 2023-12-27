package com.zero07.rssb.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ironsource.mediationsdk.IronSource;
import com.tapdaq.sdk.STATUS;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMInitListener;
import com.tapdaq.sdk.listeners.TMInitListenerBase;
import com.unity3d.ads.UnityAds;
import com.zero07.rssb.R;
import com.zero07.rssb.databinding.ActivitySplashScreenBinding;
import com.zero07.rssb.userModels.User;
import com.zero07.rssb.userModels.UserContext;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "splash";
    SplashScreenActivity splashScreenActivity = SplashScreenActivity.this;
    ActivitySplashScreenBinding binding;
    boolean connectedToAdNetwork;

    Animation fromLeft;
    Animation fromRight;
    Animation splashFade;
    ProgressBar splashProgressBar;
    Timer progressTimer;

    ConnectivityManager connectivityManager;
    int progressInt = 0;

    private String unityGameID = "4319067";
    private Boolean isTestModeOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fromLeft = AnimationUtils.loadAnimation(this, R.anim.anim_from_left);
        fromRight = AnimationUtils.loadAnimation(this, R.anim.anim_from_right);
        splashFade = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);

        splashProgressBar = findViewById(R.id.splash_progress_bar);

        binding.splashTitleImage.startAnimation(fromLeft);
        binding.splashTitleName.startAnimation(fromRight);
        binding.splashNote.startAnimation(splashFade);

        splashProgressBar.setProgress(progressInt);
        splashProgressBar.setMax(100);

        progressTimer = new Timer();

        if ( !internetConnected(this)) {
            showCustomDialog();
        } else {
            progressTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    progressInt = progressInt + 1;
                    splashProgressBar.setProgress(progressInt);

                    splashScreenActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.splashProgressPercent.setText(String.valueOf(progressInt) + "%");
                        }
                    });

                    if (splashProgressBar.getProgress() >= 98) {
                        splashScreenActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                initializeSDKs();      //   must be called on UI thread
                                checkAndInitializeSDK();
                            }
                        });
                    }

                    if (splashProgressBar.getProgress() >= 100) {
                        progressTimer.cancel();

                    }

                }
            }, 800, 20);
        }

    }



    private void initializeSDKs() {
/****  Starts Intializing Mobile Ads SDK ****/

/*** Admob SDK ***/

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
//                for (String adapterClass : statusMap.keySet()) {
//                    AdapterStatus status = statusMap.get(adapterClass);
//                    Log.d("RSSBApp", String.format(
//                            "Adapter name: %s, Description: %s, Latency: %d",
//                            adapterClass, status.getDescription(), status.getLatency()));
//                }
//
//                // Start loading ads here...
////                loadAdmobBanner();
//            }
//        });


/*** MoPub SDK with new method ***/
//        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("b195f8dd8ded45fe847ad89ed1d016da")
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


/**** Facebook Audience Network SDK ****/
//        AudienceNetworkAds.initialize(SplashScreenActivity.this);


/**** AppLovin SDK ***/
////         Please make sure to set the mediation provider value to "max" to ensure proper functionality
//        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
//        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
//            @Override
//            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
//                // AppLovin SDK is initialized, start loading ads
//
//            }
//        });

/****  END  Intializing Mobile Ads SDK ****/
    }

/************ MoPub SDK Listener Code ***********/
//    private SdkInitializationListener initSdkListener() {
//        return new SdkInitializationListener() {
//            @Override
//            public void onInitializationFinished() {
//                /* MoPub SDK initialized.*/
//                showToastSDKInitialized();
//            }
//        };
//    }
/************ MoPub SDK Listener Code ***********/

private void showToastSDKInitialized() {
        LayoutInflater inflater = getLayoutInflater();
        View rootView1 = inflater.inflate(R.layout.layout_toast_custom,
                (ConstraintLayout) findViewById(R.id.custom_toast_constraint));

        CardView toastCardView = rootView1.findViewById(R.id.toast_custom_card_view);
        TextView toastText = rootView1.findViewById(R.id.toast_custom_text);
//        ImageView toastIcon = rootView1.findViewById(R.id.toast_custom_image);

        toastCardView.setBackgroundResource(R.drawable.bg_custom_toast);

        toastText.setText("Welcome");
//        toastIcon.setImageResource(R.drawable.ic_paytm);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, 500);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(rootView1);

        toast.show();
    }


    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        View view = LayoutInflater.from(SplashScreenActivity.this).inflate(R.layout.layout_dialog_network,
                (ConstraintLayout) findViewById(R.id.layout_network_dialog_container));
        builder.setView(view);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();

        Button networkExit = view.findViewById(R.id.network_exit_butn);
        Button networkConnect = view.findViewById(R.id.network_connect_butn);

        networkConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
            }
        });

        networkExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

/**** Default Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        builder.setIcon(R.drawable.ic_exit)
                .setMessage("Please check your internet connection.")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                }).show();
Default Alert Dialog ****/

    }


/*****Checking Internet Connectivity****/
    private boolean internetConnected(SplashScreenActivity splashScreenActivity) {

        connectivityManager = (ConnectivityManager) splashScreenActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileDataConnection = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

        if ((wifiConnection != null && wifiConnection.isConnected()
                || (mobileDataConnection != null && mobileDataConnection.isConnected()))) {
            return true;
        } else {
            return false;
        }
    }
/*****Checking Internet Connectivity****/


    private void checkAndInitializeSDK() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean isStoryInsideApp = remoteConfig.getBoolean("openStoryInsideApp");
                    UserContext.setOpenStoryInsideApp(isStoryInsideApp);

                    boolean isAdmobEnabled = remoteConfig.getBoolean("isAdmob_enabled");
//                    boolean isAdmobEnabled = false;
                    UserContext.setIsAdmobEnabled(isAdmobEnabled);

                    boolean isTapdaqEnabled = remoteConfig.getBoolean("isTapdaq_enabled");
//                    boolean isTapdaqEnabled = false;
                    UserContext.setIsTapdaqEnabled(isTapdaqEnabled);

                    boolean isApplovinEnabled = remoteConfig.getBoolean("isApplovin_enabled");
//                    boolean isApplovinEnabled = true;
                    UserContext.setIsApplovinEnabled(isApplovinEnabled);
//
                    boolean isUnityEnabled = remoteConfig.getBoolean("isUnity_enabled");
//                    boolean isUnityEnabled = false;
                    UserContext.setIsUnityEnabled(isUnityEnabled);

                    boolean isIronsourceEnabled = remoteConfig.getBoolean("isIronsource_enabled");
//                    boolean isIronsourceEnabled = false;
                    UserContext.setIsIronsourceEnabled(isIronsourceEnabled);

                    if (isAdmobEnabled || isTapdaqEnabled || isApplovinEnabled || isUnityEnabled || isIronsourceEnabled) {

                        if (isAdmobEnabled) {
//                            boolean isAdmobInterstitialEnabled = remoteConfig.getBoolean("isAdmobInterstitial_enabled");
//                            UserContext.setIsAdmobInterstitialEnabled(isAdmobInterstitialEnabled);

                            MobileAds.initialize(SplashScreenActivity.this, new OnInitializationCompleteListener() {
                                @Override
                                public void onInitializationComplete(InitializationStatus initializationStatus) {
                                    Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                                    for (String adapterClass : statusMap.keySet()) {
                                        AdapterStatus status = statusMap.get(adapterClass);
                                        Log.d("RSSBApp", String.format(
                                                "Adapter name: %s, Description: %s, Latency: %d",
                                                adapterClass, status.getDescription(), status.getLatency()));
                                    }
                                    // Start loading ads here...
                                    showToastSDKInitialized();
                                    connectedToAdNetwork = true;
                                }
                            });
                        }

                        if (isTapdaqEnabled) {
//                            boolean isTapdaqInterstitial_enabled = remoteConfig.getBoolean("isTapdaqInterstitial_enabled");
//                            UserContext.setIsTapdaqInterstitialEnabled(isTapdaqInterstitial_enabled);

                            TapdaqConfig config = Tapdaq.getInstance().config();
                            config.setUserSubjectToGdprStatus(STATUS.FALSE); //GDPR declare if user is in EU
                            config.setConsentStatus(STATUS.TRUE); //GDPR consent must be obtained from the user
                            config.setAgeRestrictedUserStatus(STATUS.FALSE); //Is user subject to COPPA or GDPR age restrictions

                            Tapdaq.getInstance().initialize(SplashScreenActivity.this,
                                    getString(R.string.tapdaq_app_id), getString(R.string.tapdaq_client_key),
                                    config, new TMInitListenerBase() {
                                        @Override
                                        public void didInitialise() {
                                            connectedToAdNetwork = true;
                                            showToastSDKInitialized();
                                        }
                                        @Override
                                        public void didFailToInitialise(TMAdError tmAdError) {
                                            connectedToAdNetwork = false;
                                            showToastSDKInitialized();
                                        }
                                    });
                        }

                        if (isApplovinEnabled) {
//                            boolean isApplovinBanner = remoteConfig.getBoolean("isApplovin_banner");
//                            boolean isApplovinInterstitial = remoteConfig.getBoolean("isApplovin_interstitial");
//
//                            UserContext.setIsApplovinBanner(isApplovinBanner);
//                            UserContext.setIsApplovinInterstitial(isApplovinInterstitial);

                            AppLovinSdk appLovinSdk = AppLovinSdk.getInstance(SplashScreenActivity.this);
                            appLovinSdk.setMediationProvider("max");
                            appLovinSdk.initializeSdk(SplashScreenActivity.this, new AppLovinSdk.SdkInitializationListener() {
                                @Override
                                public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                                    // AppLovin SDK is initialized, start loading ads
                                    connectedToAdNetwork = true;
                                    AppLovinPrivacySettings.setIsAgeRestrictedUser(false, SplashScreenActivity.this);
                                    AppLovinPrivacySettings.setDoNotSell(true, SplashScreenActivity.this);
                                }
                            });
                        }

                        if (isUnityEnabled) {
                            UnityAds.initialize(getApplicationContext(), unityGameID, isTestModeOn);
                            showToastSDKInitialized();
//                            startActivity();
                        }

                        if (isIronsourceEnabled) {
//                            boolean isIronsourceBanner = remoteConfig.getBoolean("isIronsource_banner");
//                            boolean isIronsourceInterstitial = remoteConfig.getBoolean("isIronsource_interstitial");
//
//                            UserContext.setIsIronsource_banner(isIronsourceBanner);
//                            UserContext.setIsIronsource_interstitial(isIronsourceInterstitial);

                            AudienceNetworkAds.initialize(SplashScreenActivity.this);
                            IronSource.init(SplashScreenActivity.this, getString(R.string.ironSource_app_id));
                            IronSource.setMetaData("Facebook_IS_CacheFlag","ALL");

                            connectedToAdNetwork = true;
                        }

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity();
                            }
                        }, 2000);

                    } else {
                        startActivity();
                    }
                } else {
                    Log.e(TAG, "Exception while remote config is fetched.");
                }
            }
        });

    }

    private void startActivity() {
//        if (connectedToAdNetwork) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//        } else {
//            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//        }
    }


    @Override
    protected void onPause() {
        IronSource.onPause(this);

        super.onPause();
    }

    @Override
    protected void onResume() {
        IronSource.onPause(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}