package com.o2games.playwin.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdkUtils;
import com.appodeal.ads.Appodeal;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ironsource.mediationsdk.IronSource;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.adapter.MainFragmentAdapter;
import com.o2games.playwin.android.databinding.ActivityMainBinding;
import com.o2games.playwin.android.fragment.HomeFragment;
import com.o2games.playwin.android.model.LeaderboardModel;
import com.o2games.playwin.android.model.User;
import com.o2games.playwin.android.model.UserWalletDataModel;
import com.o2games.playwin.android.model.WinnerDataModel;
import com.o2games.playwin.android.userData.UserContext;
import com.o2games.playwin.android.versionChecker.ForceUpdateChecker;
import com.tapjoy.TJActionRequest;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompat {

    public static final int UPDATE_REQUEST_CODE = 100;
    private AppUpdateManager appUpdateManager;
    private static MainActivity instance;
    private ConnectivityManager connectivityManager;
    private BottomSheetDialog pastWinner_bsDialog;
    //    private List<WinnerDataModel> winnerDataModelsList;
    private WinnerDataModel winnerDataModel;
    private int winnerDataPosition;
    boolean doubleBackToExit = false;
    private static final String TAG = "papa";
    private final FirebaseDataService firebaseDataService = new FirebaseDataService(this);
    public static final String sql_Total_CashCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    private FirebaseAuth firebaseUser;
    DatabaseReference databaseRef;
    boolean autoCleanLeaderboard;
    public static long LEADERBOARD_DELETION_FIREBASE_FUTURE_TIME;

//    private boolean isAdmobBannerEnabled;
//    private boolean isAdmobInterstitialEnabled;
//    private boolean isAdmobRewardedEnabled;
//    private String ADMOB_BANNER_AD_UNIT_ID;
//    private String ADMOB_INTERSTITIAL_AD_UNIT_ID;
//    private String ADMOB_REWARDED_AD_UNIT_ID;
//    private AdView admobBanner;
//    private InterstitialAd admobInterstitial;
//    private RewardedAd admobRewarded;

//    private boolean isTapdaqBannerEnabled;
//    private boolean isTapdaqInterstitialEnabled;
//    private boolean isTapdaqRewardedEnabled;
//    private boolean isTapdaqNativeEnabled;
//    private static String TAPDAQ_BANNER_ID;
//    private static String TAPDAQ_INTERS_VIDEO_ID;
//    private static String TAPDAQ_REWARDED_ID;
//    private static String NATIVE_AD_UNIT_ID;
//    private TMBannerAdView tapdaqBanner;
//    private TDMediatedNativeAd tapdaqNativeAd;
//    private boolean isTapdaqNativeReady;
//    private TapdaqNativeLargeLayout tapdaqNativeLayout;

    private boolean isAppLovinBannerEnabled;
    private boolean isAppLovinInterstitialEnabled;
    private boolean isAppLovinRewardedEnabled;
    private static String APPLOVIN_BANNER_ID;
    private static String APPLOVIN_INTERSTITIAL_ID;
    private static String APPLOVIN_REWARDED_ID;
    private MaxAdView applovinBanner;
    private MaxInterstitialAd applovinInterstitial;
    private MaxRewardedAd applovinRewarded;

    private boolean isAppodealBannerEnabled;
    private boolean isAppodealInterstitialEnabled;

    private boolean isTapjoyOfferwallEnabled;
    private TJPlacement tapjoyOfferwall;
//    private boolean isIronSourceOfferwallEnabled;

//    private boolean isUnityInterstitialEnabled;
//    private boolean isUnityInterstitialReady = false;
//    private boolean isUnityRewardedEnabled;
//    private boolean isUnityRewardedReady = false;
//    private static String UNITY_INTERSTITIAL_ID;
//    private static String UNITY_REWARDED_ID;
//    private com.unity3d.mediation.InterstitialAd unityInterstitial;
//    private com.unity3d.mediation.RewardedAd unityRewarded;

    ActivityMainBinding binding;
    FirebaseRemoteConfig remoteConfig;
    ViewPager2 viewPager_main;
    BottomNavigationView bottomMenu;

    private static final boolean isDailyWinner = UserContext.getIsDailyWinnerLeaderboard();
    private static final boolean isWeeklyWinner = UserContext.getIsWeeklyWinnerLeaderboard();

    Handler handler;
    SharedPreferences writeSPref;
    SharedPreferences.Editor editorSPref;
    SharedPreferences readSPref;

    public static MainActivity GetInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appUpdateManager = AppUpdateManagerFactory.create(this);
        instance = this;

//        int red = 255; //i.e. FF
//        int green = 0;
//        int stepSize = 32; //how many colors do you want?
//        while(green < 255)
//        {
//            green += stepSize;
//            if(green > 255) { green = 255; }
//            output(red, green, 0); //assume output is function that takes RGB
//        }
//        while(red > 0)
//        {
//            red -= stepSize;
//            if(red < 0) { red = 0; }
//            output(red, green, 0); //assume output is function that takes RGB
//        }

        firebaseUser = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

//        startService(new Intent(this, LeaderboardBackgroundService.class));

        handler = new Handler(Looper.getMainLooper());
        autoCleanLeaderboard = UserContext.getAutoCleanLeaderboard();

        writeSPref = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
        editorSPref = writeSPref.edit();
        readSPref = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);


//        isAdmobBannerEnabled = UserContext.getIsAdmobBanner();
//        isAdmobInterstitialEnabled = UserContext.getIsAdmobInterstitial();
//        isAdmobRewardedEnabled = UserContext.getIsAdmobRewarded();
//        isTapdaqBannerEnabled = UserContext.getIsTapdaqBanner();
//        isTapdaqInterstitialEnabled = UserContext.getIsTapdaqInterstitial();
//        isTapdaqRewardedEnabled = UserContext.getIsTapdaqRewarded();
//        isTapdaqNativeEnabled = UserContext.getIsTapdaqNative();
        isAppLovinBannerEnabled = UserContext.getIsAppLovin_banner();
        isAppLovinInterstitialEnabled = UserContext.getIsAppLovin_interstitial();
        isAppLovinRewardedEnabled = UserContext.getIsAppLovin_rewarded();
//        isUnityInterstitialEnabled = UserContext.getIsUnity_interstitial();
//        isUnityRewardedEnabled = UserContext.getIsUnity_rewarded();
        isTapjoyOfferwallEnabled = UserContext.getIsTapjoy_offerwall();
//        isIronSourceOfferwallEnabled = UserContext.getIsIronSourceOfferwall();
        isAppodealBannerEnabled = UserContext.getIsAppodeal_banner();
        isAppodealInterstitialEnabled = UserContext.getIsAppodeal_interstitial();

//        ADMOB_BANNER_AD_UNIT_ID = getString(R.string.aM_banner_default);
//        ADMOB_INTERSTITIAL_AD_UNIT_ID = getString(R.string.aM_interstitial_default);
//        ADMOB_REWARDED_AD_UNIT_ID = getString(R.string.aM_rewarded_default);
//        TAPDAQ_BANNER_ID = getString(R.string.tad_banner_default);
//        TAPDAQ_INTERS_VIDEO_ID = getString(R.string.tad_inters_video_default);
//        TAPDAQ_REWARDED_ID = getString(R.string.tad_rewarded_default);
//        NATIVE_AD_UNIT_ID = getString(R.string.tad_native_default);
        APPLOVIN_BANNER_ID = getString(R.string.aL_banner_default);
        APPLOVIN_INTERSTITIAL_ID = getString(R.string.aL_interstitial_default);
        applovinInterstitial = new MaxInterstitialAd(APPLOVIN_INTERSTITIAL_ID, this);
        APPLOVIN_REWARDED_ID = getString(R.string.aL_rewarded_default);
//        UNITY_INTERSTITIAL_ID = getString(R.string.uni_interstitial_default);
//        UNITY_REWARDED_ID = getString(R.string.uni_rewarded_default);
        tapjoyOfferwall = Tapjoy.getPlacement(getString(R.string.tJ_offerwall_default), new TJOfferwallListener());


        showBannerFrom();
        loadOfferwallFrom();
        loadMainAct_interstitialFrom();
        continuousListenerForFirebaseData();

//        showPastWinner_bsDialog();
//        showPastWinnerDataAndRecyclerView();

        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        /**
         * Handle leaderboard deletion **/
        handleAutoCleanLeaderboard(currentTime);
        /** Handle leaderboard deletion
         **/

        if (currentTime >= getRatingBsDialogFutureTime()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showRatingBsDialog();
                }
            }, 2500);
        }
//        if (Long.parseLong(firebaseDataService.getCoinBalance()) >= 1000) {
//            showRatingBsDialog();
//        }

        viewPager_main = findViewById(R.id.viewPager_main_activity);
        bottomMenu = findViewById(R.id.bottom_menu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(fragmentManager, getLifecycle());
        viewPager_main.setAdapter(mainFragmentAdapter);

        bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager_main.setCurrentItem(0);
                        break;
                    case R.id.leaderboard:
                        viewPager_main.setCurrentItem(1);
                        break;
                    case R.id.offerwall_is:
                        viewPager_main.setCurrentItem(2);
                        break;
                    case R.id.wallet:
                        viewPager_main.setCurrentItem(3);
                        break;
                    case R.id.user_profile:
                        viewPager_main.setCurrentItem(4);
                }
                return true;
            }
        });

        viewPager_main.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomMenu.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        bottomMenu.getMenu().findItem(R.id.leaderboard).setChecked(true);
                        break;
                    case 2:
                        bottomMenu.getMenu().findItem(R.id.offerwall_is).setChecked(true);
                        break;
                    case 3:
                        bottomMenu.getMenu().findItem(R.id.wallet).setChecked(true);
                        break;
                    case 4:
                        bottomMenu.getMenu().findItem(R.id.user_profile).setChecked(true);
                        break;
                }
            }
        });

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean showImpNotice = remoteConfig.getBoolean(Constants.SHOW_HOME_IMP_NOTICE_DIALOG);

                    if (showImpNotice) {
                        String dialog_title = remoteConfig.getString(Constants.GET_HOME_IMP_NOTICE_TITLE);
                        String dialog_msg_eng = remoteConfig.getString(Constants.GET_HOME_IMP_NOTICE_TITLE_ENG);
                        String dialog_msg_hindi = remoteConfig.getString(Constants.GET_HOME_IMP_NOTICE_TITLE_HI);

                        showImportantNoticeDialog(dialog_title, dialog_msg_eng, dialog_msg_hindi);
                    }
                } else {
//                    Log.e(TAG, "Error in fetching Home Important Notice Dialog Data");
                }
            }
        });


        checkUpdate();
    }

//    private void output(int red, int green, int i) {
//        Log.d(TAG, "" + red + "," + green + "," + i);
//    }

    private void checkUpdate() {
        ForceUpdateChecker.with(this).onUpdateNeeded(this::onUpdateNeeded).check();

        if (UserContext.getGoogleForceUpdate()) {
//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
            // Returns an intent object that you use to check for an update.
            com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // This example applies an immediate update. To apply a flexible update
                        // instead, pass in AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                // The current activity making the update request.
                                // Include a request code to later monitor this update request.
                                appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
//                log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                checkUpdate();
            }
        }
    }

    private void handleImmediateUpdate() {
        // Checks that the update is not stalled during 'onResume()'.
        // However, you should execute this check at all entry points into the app.
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
//
//            appUpdateManager
//                    .getAppUpdateInfo()
//                    .addOnSuccessListener(
//                            appUpdateInfo -> {
//                                if (appUpdateInfo.updateAvailability()
//                                        == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                                    // If an in-app update is already running, resume the update.
//                                    appUpdateManager.startUpdateFlowForResult(
//                                            appUpdateInfo,
//                                            IMMEDIATE,
//                                            this,
//                                            MY_REQUEST_CODE);
//                                }
//                            });
    }

    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View rootView1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog_force_update,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_force_update));
        builder.setView(rootView1);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();

        View updateButton = rootView1.findViewById(R.id.forceUpdate_dialog_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectStore(updateUrl);
            }
        });

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void showRatingBsDialog() {
//        disableRatingBsFor(365);
//        ReviewManager manager = ReviewManagerFactory.create(this);
////        ReviewManager manager = new FakeReviewManager(this);
//
//        com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow();
//        request.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // We can get the ReviewInfo object
//                ReviewInfo reviewInfo = task.getResult();
//                com.google.android.play.core.tasks.Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
//                flow.addOnCompleteListener(task1 -> {
//                    // The flow has finished. The API does not indicate whether the user
//                    // reviewed or not, or even whether the review dialog was shown. Thus, no
//                    // matter the result, we continue our app flow.
//                });
//            } else {
//                // There was some problem, log or handle the error code.
////                @ReviewErrorCode int reviewErrorCode = ((TaskException) task.getException()).getErrorCode();
//            }
//        });

        BottomSheetDialog ratingBsDialog = new BottomSheetDialog(this, R.style.bottomSheetDialog);
        ratingBsDialog.setContentView(R.layout.layout_bsdialog_rating_playstore);
        ratingBsDialog.getDismissWithAnimation();
        ratingBsDialog.setCancelable(false);
        ratingBsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ratingBsDialog.show();

        RatingBar ratingBar = ratingBsDialog.findViewById(R.id.rating_dialog_ratingBar);
        CheckBox dontShowCheckBox = ratingBsDialog.findViewById(R.id.rating_dialog_checkBox);
        FrameLayout closeBtn = ratingBsDialog.findViewById(R.id.upstox_dialog_ac_open_btn);

        ratingBar.setRating(1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.APP_PLAYSTORE_LINK)));
                        ratingBsDialog.dismiss();
                    }
                }, 500);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dontShowCheckBox.isChecked()) {
                    disableRatingBsFor(168);
                } else {
                    disableRatingBsFor(24);
                }
                ratingBsDialog.dismiss();
            }
        });
    }

    private long getRatingBsDialogFutureTime() {
        long ratingBsDialogFutureTime = readSPref.getLong(Constants.SP_RATING_BSDIALOG_FUTURE_TIME, 0);
        return ratingBsDialogFutureTime;
    }

    private void disableRatingBsFor(int daysDisabled) {
        Date dateTime = new Date(System.currentTimeMillis());
        long btnCurrentTime = dateTime.getTime();

        Calendar calFutureTime = Calendar.getInstance();
        calFutureTime.setTimeInMillis(btnCurrentTime);
        calFutureTime.add(Calendar.DAY_OF_YEAR, daysDisabled);
        calFutureTime.set(Calendar.SECOND, 0);
        long btnEnable_futureTime = calFutureTime.getTimeInMillis();

        editorSPref.putLong(Constants.SP_RATING_BSDIALOG_FUTURE_TIME, btnEnable_futureTime);
        editorSPref.commit();

//        Toast.makeText(this, "Hidden for 24 hours", Toast.LENGTH_SHORT).show();
    }

    private void showImportantNoticeDialog(String dialog_title, String dialog_msg_eng, String dialog_msg_hindi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View rootView1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog_important_notice,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_important_notice));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog impNoticeDialog = builder.create();

        TextView title = rootView1.findViewById(R.id.dialog_imp_notice_title);
        TextView eng_msg = rootView1.findViewById(R.id.dialog_imp_notice_message_eng);
        TextView hindi_msg = rootView1.findViewById(R.id.dialog_imp_notice_message_hindi);
        Button closeBtn = rootView1.findViewById(R.id.dialog_imp_notice_close_btn);

        title.setText(dialog_title);
        eng_msg.setText(dialog_msg_eng);
        hindi_msg.setText(dialog_msg_hindi);

        if (TextUtils.isEmpty(dialog_msg_hindi)) {
            hindi_msg.setVisibility(View.GONE);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                impNoticeDialog.dismiss();
            }
        });

        impNoticeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        impNoticeDialog.show();
    }

    private void showBannerFrom() {
        if (isAppLovinBannerEnabled || isAppodealBannerEnabled) {
//        if (isAdmobBannerEnabled || isTapdaqBannerEnabled || isAppLovinBannerEnabled) {
//            if (isTapdaqBannerEnabled) {
//                tapdaqBanner = new TMBannerAdView(this);
//                binding.mainAdmobAdView.addView(tapdaqBanner);
//                tapdaqBanner.load(this, TAPDAQ_BANNER_ID, TMBannerAdSizes.STANDARD, new TMAdListener());
//            }
//
//            if (isAdmobBannerEnabled) {
//                FrameLayout adViewContainer = findViewById(R.id.main_admobAdView);
//                admobBanner = new AdView(this);
//                admobBanner.setAdUnitId(ADMOB_BANNER_AD_UNIT_ID);
//                binding.mainAdmobAdView.addView(admobBanner);
//
//                AdRequest adRequest = new AdRequest.Builder().build();
//                AdSize adSize = getAdSize();
//                admobBanner.setAdSize(adSize);
//                admobBanner.loadAd(adRequest);
//            }

            if (isAppLovinBannerEnabled) {
                binding.mainAdmobAdView.setVisibility(View.VISIBLE);
                applovinBanner = new MaxAdView(APPLOVIN_BANNER_ID, MainActivity.this);
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(MainActivity.this).getHeight();
                int heightPx = AppLovinSdkUtils.dpToPx(MainActivity.this, heightDp);
                applovinBanner.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                applovinBanner.setExtraParameter("adaptive_banner", "true");
                binding.mainAdmobAdView.addView(applovinBanner);
                applovinBanner.loadAd();
            }

            if (isAppodealBannerEnabled) {
                binding.mainAppodealBannerView.setVisibility(View.VISIBLE);
                Appodeal.setBannerViewId(R.id.main_appodealBannerView);
                Appodeal.show(this, Appodeal.BANNER_VIEW);
            }
        }
    }

    private AdSize getAdSize() {
        /** Step 2 - Determine the screen width (less decorations) to use for the ad width. **/
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        /** Step 3 - Get adaptive ad size and return for setting on the ad view. **/
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

//    private void refreshLeaderboardData() {
//        String userId = UserContext.getLoggedInUser().getId();
//        String userName = UserContext.getLoggedInUser().getUserName();
//        String userPhotoUrl = UserContext.getLoggedInUser().getUserPhotoUrl();
//
//        String sql_total_COIN = UserContext.getAllGameInOneMapByGameId(sql_Total_CashCoinsCOL).getCoins();
//        long userTotal_COIN = Long.parseLong(sql_total_COIN);
//
//        String sql_total_CASH = UserContext.getAllGameInOneMapByGameId(sql_Total_CashCoinsCOL).getCash();
//        long userTotal_CASH = Long.parseLong(sql_total_CASH);
//
//        String authUid = UserContext.getLoggedInUser().getAuthUid();
//        LeaderboardModel leaderboardModelData = new LeaderboardModel(userId, authUid, userName, userPhotoUrl, userTotal_COIN);
//
//        databaseRef
//                .child(Constants.TEST_DB_CHILD)
////                    .child(Constants.WALLET_BALANCE)
//                .child(UserContext.getLoggedInUser().getAuthUid())
//                .setValue(leaderboardModelData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(@NonNull @NotNull Void unused) {
//                        refreshLeaderboardAfter30();
//                    }
//                });
//    }

    private long getRefreshLeaderboardTime() {
        long refreshLeaderboardTime = readSPref.getLong(Constants.SP_REFRESH_LEADERBOARD_TIME, 0);
        return refreshLeaderboardTime;
    }

    private void refreshLeaderboardAfter30() {
        Date dateTime = new Date(System.currentTimeMillis());
        long btnCurrentTime = dateTime.getTime();

        Calendar calFutureTime = Calendar.getInstance();
        calFutureTime.setTimeInMillis(btnCurrentTime);
        calFutureTime.add(Calendar.MINUTE, 30);
        calFutureTime.set(Calendar.SECOND, 0);
        long refreshFutureTime = calFutureTime.getTimeInMillis();

        editorSPref.putLong(Constants.SP_REFRESH_LEADERBOARD_TIME, refreshFutureTime);
        editorSPref.commit();
    }

    /**
     * Leaderboard Auto Deletion Starts
     **/
    private void handleAutoCleanLeaderboard(long currentTime) {
        if (autoCleanLeaderboard) {
            if (currentTime < getLeaderboardDeletion_sPrefFutureTime()) {
                // When current time is less than saved future deletion time
                // That mean 24 hours has not been completed yet
                // DO NOTHING
//                Log.e("Leader clean time - ", "user current time is less than 24 hours");
            } else {
                // When current time is bigger that saved future deletion time
                // Now there will be 2 cases : future time == 0 ::: 24 hours is completed now
                if (getLeaderboardDeletion_sPrefFutureTime() == 0) {
                    // That means this is a new user OR
                    // User have cleared their app storage
                    getLeaderboardDeletion_firebaseFutureTime();
//                    Log.e("Leader clean time - ", "new user OR cleared storage user");
                } else {
                    getLeaderboardDeletion_firebaseFutureTime();
//                    Log.e("Leader clean time - ", "getting delete time from db");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (currentTime > LEADERBOARD_DELETION_FIREBASE_FUTURE_TIME) {
                                cleanLeaderboard();
//                                Log.e("Leader clean time - ", "cleanup started, leaderboard cleaned");
                            } else {
                                // Leaderboard is already cleaned by some other user
                                // DO NOTHING
//                                Log.e("Leader clean time - ", "cleanup delayed, someone else already cleaned the leaderboard");
                            }
                        }
                    }, 10000);
                }
            }
        }
    }

    private void cleanLeaderboard() {
        if (autoCleanLeaderboard) {
            databaseRef
                    .child(Constants.LEADERBOARD_TABLE)
                    /** Deleting Leaderboard Data
                     * **/
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull @NotNull Void unused) {
                            Date dateTime = new Date(System.currentTimeMillis());
                            long userTime_leaderboardDeletion = dateTime.getTime();

                            int clean_leaderboard_interval = (int) UserContext.getClean_leaderboard_interval();

                            Calendar calFutureTime = Calendar.getInstance();
                            calFutureTime.setTimeInMillis(userTime_leaderboardDeletion);
                            calFutureTime.add(Calendar.HOUR, clean_leaderboard_interval);

                            long dataDeletion_futureTime = calFutureTime.getTimeInMillis();

                            databaseRef
                                    .child(Constants.LEADERBOARD_DELETION_FUTURE_TIME)
                                    .setValue(dataDeletion_futureTime);

                            UserContext.setLeaderboardDeletion_futureTime(dataDeletion_futureTime);
                            setLeaderboardDeletion_sPrefFutureTime(dataDeletion_futureTime);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                        }
                    });
        }
    }

    private void getLeaderboardDeletion_firebaseFutureTime() {
        if (autoCleanLeaderboard) {
            databaseRef
                    .child(Constants.LEADERBOARD_DELETION_FUTURE_TIME)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                        LeaderboardModel model = snapshot.getValue(LeaderboardModel.class);
                            if (snapshot.getValue() == null) {
                                Date dateTime = new Date(System.currentTimeMillis());
                                long currentTime = dateTime.getTime();

                                Calendar calFutureTime = Calendar.getInstance();
                                calFutureTime.setTimeInMillis(currentTime);
                                calFutureTime.add(Calendar.HOUR, 1);

                                long self_future_time = calFutureTime.getTimeInMillis();

                                UserContext.setLeaderboardDeletion_futureTime(self_future_time);
                                setLeaderboardDeletion_sPrefFutureTime(self_future_time);
//                                Log.e("Leader clean time - ", "deletion time = null, added 1 hour and bypass cleanLeaderboard process");
                            } else {
                                long dataDeletion_futureTime = Long.parseLong(String.valueOf(snapshot.getValue()));

                                LEADERBOARD_DELETION_FIREBASE_FUTURE_TIME = dataDeletion_futureTime;
                                UserContext.setLeaderboardDeletion_futureTime(dataDeletion_futureTime);
                                setLeaderboardDeletion_sPrefFutureTime(dataDeletion_futureTime);

//                                Log.e("Leader clean time", "Delete time received = " + snapshot.getValue());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
//        return UserContext.getLeaderboardDeletion_futureTime();
    }

    private long getLeaderboardDeletion_sPrefFutureTime() {
        return readSPref.getLong(Constants.LEADERBOARD_DELETION_FUTURE_TIME, 0);
    }

    private void setLeaderboardDeletion_sPrefFutureTime(long dataDeletion_futureTime) {
        editorSPref.putLong(Constants.LEADERBOARD_DELETION_FUTURE_TIME, dataDeletion_futureTime);
        editorSPref.commit();
    }
    /**
     * Leaderboard Auto Deletion Ends
     **/



    /******
     *
     * * * * Ad Caching start for all fragments
     *
     * ******/



    /**
     * * * * * Interstitial
     ***/
    private void loadMainAct_interstitialFrom() {
//        if (isAdmobInterstitialEnabled || isTapdaqInterstitialEnabled || isAppLovinInterstitialEnabled || isAppodealInterstitialEnabled || isUnityInterstitialEnabled) {
        if (isAppLovinInterstitialEnabled || isAppodealInterstitialEnabled) {
            if (isAppLovinIntertitialReady() || Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
//            if (admobInterstitial != null || isTapdaqIntersVideoReady() || isAppLovinIntertitialReady() || isUnityInterstitialReady) {
                // Do Nothing
                // Don't load ad again
            } else {
//                if (isAdmobInterstitialEnabled) {
//                    loadAdmobInterstitial();
//                }
//                if (isTapdaqInterstitialEnabled) {
//                    loadTapdaqIntersVideo();
//                }
                if (isAppLovinInterstitialEnabled) {
                    loadAppLovinInterstitial();
                }
                if (isAppodealInterstitialEnabled) {
                    // Auto cached
//                    Appodeal.cache(this, Appodeal.INTERSTITIAL);
                }
//                if (isUnityInterstitialEnabled) {
//                    loadUnityInterstitial();
//                }
            }
        }
    }

    public void showMainAct_interstitialFrom() {
//        if (isAdmobInterstitialEnabled || isTapdaqInterstitialEnabled || isAppLovinInterstitialEnabled || isAppodealInterstitialEnabled || isUnityInterstitialEnabled) {
        if (isAppLovinInterstitialEnabled || isAppodealInterstitialEnabled) {
//            if (admobInterstitial != null || isTapdaqIntersVideoReady() || isAppLovinIntertitialReady() || Appodeal.isLoaded(Appodeal.INTERSTITIAL) || isUnityInterstitialReady) {
            if (isAppLovinIntertitialReady() || Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
//            if (isTapdaqIntersVideoReady()) {
//                showTapdaqIntersVideo();
//            }
//            if (admobInterstitial != null) {
//                showAdmobInterstitial();
//            }
                if (isAppLovinIntertitialReady()) {
                    showAppLovinInterstitial();
                }
                if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                    Appodeal.show(this, Appodeal.INTERSTITIAL);
                }
//                if (isUnityInterstitialReady) {
//                    showUnityInterstitial();
//                }
            } else {
                showCustomToast(getString(R.string.game_no_ad_available));
            }
        }
    }

    /**
     * Tapdaq Intertitial
     **/
//    private void loadTapdaqIntersVideo() {
////        showProgressDialog();
//        Tapdaq.getInstance().loadVideo(this, TAPDAQ_INTERS_VIDEO_ID, new TapdaqInterstitialVideoListener());
//    }
//
//    private boolean isTapdaqIntersVideoReady() {
//        boolean isReady = Tapdaq.getInstance().isVideoReady(this, TAPDAQ_INTERS_VIDEO_ID);
//        return isReady;
//    }
//
//    private void showTapdaqIntersVideo() {
//        Tapdaq.getInstance().showVideo(this, TAPDAQ_INTERS_VIDEO_ID, new TapdaqInterstitialVideoListener());
//    }
//
//    private class TapdaqInterstitialVideoListener extends TMAdListener {
//        @Override
//        public void didLoad() {
//            super.didLoad();
////            progressDialog.dismiss();
////            Log.e("Main Acti", "Tapdaq Inters Ad loaded");
//        }
//
//        @Override
//        public void didFailToLoad(TMAdError error) {
//            super.didFailToLoad(error);
////            progressDialog.dismiss();
////            Log.e("Main Acti", "Tapdaq Inters FAILED - " + error);
////            Toast.makeText(MainActivity.this, "Main Acti - " + "Tapdaq Inters FAILED - " + error, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void didClose() {
//            loadTapdaqIntersVideo();
//            super.didClose();
//        }
//    }
//
//    /**
//     * Admob Interstitial
//     **/
//    private void loadAdmobInterstitial() {
////        showProgressDialog();
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(this, ADMOB_INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                admobInterstitial = interstitialAd;
////                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                admobInterstitial = null;
////                Log.e(TAG, "Ad Failed to LOAD" + loadAdError);
////                progressDialog.dismiss();
//            }
//        });
//    }
//
//    private void showAdmobInterstitial() {
//        if (admobInterstitial != null) {
//            admobInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                    admobInterstitial = null;
//                }
//
//                @Override
//                public void onAdShowedFullScreenContent() {
//                }
//
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    admobInterstitial = null;
//                    loadAdmobInterstitial();
////                    showDialogAfterInterstitial(true, false, 0);
//                }
//            });
//            admobInterstitial.show(this);
//        }
//    }

    /**
     * AppLovin Intertitial
     **/
    private void loadAppLovinInterstitial() {
//        showProgressDialog();
        // Initialized in onCreate() method of this class
//        applovinInterstitial = new MaxInterstitialAd(APPLOVIN_INTERSTITIAL_ID, this);
        applovinInterstitial.setListener(new AppLovinInterstitialAdListener());
        applovinInterstitial.loadAd();
    }

    private void showAppLovinInterstitial() {
        // listener already attached in load() method
        applovinInterstitial.showAd();
    }

    private boolean isAppLovinIntertitialReady() {
        // Initialize MaxInterstitialAd in onCreate() method before checking .isReady()
        return applovinInterstitial.isReady();
    }

    private class AppLovinInterstitialAdListener implements MaxAdListener {
        @Override
        public void onAdLoaded(MaxAd ad) {
//            progressDialog.dismiss();
//            Log.e("Main activity - ", "aL ad loaded = ");
        }
        @Override
        public void onAdDisplayed(MaxAd ad) {
        }
        @Override
        public void onAdHidden(MaxAd ad) {
            // Ad closed, pre-load next ad
            loadAppLovinInterstitial();
//            showDialogAfterInterstitial(true, false, 0);
        }
        @Override
        public void onAdClicked(MaxAd ad) {
        }
        @Override
        public void onAdLoadFailed(String adUnitId, MaxError error) {
//            progressDialog.dismiss();
//            Log.e("Main activity - ", "aL ad Failed to load = " + error.getMessage());
        }
        @Override
        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        }
    }

    /**
     * Unity Intertitial
     **/
//    private void loadUnityInterstitial() {
////        showProgressDialog();
//        unityInterstitial = new com.unity3d.mediation.InterstitialAd(this, UNITY_INTERSTITIAL_ID);
//        isUnityInterstitialReady = false;
//        IInterstitialAdLoadListener unityIntersLoadListener = new IInterstitialAdLoadListener() {
//            @Override
//            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
////                progressDialog.dismiss();
//                isUnityInterstitialReady = true;
////                Log.e("Main Acti - ", "Unity Inters Ads LOADED");
//            }
//            @Override
//            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
////                progressDialog.dismiss();
//                isUnityInterstitialReady = false;
////                Log.e("Main Acti - ", "Unity Inters Ads FAILED to LOAD");
//            }
//        };
//        unityInterstitial.load(unityIntersLoadListener);
//    }
//
//    private void showUnityInterstitial() {
//        IInterstitialAdShowListener unityIntersShowListener = new IInterstitialAdShowListener() {
//            @Override
//            public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {
//                isUnityInterstitialReady = false;
////                Log.e("Main Acti - ", "Unity Inters Ads SHOWED");
//            }
//            @Override
//            public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {
//            }
//            @Override
//            public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {
//                isUnityInterstitialReady = false;
//                loadUnityInterstitial();
//            }
//            @Override
//            public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
//                isUnityInterstitialReady = false;
////                Log.e("Main Acti - ", "Unity Inters Ads FAILED to SHOW");
//            }
//        };
//        unityInterstitial.show(unityIntersShowListener);
//    }

    /******
     *
     * * * * Ad Caching done for all fragments
     *
     * ******/

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View rootView1 = inflater.inflate(R.layout.layout_toast_custom,
                (ConstraintLayout) findViewById(R.id.custom_toast_constraint));
        TextView toastText = rootView1.findViewById(R.id.custom_toast_text);
        toastText.setText(message);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(rootView1);
        toast.show();
    }

    private void continuousListenerForFirebaseData() {
        // get next winner announcement time
        getNextWinnerAnnouncementTime();

//        AllGameInOne sqlUserData = firebaseDataService.getAllGameInOneMapByGameId(sql_Total_CashCoinsCOL);
        databaseRef
                .child(Constants.LEADERBOARD_TABLE)
//                .child(UserContext.getLoggedInUser().getAuthUid())
                .child(firebaseUser.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        LeaderboardModel userLeaderDataModel = snapshot.getValue(LeaderboardModel.class);
                        User loggedInUser = UserContext.getLoggedInUser();

                        if (userLeaderDataModel != null) {
//                            HomeFragment homeFragment = HomeFragment.GetInstance();
////                            sqlUserData.setCoins(String.valueOf(userLeaderDataModel.getUserTotalCOIN()));
////                            dbHelper.updateFreeAdGameChanceAndCoinsData(sqlUserData);
                            firebaseDataService.updateSqlCoinData(userLeaderDataModel.getUserTotalCOIN());
//                            homeFragment.updateHomeUI_CashCoinWallet();
                        } else {
                            /** Either the Leaderboard has been cleared OR the user is new **/
                            if (Long.parseLong(firebaseDataService.getCoinBalance()) <= 0) {
                                // No SQL/Firebase Data = new user -or- leaderboard cleared & storage cleared
                                LeaderboardModel leaderboardDataModel = new LeaderboardModel(loggedInUser.getId(), loggedInUser.getAuthUid(),
                                        loggedInUser.getUserName(), loggedInUser.getUserPhotoUrl(),
                                        0);
                                databaseRef
                                        .child(Constants.LEADERBOARD_TABLE)
                                        .child(loggedInUser.getAuthUid())
                                        .setValue(leaderboardDataModel);
                            } else {
                                // only leaderboard has been cleared
                                // data found in sql
                                long userSQLCoins = Long.parseLong(firebaseDataService.getCoinBalance());

                                LeaderboardModel leaderboardDataModel = new LeaderboardModel(loggedInUser.getId(), loggedInUser.getAuthUid(),
                                        loggedInUser.getUserName(), loggedInUser.getUserPhotoUrl(),
                                        userSQLCoins);
                                databaseRef
                                        .child(Constants.LEADERBOARD_TABLE)
                                        .child(loggedInUser.getAuthUid())
                                        .setValue(leaderboardDataModel);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });

        databaseRef
                .child(Constants.USER_WALLET)
//                .child(UserContext.getLoggedInUser().getAuthUid())
                .child(firebaseUser.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserWalletDataModel userWalletDataModel = snapshot.getValue(UserWalletDataModel.class);
                        User loggedInUser = UserContext.getLoggedInUser();

                        if (userWalletDataModel != null) {
//                            HomeFragment homeFragment = HomeFragment.GetInstance();
////                            sqlUserData.setCash(String.valueOf(userWalletDataModel.getWalletAmount()));
////                            dbHelper.updateFreeAdGameChanceAndCoinsData(sqlUserData);
                            firebaseDataService.updateSqlCashData(userWalletDataModel.getWalletAmount());
//                            homeFragment.updateHomeUI_CashCoinWallet();
                        } else {
                            /** Either the Leaderboard has been cleared OR the user is new **/
                            UserWalletDataModel walletDataModel = new UserWalletDataModel(
                                    loggedInUser.getId(), loggedInUser.getAuthUid(), 0);
                            databaseRef
                                    .child(Constants.USER_WALLET)
                                    .child(loggedInUser.getAuthUid())
                                    .setValue(walletDataModel);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });
    }

    private void getNextWinnerAnnouncementTime() {
        long getNextWinnerAnnouncementTime = readSPref.getLong(Constants.NEXT_WINNER_ANNOUNCEMENT_TIME, 0);
        UserContext.setNextWinnerAnnouncementTime(getNextWinnerAnnouncementTime);

        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        if (isDailyWinner) {
            if (currentTime >= getNextWinnerAnnouncementTime) {
                //** Current time is more than next winner time **/
                databaseRef
                        .child(Constants.WINNER_LIST)
                        .child(Constants.WINNER_LIST_DAILY_LEADERBOARD)
                        .child(Constants.NEXT_WINNER_ANNOUNCEMENT_TIME)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.getValue() == null) {

                                } else {
                                    long nextWinnerAnnouncementTime = Long.parseLong(String.valueOf(snapshot.getValue()));

                                    editorSPref.putLong(Constants.NEXT_WINNER_ANNOUNCEMENT_TIME, nextWinnerAnnouncementTime);
                                    editorSPref.commit();
                                    UserContext.setNextWinnerAnnouncementTime(nextWinnerAnnouncementTime);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }
                        });
            } else {
                UserContext.setNextWinnerAnnouncementTime(getNextWinnerAnnouncementTime);
            }
        }

        if (isWeeklyWinner) {
            if (currentTime >= getNextWinnerAnnouncementTime) {
                //** Current time is more than next winner time **/
                databaseRef
                        .child(Constants.WINNER_LIST)
                        .child(Constants.WINNER_LIST_WEEKLY_LEADERBOARD)
                        .child(Constants.NEXT_WINNER_ANNOUNCEMENT_TIME)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.getValue() == null) {

                                } else {
                                    long nextWinnerAnnouncementTime = Long.parseLong(String.valueOf(snapshot.getValue()));

                                    editorSPref.putLong(Constants.NEXT_WINNER_ANNOUNCEMENT_TIME, nextWinnerAnnouncementTime);
                                    editorSPref.commit();
                                    UserContext.setNextWinnerAnnouncementTime(nextWinnerAnnouncementTime);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            }
                        });
            } else {
                UserContext.setNextWinnerAnnouncementTime(getNextWinnerAnnouncementTime);
            }
        }
    }

//    public void showPastWinnerDataAndRecyclerView() {
//        pastWinner_bsDialog = new BottomSheetDialog(this, R.style.bottomSheetDialog);
//        pastWinner_bsDialog.setContentView(R.layout.layout_bsdialog_past_winner_rv);
//        pastWinner_bsDialog.getDismissWithAnimation();
//        pastWinner_bsDialog.setCancelable(false);
//        pastWinner_bsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        RecyclerView pastWinnerRV = pastWinner_bsDialog.findViewById(R.id.bsdialog_past_winner_rv);
//        getPastWinnerList(pastWinnerRV);
//    }
//
//    private void getPastWinnerList(RecyclerView pastWinnerRV) {
//        List<WinnerDataModel> winnerDataModels = new ArrayList<>();
//        PastWinnerAdapter adapter = new PastWinnerAdapter(this, winnerDataModels);
//
//        databaseRef
//                .child(Constants.WINNER_LIST)
//                .child(Constants.WINNER_LIST_WEEKLY_LEADERBOARD)
//                .child(Constants.PAST_WEEK_WINNER_LIST)
//                .orderByChild(Constants.PAST_WINNER_ORDERING_PARENT)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                        winnerDataModels.clear();
//                        for (DataSnapshot pastWinnerList : snapshot.getChildren()) {
//                            WinnerDataModel pastWinnerListModel = pastWinnerList.getValue(WinnerDataModel.class);
//                            winnerDataModels.add(pastWinnerListModel);
//                        }
////                        Collections.reverse(winnerDataModels);
//                        adapter.notifyDataSetChanged();
//
//                        callPastWinner_bsDialog();
//                    }
//                    @Override
//                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                    }
//                });
//
//        pastWinnerRV.setLayoutManager(new GridLayoutManager(this, 1));
//        pastWinnerRV.setAdapter(adapter);
//    }
//
//    public void setWalletFragTop3PastWinner(List<WinnerDataModel> winnerDataModelsList, int position) {
////        winnerDataModel = new WinnerDataModel(model);
////        winnerDataModelsList.add(model);
//        winnerDataModel = winnerDataModelsList.get(position);
//        winnerDataPosition = position;
//
////        callPastWinner_bsDialog(model, position);
//    }
//
//    public void callPastWinner_bsDialog() {
//        pastWinner_bsDialog.show();
//
////        RecyclerView pastWinnerRV = pastWinner_bsDialog.findViewById(R.id.bsdialog_past_winner_rv);
//        ImageView closeBtn = pastWinner_bsDialog.findViewById(R.id.bsdialog_past_winner_close_iV_btn);
//
////        getPastWinnerList(pastWinnerRV);
//
//        ImageView rank1PlayerIV = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_1_player_imageV);
//        TextView rank1PlayerNameText = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_1_player_name_textV);
//        TextView rank1PlayerPrizeText = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_1_player_prizeAmt_textV);
//
//        ImageView rank2PlayerIV = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_2_player_imageV);
//        TextView rank2PlayerNameText = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_2_player_name_textV);
//        TextView rank2PlayerPrizeText = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_2_player_prizeAmt_textV);
//
//        ImageView rank3PlayerIV = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_3_player_imageV);
//        TextView rank3PlayerNameText = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_3_player_name_textV);
//        TextView rank3PlayerPrizeText = pastWinner_bsDialog.findViewById(R.id.bsdialog_rank_3_player_prizeAmt_textV);
//
//        RequestOptions loadImage = new RequestOptions()
//                .centerCrop()
//                .circleCrop()  //to crop image in circle view
//                .placeholder(R.drawable.user_color)
//                .error(R.drawable.user_color);
//
//        try {
//            if (winnerDataPosition == 0) {
//                Glide.with(this)
//                        .load(winnerDataModel.getUserPhotoUrl())
//                        .apply(loadImage)
//                        .into(rank1PlayerIV);
//
//                rank1PlayerNameText.setText(winnerDataModel.getUserName());
//                rank1PlayerPrizeText.setText("\u20B9" + winnerDataModel.getPrize());
////            marqueeText(binding.leaderboardRank1PlayerNameTextV);
//            }
//            if (winnerDataPosition == 1) {
//                Glide.with(this)
//                        .load(winnerDataModel.getUserPhotoUrl())
//                        .apply(loadImage)
//                        .into(rank2PlayerIV);
//
//                rank2PlayerNameText.setText(winnerDataModel.getUserName());
//                rank2PlayerPrizeText.setText("\u20B9" + winnerDataModel.getPrize());
////            marqueeText(binding.leaderboardRank2PlayerNameTextV);
//            }
//            if (winnerDataPosition == 2) {
//                Glide.with(this)
//                        .load(winnerDataModel.getUserPhotoUrl())
//                        .apply(loadImage)
//                        .into(rank3PlayerIV);
//
//                rank3PlayerNameText.setText(winnerDataModel.getUserName());
//                rank3PlayerPrizeText.setText("\u20B9" + winnerDataModel.getPrize());
////            marqueeText(binding.leaderboardRank3PlayerNameTextV);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        closeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pastWinner_bsDialog.dismiss();
//            }
//        });
//    }

    /**
     * Offerwall
     * **/

    public void loadOfferwallFrom() {
        if (isTapjoyOfferwallEnabled) {
//        if (isTapjoyOfferwallEnabled || isIronSourceOfferwallEnabled) {
            if (isTapjoyOfferwallEnabled) {
                if (Tapjoy.isConnected()) {
                    loadTapjoyOfferwall();
                }
            }
//            if (isIronSourceOfferwallEnabled) {
//                loadIronSourceOfferwall();
//            }
        }
    }
    public void showOfferwallFrom() {
        if (isTapjoyOfferwallEnabled) {
//        if (isTapjoyOfferwallEnabled || isIronSourceOfferwallEnabled) {
            if (tapjoyOfferwall.isContentAvailable() || IronSource.isOfferwallAvailable()) {

                if (tapjoyOfferwall.isContentReady()) {
                    showTapjoyOfferwall();
                } else {
                    showCustomToast(getString(R.string.main_act_please_wait_loading_offer)); }

//                if (IronSource.isOfferwallAvailable()) {
//                    showIronSourceOfferwall();
//                }
            } else {
                showCustomToast(getString(R.string.main_act_no_offer_available));
            }
        } else {
            showCustomToast(getString(R.string.main_act_no_offer_available));
        }
    }

    private void loadTapjoyOfferwall() {
        tapjoyOfferwall.requestContent();
    }
    private void showTapjoyOfferwall() {
        tapjoyOfferwall.showContent();
    }

    public class TJOfferwallListener implements TJPlacementListener {
        @Override
        public void onRequestSuccess(TJPlacement tjPlacement) {
        }
        @Override
        public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
        }
        @Override
        public void onContentReady(TJPlacement tjPlacement) {
        }
        @Override
        public void onContentShow(TJPlacement tjPlacement) {
        }
        @Override
        public void onContentDismiss(TJPlacement tjPlacement) {
//            Log.e("TJ Offerwall", " Closed");
            loadTapjoyOfferwall();
        }
        @Override
        public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {
        }
        @Override
        public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {
//            firebaseDataService.updateUserCoin(false, binding.dummyTextView, Long.parseLong(String.valueOf(i)));
        }
        @Override
        public void onClick(TJPlacement tjPlacement) {
        }
    }

    /**
     * ironSource Offerwall
     * **/

//    private void loadIronSourceOfferwall() {+
//        SupersonicConfig.getConfigObj().setClientSideCallbacks(false);
////        IronSource.setOfferwallListener(new IronSourceOfferwallListener());
////        IronSource.init(MainActivity.this, getString(R.string.iS_app_id),
////                new IronSourceInitListener(), IronSource.AD_UNIT.OFFERWALL);
//        IronSource.init(this, getString(R.string.iS_app_id), IronSource.AD_UNIT.OFFERWALL);
//        IronSource.setOfferwallListener(new IronSourceOfferwallListener());
////
////        IronSource.setOfferwallListener(this);
////        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
////        IronSource.addImpressionDataListener(this);
////
////        String advertisingId = IronSource.getAdvertiserId(MainActivity.this);
////        // set the IronSource user id
////        IronSource.setUserId(advertisingId);
////        // init the IronSource SDK
////        IronSource.init(this, APP_KEY);
////
////        IronSource.getAdvertiserId(this);
////        //Network Connectivity Status
////        IronSource.shouldTrackNetworkState(this, true);
//
//    }
//    private void showIronSourceOfferwall() {
//        IronSource.showOfferwall();
//    }
//
//    public class IronSourceOfferwallListener implements OfferwallListener {
//        @Override
//        public void onOfferwallAvailable(boolean isAvailable) {
////            Log.e("ironSource", "iS offerwall available");
////            IronSource.showOfferwall();
//        }
//        @Override
//        public void onOfferwallOpened() {
//        }
//        @Override
//        public void onOfferwallShowFailed(IronSourceError ironSourceError) {
////            Log.e("ironSource", "iS offerwall showFailed " + ironSourceError);
//        }
//        @Override
//        public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
////            Log.e("ironSource", "You've just earned " + credits);
////            Log.e("ironSource - ", "Your total earned - " + totalCredits);
//            firebaseDataService.updateUserCoin(false, binding.dummyTextView, Long.parseLong(String.valueOf(credits)));
////            return false;
//            return true;
//        }
//        @Override
//        public void onGetOfferwallCreditsFailed(IronSourceError ironSourceError) {
//        }
//        @Override
//        public void onOfferwallClosed() {
////            loadIronSourceOfferwall();
//        }
//    }
//    private class IronSourceInitListener implements InitializationListener {
//        @Override
//        public void onInitializationComplete() {
////            Log.e("ironSource", "iS Init successful");
//        }
//    }


//    public void showInfoHowToWin_bsDialog() {
//        BottomSheetDialog infoHowToWin_bsDialog = new BottomSheetDialog(this, R.style.bottomSheetDialog);
//        infoHowToWin_bsDialog.setContentView(R.layout.layout_bsdialog_info_how_to_win);
//        infoHowToWin_bsDialog.getDismissWithAnimation();
//        infoHowToWin_bsDialog.setCancelable(true);
//        infoHowToWin_bsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        infoHowToWin_bsDialog.show();
//
//        FrameLayout earnMoreBtn = infoHowToWin_bsDialog.findViewById(R.id.info_bsDialog_earn_more_btn_fL);
//
//        earnMoreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                infoHowToWin_bsDialog.dismiss();
//                viewPager_main.setCurrentItem(2);
//            }
//        });
//    }

/*****Checking Internet Connectivity****/
    public boolean internetConnected() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void showNoInternetDialog(View rootView1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        rootView1 = LayoutInflater.from(this).inflate(R.layout.layout_dialog_no_internet,
                (ConstraintLayout) rootView1.findViewById(R.id.constraint_dialog_no_internet));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        Button reloadActivityButton = rootView1.findViewById(R.id.reload_activity_button);
        Button exitActivityButton = rootView1.findViewById(R.id.exit_button);

        reloadActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                alertDialog.dismiss();
            }
        });

        exitActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (viewPager_main.getCurrentItem() != 0) {
            viewPager_main.setCurrentItem(0);
        } else {
            if (doubleBackToExit) {
                super.onBackPressed();
                overridePendingTransition(R.anim.anim_enter_from_left, R.anim.anim_exit_to_left);
                finishAffinity();
                System.exit(0);
            }

            doubleBackToExit = true;
            showCustomToast(getString(R.string.main_act_press_back_to_exit) + " " + getString(R.string.app_name));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExit = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    protected void onResume() {
        if (UserContext.getGoogleForceUpdate()) {
            handleImmediateUpdate();
        }

        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();
//        continuousListenerForFirebaseData();
        getNextWinnerAnnouncementTime();

//        if (currentTime >= getRefreshLeaderboardTime()) {
////            refreshLeaderboardData();
//        }

        Appodeal.show(this, Appodeal.BANNER_VIEW);
        IronSource.onResume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        if (tapdaqBanner != null) {
//            tapdaqBanner.destroy(this);
//        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Tapjoy.onActivityStart(MainActivity.this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(MainActivity.this);
        super.onStop();
    }
}