package com.o2games.playwin.android.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.sdk.AppLovinSdkUtils;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.google.android.gms.ads.AdSize;
import com.ironsource.mediationsdk.IronSource;
import com.o2games.playwin.android.AlarmService.AllGameAlarmReciever;
import com.o2games.playwin.android.App;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.TapdaqNativeLargeLayout;
import com.o2games.playwin.android.databinding.ActivityScratchBinding;
import com.o2games.playwin.android.scratchView.ScratchView;
import com.o2games.playwin.android.sqlUserGameData.DBHelper;
import com.o2games.playwin.android.userData.UserContext;
import com.tapjoy.Tapjoy;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ScratchActivity extends AppCompat {

    ConnectivityManager connectivityManager;
    private static final String TAG = ScratchActivity.class.getName();

    private final FirebaseDataService firebaseDataService = new FirebaseDataService(this);
    private static final String scratchGameId_enum = Game.NORMAL_SCRATCH.getId();
    private static final String sqlTotal_CashCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    private static int MAX_AMOUNT_OF_COIN = 25;  // 101
    private static int MIN_AMOUNT_OF_COIN = 5;  // 25

    int numberGamePLayed = 0;

//    private boolean isAdmobBannerEnabled;
//    private boolean isAdmobInterstitialEnabled;
//    private boolean isAdmobRewardedEnabled;
//    private String ADMOB_BANNER_AD_UNIT_ID;
//    private String ADMOB_INTERSTITIAL_AD_UNIT_ID;
//    private String ADMOB_REWARDED_AD_UNIT_ID;
//    private AdView admobBanner;
//    private InterstitialAd admobInterstitial;
//    private RewardedAd admobRewarded;
//
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
////    private TapdaqNativeLargeLayout tapdaqNativeLayout;

    private boolean isAppLovinBannerEnabled;
    private boolean isAppLovinInterstitialEnabled;
    private boolean isAppLovinRewardedEnabled;
    private boolean isAppLovinNativeEnabled;
    private static String APPLOVIN_BANNER_ID;
    private static String APPLOVIN_INTERSTITIAL_ID;
    private static String APPLOVIN_REWARDED_ID;
    private static String APPLOVIN_MEDIUM_NATIVE_ID;
    private MaxAdView applovinBanner;
    private MaxInterstitialAd applovinInterstitial;
    private MaxRewardedAd applovinRewarded;
    private MaxNativeAdLoader applovinNativeAdLoader;
    private MaxAd applovinNativeAd;
    private View maxNativeAdView_received;

    private boolean isAppodealBannerEnabled;
    private boolean isAppodealInterstitialEnabled;
    private boolean isAppodealRewardedEnabled;
    private boolean isAppodealNativeEnabled;

//    private boolean isUnityInterstitialEnabled;
//    private boolean isUnityInterstitialReady = false;
//    private boolean isUnityRewardedEnabled;
//    private boolean isUnityRewardedReady = false;
//    private static String UNITY_INTERSTITIAL_ID;
//    private static String UNITY_REWARDED_ID;
//    private com.unity3d.mediation.InterstitialAd unityInterstitial;
//    private com.unity3d.mediation.RewardedAd unityRewarded;

    ActivityScratchBinding binding;
    DBHelper dbHelper;
    Random random;
    long generateCoinsEarned;
    boolean giveDoubleReward = false;
    int randomForDoubleReward;

    SharedPreferences writeSPref;
    SharedPreferences readSPref;
    SharedPreferences.Editor editorSPref;

    Handler handler;
    String scratchChanceFromSQL;
    ScratchView scratchView;
    Animation scale_up;
    Animation scale_down;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);
        binding = ActivityScratchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        writeSPref = getSharedPreferences("o2_" + UserContext.getLoggedInUser().getId() + Constants.USER_SPECIFIC, Context.MODE_PRIVATE);
        editorSPref = writeSPref.edit();
        readSPref = getSharedPreferences("o2_" + UserContext.getLoggedInUser().getId() + Constants.USER_SPECIFIC, Context.MODE_PRIVATE);

        dbHelper = new DBHelper(ScratchActivity.this);
        handler = new Handler(Looper.getMainLooper());
        scale_up = AnimationUtils.loadAnimation(this, R.anim.anim_scale_up);
        scale_down = AnimationUtils.loadAnimation(this, R.anim.anim_scale_down);

        numberGamePLayed = getSPrefGamePlayed();
        checkAndStart_earnBtnTimer();
        chanceChecker();
        updateUIChance();
        updateUICoins();

        MAX_AMOUNT_OF_COIN = UserContext.getMaxInGameCoins();
        MIN_AMOUNT_OF_COIN = UserContext.getMinInGameCoins();

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
        isAppLovinNativeEnabled = UserContext.getIsAppLovin_mediumNative();
//        isUnityInterstitialEnabled = UserContext.getIsUnity_interstitial();
//        isUnityRewardedEnabled = UserContext.getIsUnity_rewarded();
        isAppodealBannerEnabled = UserContext.getIsAppodeal_banner();
        isAppodealInterstitialEnabled = UserContext.getIsAppodeal_interstitial();
        isAppodealRewardedEnabled = UserContext.getIsAppodeal_rewarded();
        isAppodealNativeEnabled = UserContext.getIsAppodeal_native();

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
        applovinRewarded = MaxRewardedAd.getInstance(APPLOVIN_REWARDED_ID, this);
        applovinRewarded.setListener(new AppLovinRewardedAdListener());
        APPLOVIN_MEDIUM_NATIVE_ID = getString(R.string.aL_native_default_medium);
        applovinNativeAdLoader = new MaxNativeAdLoader(APPLOVIN_MEDIUM_NATIVE_ID, this);
//        UNITY_INTERSTITIAL_ID = getString(R.string.uni_interstitial_default);
//        UNITY_REWARDED_ID = getString(R.string.uni_rewarded_default);

        showBannerAdFrom();
        loadInterstitialFrom();
        loadRewardedAdFrom();
//        loadNativeAdFrom();

        random = new Random();
        ScratchActivity.this.generateCoinsEarned = generateRandomCoins();
        binding.scratchGeneratedRandomNumber.setVisibility(View.INVISIBLE);
        binding.scratchGeneratedRandomNumber.setText(new StringBuffer().append("  + ").append(generateCoinsEarned).toString());
        binding.scratchGeneratedRandomNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_dollar, 0, 0, 0);

        binding.earnChance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(scratchChanceFromSQL) <= 0) {
                    numberGamePLayed = 0;
                    updateSharedPrefGamePlayed();
//                            disableEarnChanceBtn();   // shifted to rewarded
                    showRewardedFrom(true, false, 0);
                } else {
                    showCustomToast(getString(R.string.game_you_have_enough_chance));
                }
            }
        });

        scratchView = findViewById(R.id.scratch_view);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void beforeRevealed(ScratchView scratchView) {
                numberGamePLayed++;
                updateSharedPrefGamePlayed();
                binding.scratchGeneratedRandomNumber.setVisibility(View.INVISIBLE);
//                updateChanceLeftAndCoins(true, false, true, generateCoinsEarned);
//                updateScratchChanceLeftAndCoinsToSQL(true, coinsEarned);
//                updateUIChance();
            }

            @Override
            public void onRevealed(ScratchView scratchView) {
                scratchView.reveal();
                binding.scratchGeneratedRandomNumber.setVisibility(View.VISIBLE);
                showDialogAfterInterstitial(false, true, generateCoinsEarned);
//                showAddCoinsDialog(random_scratch_coins);
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent >= 0.50) {
//                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + percent);
                }
            }
        });


    }

    private void updateSharedPrefGamePlayed() {
        editorSPref.putInt(Constants.SP_NORMAL_SCRATCH_AUTO_INTERSTITIAL, numberGamePLayed);
        editorSPref.commit();
    }

    private int getSPrefGamePlayed() {
        int gamePlayedFromSPref = readSPref.getInt(Constants.SP_NORMAL_SCRATCH_AUTO_INTERSTITIAL, 0);
        return gamePlayedFromSPref;
    }

    private long getSPrefBtnTime() {
        long earnBtnTime = readSPref.getLong(Constants.SP_NORMAL_SCRATCH_EARN_BTN_TIME, 0);
        return earnBtnTime;
    }

    private long generateRandomCoins() {
        return random.nextInt(MAX_AMOUNT_OF_COIN - MIN_AMOUNT_OF_COIN) + MIN_AMOUNT_OF_COIN;
    }

    private int generateRandom() {
        int randomNo = random.nextInt(11 - 1) + 1;

        if (randomNo <= 4) {
            // less than or equal to 4
            giveDoubleReward = true;
        } else {
            // more than 4
            giveDoubleReward = false;
        }
        return randomNo;
    }

    private void updateUIChance() {
        binding.scratchChanceLeft.setText(firebaseDataService.getChanceAvailable(scratchGameId_enum));
//        binding.scratchChanceLeft.setText(UserContext.getAllGameInOneMapByGameId(scratchGameId_enum).getChanceLeft());
    }

    private void updateUICoins() {
        binding.scratchTotalCoins.setText(firebaseDataService.getCoinBalance());
//        binding.scratchTotalCoins.setText(UserContext.getAllGameInOneMapByGameId(sqlTotal_CashCoinsCOL).getCoins());
    }

    private void chanceChecker() {
        scratchChanceFromSQL = firebaseDataService.getChanceAvailable(scratchGameId_enum);
//                UserContext.getAllGameInOneMapByGameId(scratchGameId_enum).getChanceLeft();
        if (Integer.parseInt(scratchChanceFromSQL) <= 0) {
            binding.scratchViewChanceChecker.setVisibility(View.VISIBLE);

            binding.scratchViewChanceChecker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCustomToast(getString(R.string.game_watch_ad_to_get_chance));
                    binding.earnChance.startAnimation(scale_up);
                }
            });
        } else {
            binding.scratchViewChanceChecker.setVisibility(View.GONE);
        }
        if (!internetConnected(this)) {
            showNoInternetDialog();
        }
    }

    private void showCustomToast(String toastMessage) {
        LayoutInflater inflater = getLayoutInflater();
        View rootView1 = inflater.inflate(R.layout.layout_toast_custom,
                (ConstraintLayout) findViewById(R.id.custom_toast_constraint));

        TextView toastText = rootView1.findViewById(R.id.custom_toast_text);
        toastText.setText(toastMessage);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(rootView1);
        toast.show();
    }

//    private void updateScratchChanceAfterReward() {
//        AllGameInOne allGameInOne = UserContext.getAllGameInOneMapByGameId(scratchGameId_enum);
//        int chanceBeforeReward = Integer.parseInt(allGameInOne.getChanceLeft());
//        int chanceAfterReward = chanceBeforeReward + 16;
//        String finalChancesAfterReward = String.valueOf(chanceAfterReward);
//
//        allGameInOne.setChanceLeft(finalChancesAfterReward);
//        dbHelper.updateFreeAdGameChanceAndCoinsData(allGameInOne);
//        chanceChecker();
//    }

    private void updateChanceLeftAndCoins(boolean updateChance, boolean addChance, boolean updateCoins, long coinsEarned) {
        if (updateChance) {
            firebaseDataService.updateUserChance(scratchGameId_enum, addChance);
            chanceChecker();
            updateUIChance();
        }
        if (updateCoins) {
            firebaseDataService.updateUserCoin(true, binding.scratchTotalCoins, coinsEarned);
            updateUICoins();
        }
    }

//    private void updateScratchChanceLeftAndCoinsToSQL(boolean chanceToBeUpdated, long coinsEarned) {
//        AllGameInOne allGameInOne = UserContext.getAllGameInOneMapByGameId(scratchGameId_enum);
//
//        /** Updating chance after Spin **/
//        String finalChanceLeftAfterScratch = null;
//        if(chanceToBeUpdated) {
//            int chanceBeforeScratch = Integer.parseInt(allGameInOne.getChanceLeft());
//            int chanceAfterScratch = chanceBeforeScratch - 1;
//            finalChanceLeftAfterScratch = String.valueOf(chanceAfterScratch);
//        }
//        allGameInOne.setChanceLeft(finalChanceLeftAfterScratch != null ? finalChanceLeftAfterScratch : allGameInOne.getChanceLeft());
//        dbHelper.updateFreeAdGameChanceAndCoinsData(allGameInOne);
//
//
//        /** Updating coins after Spin **/
////        String finalCoinsAfterScratch = null;
////        if(coinsEarned > 0) {
////            long coinsBeforeScratch = Long.parseLong(allGameInOne.getCoins());
////            long coinsAfterScratch = coinsBeforeScratch + coinsEarned;
////            finalCoinsAfterScratch = String.valueOf(coinsAfterScratch);
////        }
////        allGameInOne.setCoins(finalCoinsAfterScratch != null ? finalCoinsAfterScratch : allGameInOne.getCoins());
//
//
//        AllGameInOne totalCoinsModel = UserContext.getAllGameInOneMapByGameId(sqlTotal_CashCoinsCOL);
//
//        /** Updating coins after Spin **/
//        long existingTotalCoins = Long.parseLong(UserContext.getAllGameInOneMapByGameId(sqlTotal_CashCoinsCOL).getCoins());
//        long finalTotalCoins = existingTotalCoins + coinsEarned;
//        totalCoinsModel.setCoins(String.valueOf(finalTotalCoins));
//        dbHelper.updateFreeAdGameChanceAndCoinsData(totalCoinsModel);
//    }

    private void showDialogAfterInterstitial(boolean chanceMsgToBeShown, boolean coinsMsgToBeShown, long coinsEarned) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScratchActivity.this);
        View dialog_rootLayout = LayoutInflater.from(ScratchActivity.this).inflate(R.layout.layout_dialog_add_reward_spin_flip_scratch,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_after_interstitial));
        builder.setView(dialog_rootLayout);
        builder.setCancelable(false);

        AlertDialog dialogAfterInterstitial = builder.create();
        dialogAfterInterstitial.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAfterInterstitial.show();

        TapdaqNativeLargeLayout tapdaqNativeLayout = dialog_rootLayout.findViewById(R.id.native_ad_container);
        tapdaqNativeLayout.setVisibility(View.GONE);
        FrameLayout applovinNativeFrameLayout = dialog_rootLayout.findViewById(R.id.native_ad_frameLayout);
        applovinNativeFrameLayout.setVisibility(View.GONE);

        TextView dialogTimerText = dialog_rootLayout.findViewById(R.id.dialog_after_interstitial_timer_text);
        TextView dialogEarnMessage = dialog_rootLayout.findViewById(R.id.dialog_after_interstitial_message);
        Button dialogDoubleRewardBtn = dialog_rootLayout.findViewById(R.id.dialog_after_interstitial_double_reward_btn);
        FrameLayout dialogClaimRewardBtn = dialog_rootLayout.findViewById(R.id.dialog_after_interstitial_claim_reward_btn);
        dialogDoubleRewardBtn.setTextColor(getResources().getColor(R.color.white));

        if (chanceMsgToBeShown) {
            dialogTimerText.setVisibility(View.GONE);
            dialogEarnMessage.setText(getString(R.string.dialog_after_interstitial_16_chances));
            dialogDoubleRewardBtn.setText(getString(R.string.dialog_after_interstitial_chance_add_button));

            dialogDoubleRewardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateChanceLeftAndCoins(true, true, false, 0);
//                    updateScratchChanceAfterReward();
                    chanceChecker();
                    updateUIChance();
                    dialogAfterInterstitial.dismiss();
                }
            });
        }

        if (coinsMsgToBeShown) {
            try {
                showNativeAdFrom(tapdaqNativeLayout, applovinNativeFrameLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialogEarnMessage.setText(new StringBuffer().append("  +").append(coinsEarned).toString());
            dialogEarnMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_dollar, 0, 0, 0);

            if (giveDoubleReward) {
                /** Give Double Reward **/
                dialogTimerText.setVisibility(View.GONE);
                dialogDoubleRewardBtn.setEnabled(false);
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        dialogDoubleRewardBtn.setText(new StringBuffer()
                                .append(getString(R.string.dialog_after_interstitial_getting_coins_ready))
                                .append(" ")
                                .append(millisUntilFinished / 1000).append("s").toString());
                    }

                    @Override
                    public void onFinish() {
                        dialogDoubleRewardBtn.setEnabled(true);
                        dialogClaimRewardBtn.setVisibility(View.VISIBLE);
                        dialogDoubleRewardBtn.setText(getString(R.string.dialog_after_interstitial_double_reward_btn_text));
                    }
                }.start();

            } else {
                /** NO Double Reward **/
                dialogDoubleRewardBtn.setVisibility(View.GONE);
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        dialogTimerText.setText(new StringBuffer().append(millisUntilFinished / 1000).toString());
                    }
                    @Override
                    public void onFinish() {
                        dialogTimerText.setVisibility(View.GONE);
                        dialogClaimRewardBtn.setVisibility(View.VISIBLE);
                    }
                }.start();


            }

            dialogClaimRewardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateChanceLeftAndCoins(true, false, true, coinsEarned);

                    scratchView.mask();
                    ScratchActivity.this.generateCoinsEarned = generateRandomCoins();
                    binding.scratchGeneratedRandomNumber.setText(new StringBuffer().append("  + ").append(generateCoinsEarned).toString());
                    binding.scratchGeneratedRandomNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_dollar, 0, 0, 0);

                    dialogAfterInterstitial.dismiss();

                    if (getSPrefGamePlayed() >= 1) {
                        if (isAppLovinNativeEnabled || isAppodealNativeEnabled) {
//                        if (isTapdaqNativeEnabled || isAppLovinNativeEnabled) {
//                            showProgressDialog();
                            loadNativeAdFrom();
                        }
                    }
                    if (getSPrefGamePlayed() >= 2) {
                        numberGamePLayed = 0; // shifted to showInterstitial
                        updateSharedPrefGamePlayed();
//                        showTimerInterstitial();
                        showInterstitialFrom();
                    }
                }
            });

            dialogDoubleRewardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRewardedFrom(false, true, coinsEarned);

                    scratchView.mask();
                    ScratchActivity.this.generateCoinsEarned = generateRandomCoins();
                    binding.scratchGeneratedRandomNumber.setText(new StringBuffer().append("  + ").append(generateCoinsEarned).toString());
                    binding.scratchGeneratedRandomNumber.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_dollar, 0, 0, 0);

                    dialogAfterInterstitial.dismiss();
                }
            });
        }

//        if (coinsMsgToBeShown) {
//            try {
//                showNativeAdFrom(tapdaqNativeLayout, applovinNativeFrameLayout);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            dialogAddButton.setEnabled(false);
//            new CountDownTimer(5000, 1000){
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    dialogAddButton.setText(new StringBuffer()
//                            .append(getString(R.string.dialog_after_interstitial_getting_coins_ready))
//                            .append(" ")
//                            .append(millisUntilFinished / 1000).append("s").toString());
//                }
//                @Override
//                public void onFinish() {
//                    dialogAddButton.setEnabled(true);
//                    dialogAddButton.setText(getString(R.string.dialog_after_interstitial_double_reward_btn_text));
//                }
//            }.start();
//
//            String msg1 = getString(R.string.dialog_after_interstitial_coins1);
//            String msg2 = getString(R.string.dialog_after_interstitial_coins2);
//            dialogEarnMessage.setText(new StringBuffer().append(msg1)
//                    .append(" ").append(coinsEarned)
//                    .append(" ").append(msg2).toString());
//
//            dialogAddButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    updateUICoins();
//                    scratchView.mask();
//                    ScratchActivity.this.generateCoinsEarned = generateRandomCoins();
//                    binding.scratchGeneratedRandomNumber.setText(new StringBuffer().append(ScratchActivity.this.generateCoinsEarned).append(" Coins").toString());
//
////                    loadTapdaqNativeAd();
//                    dialogAfterInterstitial.dismiss();
//                    chanceChecker();
//
//                    if (getSPrefGamePlayed() >= 1) {
//                        if (isTapdaqNativeEnabled || isAppLovinNativeEnabled) {
//                            showProgressDialog();
//                            loadNativeAdFrom();
//                        }
//                    }
//                    if (getSPrefGamePlayed() >= 2) {
//                        numberGamePLayed = 0; // shifted to showInterstitial
//                        updateSharedPrefGamePlayed();
////                        showTimerInterstitial();
//                        showInterstitialFrom();
//                    }
//                }
//            });
//        }
    }

    private void showDialogAfterDoubleReward(long coinsEarned) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog_rootLayout = LayoutInflater.from(this).inflate(R.layout.layout_dialog_add_double_reward_spin_flip_scratch,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_add_double_reward));
        builder.setView(dialog_rootLayout);
        builder.setCancelable(false);

        AlertDialog dialogAfterDoubleReward = builder.create();
        dialogAfterDoubleReward.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAfterDoubleReward.show();

        TextView dialogMsg = dialogAfterDoubleReward.findViewById(R.id.dialog_add_double_reward_message);
        FrameLayout dialogClaimRewardBtn = dialogAfterDoubleReward.findViewById(R.id.dialog_add_double_reward_claim_reward_btn);

        dialogMsg.setText(new StringBuffer().append("  +").append(coinsEarned * 2).toString());
        dialogMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_dollar, 0, 0, 0);

        dialogClaimRewardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateChanceLeftAndCoins(true, false, true, coinsEarned*2);
                dialogAfterDoubleReward.dismiss();
            }
        });
    }

//    private void showTimerInterstitial() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ScratchActivity.this);
//        View rootView1 = LayoutInflater.from(ScratchActivity.this).inflate(R.layout.layout_dialog_timer_interstitial,
//                (ConstraintLayout) findViewById(R.id.constraint_timer_interstitial_dialog));
//        builder.setView(rootView1);
//        builder.setCancelable(false);
//
//        AlertDialog dialogTimerInterstitial = builder.create();
//        dialogTimerInterstitial.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialogTimerInterstitial.show();
//
//        TextView timer_interstitial = rootView1.findViewById(R.id.dialog_interstitial_timer);
//
//        new CountDownTimer(3000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//                timer_interstitial.setText(new StringBuffer()
//                        .append(getString(R.string.dialog_interstitial_timer))
//                        .append(" ")
//                        .append(millisUntilFinished / 1000).toString());
//            }
//
//            @Override
//            public void onFinish() {
//                if (isTapdaqInterstitialEnabled || isAdmobInterstitialEnabled) {
//                    if (isTapdaqIntersVideoReady() || admobInterstitial != null) {
//
//                        dialogTimerInterstitial.dismiss();
//                        showInterstitialFrom();
//                    } else {
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (isTapdaqIntersVideoReady() || admobInterstitial != null) {
//                                    dialogTimerInterstitial.dismiss();
//                                    showInterstitialFrom();
//                                } else {
//                                    dialogTimerInterstitial.dismiss();
//                                    showCustomToast("Oops !!!.. No Ad available");
//                                }
//                            }
//                        }, 2500);
//                    }
//                } else {
//                    dialogTimerInterstitial.dismiss();
//                    showCustomToast("Oops !!!.. No Ad available");
//                }
//            }
//        }.start();
//    }
//
//    private void showLoadingInterstitial() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ScratchActivity.this);
//        View rootView1 = LayoutInflater.from(ScratchActivity.this).inflate(R.layout.layout_dialog_check_interstitial,
//                (ConstraintLayout) findViewById(R.id.constraint_dialog_check_interstitial));
//        builder.setView(rootView1);
//        builder.setCancelable(false);
//
//        AlertDialog dialogAfterInterstitial = builder.create();
//        dialogAfterInterstitial.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialogAfterInterstitial.show();
//
//        TextView check_reward_message = rootView1.findViewById(R.id.check_interstitial_dialog_message_tv);
//        Button close_button = rootView1.findViewById(R.id.check_interstitial_dialog_close_button);
//
//        close_button.setVisibility(View.GONE);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (admobInterstitial != null) {
//                    showAdmobChanceInterstitial();
//                    dialogAfterInterstitial.dismiss();
//                } else {
//                    close_button.setVisibility(View.VISIBLE);
//                    check_reward_message.setText("Slow Internet Connection !!! We are finding difficulty in showing Ad. Go back and click on watch ad again.");
//                }
//            }
//        }, 5000);
//
//        close_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogAfterInterstitial.dismiss();
//            }
//        });
//    }

    private void disableEarnChanceBtn() {
        Date dateTime = new Date(System.currentTimeMillis());
        long btnCurrentTime = dateTime.getTime();

        Calendar calFutureTime = Calendar.getInstance();
        calFutureTime.setTimeInMillis(btnCurrentTime);
        calFutureTime.add(Calendar.HOUR_OF_DAY, 3);
        calFutureTime.set(Calendar.SECOND, 0);
        long btnEnable_futureTime = calFutureTime.getTimeInMillis();

        editorSPref.putLong(Constants.SP_NORMAL_SCRATCH_EARN_BTN_TIME, btnEnable_futureTime);
        editorSPref.commit();

        setNotifAlarm(btnEnable_futureTime);
        checkAndStart_earnBtnTimer();
    }

    private void checkAndStart_earnBtnTimer() {
        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        long btnEnablingTime = getSPrefBtnTime();

        if (currentTime <= btnEnablingTime) {
            binding.earnChance.setEnabled(false);
            binding.earnChanceImageView.setVisibility(View.GONE);
            binding.earnChance.setCardBackgroundColor(getResources().getColor(R.color.darker_blue_app_theme));

            long timeLeft = btnEnablingTime - currentTime;
            new CountDownTimer(timeLeft, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;

                    long elapsedHours = millisUntilFinished / hoursInMilli;
                    millisUntilFinished = millisUntilFinished % hoursInMilli;

                    long elapsedMinutes = millisUntilFinished / minutesInMilli;
                    millisUntilFinished = millisUntilFinished % minutesInMilli;

                    long elapsedSeconds = millisUntilFinished / secondsInMilli;

                    binding.earnChanceTextView.setText(new StringBuffer()
                            .append(elapsedHours).append("h : ")
                            .append(elapsedMinutes).append("m : ")
                            .append(elapsedSeconds).append("s").toString());
                }

                @Override
                public void onFinish() {
                    binding.earnChance.setEnabled(true);
                    binding.earnChanceImageView.setVisibility(View.VISIBLE);
                    binding.earnChanceTextView.setText(getString(R.string.scratch_get_free_scratch));
                    binding.earnChance.setCardBackgroundColor(getResources().getColor(R.color.blue_app_theme));

                    sendFreeChanceNotif();
                }
            }.start();
        }
        else {
            binding.earnChance.setEnabled(true);
            binding.earnChanceImageView.setVisibility(View.VISIBLE);
            binding.earnChanceTextView.setText(getString(R.string.scratch_get_free_scratch));
            binding.earnChance.setCardBackgroundColor(getResources().getColor(R.color.blue_app_theme));
        }
    }






    /**
     * * Native Ad Code
     * **/
    private void loadNativeAdFrom() {
//        if (isTapdaqNativeEnabled || isAppLovinNativeEnabled) {
//            if (isTapdaqNativeEnabled) {
//                loadTapdaqNativeAd();
//            }
//            if (isAppLovinNativeEnabled) {
//                loadAppLovinNativeAd();
//            }
//        }
    }

    private void showNativeAdFrom(TapdaqNativeLargeLayout tapdaqNativeLayout, FrameLayout applovinNativeFrameLayout) {
//        if (isTapdaqNativeEnabled || isAppLovinNativeEnabled) {
//            if (isTapdaqNativeEnabled) {
//                if (isTapdaqNativeReady) {
//                    tapdaqNativeLayout.clear();
//                    tapdaqNativeLayout.setVisibility(View.VISIBLE);
//                    tapdaqNativeLayout.populate(tapdaqNativeAd);
//                } else {
//                    tapdaqNativeLayout.clear();
//                    tapdaqNativeLayout.setVisibility(View.GONE);
//                }
//            }
//            if (isAppLovinNativeEnabled) {
//                if (applovinNativeAd != null) {
//                    applovinNativeFrameLayout.removeAllViews();
//                    applovinNativeFrameLayout.setVisibility(View.VISIBLE);
//                    applovinNativeFrameLayout.addView(maxNativeAdView_received);
//                } else {
//                    applovinNativeFrameLayout.removeAllViews();
//                    applovinNativeFrameLayout.setVisibility(View.GONE);
//                }
//            }
//        }
    }

    /**
     * Tapdaq Native
     * **/
//    private void loadTapdaqNativeAd() {
//        TDMediatedNativeAdOptions options = new TDMediatedNativeAdOptions();
//        Tapdaq.getInstance().loadMediatedNativeAd(ScratchActivity.this, NATIVE_AD_UNIT_ID, options, new TMAdListener() {
//            @Override
//            public void didLoad(TDMediatedNativeAd ad) {
//                super.didLoad(ad);
//                progressDialog.dismiss();
//                tapdaqNativeAd = ad;
//                isTapdaqNativeReady = true;
//            }
//
//            @Override
//            public void didFailToLoad(TMAdError error) {
//                super.didFailToLoad(error);
//                progressDialog.dismiss();
//                tapdaqNativeAd = null;
//                isTapdaqNativeReady = false;
//            }
//        });
//    }
//
//    /**
//     * AppLovin Native
//     * **/
//    private void loadAppLovinNativeAd() {
//        applovinNativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
//            @Override
//            public void onNativeAdLoaded(MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
//                super.onNativeAdLoaded(maxNativeAdView, maxAd);
//                progressDialog.dismiss();
//                if (applovinNativeAd != null) {
//                    applovinNativeAdLoader.destroy(applovinNativeAd);
//                }
//                applovinNativeAd = maxAd;
//                maxNativeAdView_received = maxNativeAdView;
//            }
//            @Override
//            public void onNativeAdLoadFailed(String s, MaxError maxError) {
//                super.onNativeAdLoadFailed(s, maxError);
//                progressDialog.dismiss();
//            }
//            @Override
//            public void onNativeAdClicked(MaxAd maxAd) {
//                super.onNativeAdClicked(maxAd);
//            }
//        });
//        applovinNativeAdLoader.loadAd();
//    }


    /**
     * * Banner Ad Code
     * **/
    private void showBannerAdFrom() {
//        if (isTapdaqBannerEnabled || isAdmobBannerEnabled || isAppLovinBannerEnabled || isAppodealBannerEnabled) {
        if (isAppLovinBannerEnabled || isAppodealBannerEnabled) {
//            if (isTapdaqBannerEnabled) {
//                tapdaqBanner = new TMBannerAdView(this);
//                binding.normalFlipAdmobAdView.addView(tapdaqBanner);
//                tapdaqBanner.load(this, TAPDAQ_BANNER_ID, TMBannerAdSizes.STANDARD, new TMAdListener());
//            }
//
//            if (isAdmobBannerEnabled) {
//                admobBanner = new AdView(this);
//                admobBanner.setAdUnitId(ADMOB_BANNER_AD_UNIT_ID);
//                binding.normalFlipAdmobAdView.addView(admobBanner);
//
//                AdRequest adRequest = new AdRequest.Builder().build();
//                AdSize adSize = getAdSize();
//                admobBanner.setAdSize(adSize);
//                admobBanner.loadAd(adRequest);
//            }

            if (isAppLovinBannerEnabled) {
                applovinBanner = new MaxAdView(APPLOVIN_BANNER_ID, ScratchActivity.this);

                // Stretch to the width of the screen for banners to be fully functional
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                // Get the adaptive banner height.
                int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(ScratchActivity.this).getHeight();
                int heightPx = AppLovinSdkUtils.dpToPx(ScratchActivity.this, heightDp);
                applovinBanner.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                applovinBanner.setExtraParameter("adaptive_banner", "true");
                // Set background or background color for banners to be fully functional
                applovinBanner.setBackgroundColor(getResources().getColor(R.color.darker_blue_app_theme));
                // Load the ad
                binding.scratchAdmobAdView.addView(applovinBanner);
                applovinBanner.loadAd();
            }

            if (isAppodealBannerEnabled) {
                binding.scratchAdmobAdView.setVisibility(View.GONE);
                Appodeal.setBannerViewId(R.id.scratch_appodealBannerView);
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


    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScratchActivity.this);
        View rootView1 = LayoutInflater.from(ScratchActivity.this).inflate(R.layout.layout_dialog_progress_bar_white_short,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_progress));
        builder.setView(rootView1);
        builder.setCancelable(false);

        progressDialog = builder.create();
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }


    /**
     * * * Rewarded Ad Code
     **/
    private void loadRewardedAdFrom() {
//        if (isAdmobRewardedEnabled || isTapdaqRewardedEnabled || isAppLovinRewardedEnabled || isAppodealRewardedEnabled || isUnityRewardedEnabled) {
        if (isAppLovinRewardedEnabled || isAppodealRewardedEnabled) {
            if (isAppLovinRewardedReady() || Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
//            if (admobRewarded != null || isTapdaqRewardedReady() || isAppLovinRewardedReady() || Appodeal.isLoaded(Appodeal.REWARDED_VIDEO) || isUnityRewardedReady) {
                // Do not load Ad again
            } else {
//                if (isAdmobRewardedEnabled) {
//                    loadingAdmobRewarded();
//                }
//                if (isTapdaqRewardedEnabled) {
//                    loadTapdaqRewarded();
//                }
                if (isAppLovinRewardedEnabled) {
                    loadAppLovinRewarded();
                }
                if (isAppodealRewardedEnabled) {
                    loadAppodealRewarded();
                }
//                if (isUnityRewardedEnabled) {
//                    loadUnityRewarded();
//                }
            }
        }
    }

    private void showRewardedFrom(boolean rewardForChance, boolean giveDoubleReward, long coinsEarned) {
//        if (isAdmobRewardedEnabled || isTapdaqRewardedEnabled || isAppLovinRewardedEnabled || isAppodealRewardedEnabled || isUnityRewardedEnabled) {
        if (isAppLovinRewardedEnabled || isAppodealRewardedEnabled) {
            if (isAppLovinRewardedReady() || Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
//            if (admobRewarded != null || isTapdaqRewardedReady() || isAppLovinRewardedReady() || isUnityRewardedReady) {

                numberGamePLayed = 0;
                updateSharedPrefGamePlayed();

//                if (admobRewarded != null) {
//                    show_AdmobRewarded();
//                }
//                if (isTapdaqRewardedReady()) {
//                    show_TapdaqRewarded();
//                }
                if (isAppLovinRewardedReady()) {
                    show_AppLovinRewarded(rewardForChance, giveDoubleReward, coinsEarned);
                }
                if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
                    showAppodealRewarded(rewardForChance, giveDoubleReward, coinsEarned);
                }
//                if (isUnityRewardedReady) {
//                    show_UnityRewarded(rewardForChance, giveDoubleReward, coinsEarned);
//                }
            } else {
                // None of the above ad is ready
//                giveChancesForFree();
                showCustomToast(getString(R.string.game_no_ad_available));
                if (rewardForChance) {
                    // Don't give free chance
                }
                if (giveDoubleReward) {
                    // only give previous earned coins
                    updateChanceLeftAndCoins(true, false, true, coinsEarned);
                }
            }
        } else {
            // None of the above ad network is enabled
            if (rewardForChance) {
                disableEarnChanceBtn();
                giveChancesForFree();
            }
            if (giveDoubleReward) {
                updateChanceLeftAndCoins(true, false, true, coinsEarned);
            }
        }
    }

    private void giveChancesForFree() {
        showProgressDialog();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                numberGamePLayed = 0;
                updateSharedPrefGamePlayed();
                showCustomToast(getString(R.string.game_no_ad_available));
                progressDialog.dismiss();
//                disableEarnChanceBtn();
                showDialogAfterInterstitial(true, false, 0);
            }
        }, 3000);
    }

    /**
     * Tapdaq Rewarded
     **/
//    private void loadTapdaqRewarded() {
//        Tapdaq.getInstance().loadRewardedVideo(ScratchActivity.this, TAPDAQ_REWARDED_ID, new TapdaqRewardedListener());
//    }
//
//    private boolean isTapdaqRewardedReady() {
//        boolean isReady = Tapdaq.getInstance().isRewardedVideoReady(ScratchActivity.this, TAPDAQ_REWARDED_ID);
//        return isReady;
//    }
//
//    private void show_TapdaqRewarded() {
//        Tapdaq.getInstance().showRewardedVideo(ScratchActivity.this, TAPDAQ_REWARDED_ID, new TapdaqRewardedListener());
//    }
//
//    private class TapdaqRewardedListener extends TMAdListener {
//        @Override
//        public void didLoad() {
//            super.didLoad();
//        }
//
//        @Override
//        public void didFailToLoad(TMAdError error) {
//            super.didFailToLoad(error);
//        }
//
//        @Override
//        public void didClose() {
//            super.didClose();
//            loadTapdaqRewarded();
//        }
//
//        @Override
//        public void didVerify(TDReward reward) {
//            super.didVerify(reward);
//            showDialogAfterInterstitial(true, false, 0);
//        }
//    }
//
//    /**
//     * * Admob Rewarded
//     **/
//    private void loadingAdmobRewarded() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        RewardedAd.load(this, ADMOB_REWARDED_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                admobRewarded = null;
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                admobRewarded = rewardedAd;
//                progressDialog.dismiss();
//            }
//        });
//    }
//
//    private void show_AdmobRewarded() {
//        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
//            @Override
//            public void onAdShowedFullScreenContent() {
//            }
//
//            @Override
//            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
////                Log.d(TAG, "onAdFailedToShowFullScreenContent");
//                admobRewarded = null;
//            }
//
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                admobRewarded = null;
//                loadingAdmobRewarded();
//            }
//        };
//        admobRewarded.setFullScreenContentCallback(fullScreenContentCallback);
//
//        Activity activityContext = ScratchActivity.this;
//        admobRewarded.show(activityContext, new OnUserEarnedRewardListener() {
//            @Override
//            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                int rewardAmount = rewardItem.getAmount();
//                String rewardType = rewardItem.getType();
//
////                if (rewardForChance) {
////                    showDialogAfterInterstitial(true, false, 0);
////                }
////                if (giveDoubleReward) {
////                    showDialogAfterDoubleReward(coinsEarned);
////                }
//            }
//        });
//    }

    /**
     * AppLovin Rewarded
     **/
    private void loadAppLovinRewarded() {
//         Initialized in onCreate() method of this class
//        applovinInterstitial = new MaxInterstitialAd(APPLOVIN_INTERSTITIAL_ID, this);
//        applovinRewarded.setListener(new AppLovinRewardedAdListener());
        applovinRewarded.loadAd();
    }

    private boolean isAppLovinRewardedReady() {
        // Initialize MaxInterstitialAd in onCreate() method before checking .isReady()
        return applovinRewarded.isReady();
    }

    private void show_AppLovinRewarded(boolean rewardForChance, boolean giveDoubleReward, long coinsEarned) {
        // listener already attached in load() method
        applovinRewarded.setListener(new MaxRewardedAdListener() {
            @Override
            public void onRewardedVideoStarted(MaxAd ad) {
            }
            @Override
            public void onRewardedVideoCompleted(MaxAd ad) {
            }
            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                if (rewardForChance) {
                    disableEarnChanceBtn();
                    showDialogAfterInterstitial(true, false, 0);
                }
                if (giveDoubleReward) {
                    showDialogAfterDoubleReward(coinsEarned);
                }
            }
            @Override
            public void onAdLoaded(MaxAd ad) {
            }
            @Override
            public void onAdDisplayed(MaxAd ad) {
            }
            @Override
            public void onAdHidden(MaxAd ad) {
                loadAppLovinRewarded();
            }
            @Override
            public void onAdClicked(MaxAd ad) {
            }
            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
            }
            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
            }
        });
        applovinRewarded.showAd();
    }

    private class AppLovinRewardedAdListener implements MaxRewardedAdListener {
        @Override
        public void onAdLoaded(MaxAd ad) {
//            Log.e("Flip activity - ", "aL REWARDED ad loaded = ");
        }
        @Override
        public void onAdDisplayed(MaxAd ad) {
        }
        @Override
        public void onAdHidden(MaxAd ad) {
            // Ad closed, pre-load next ad
            loadAppLovinRewarded();
        }
        @Override
        public void onAdClicked(MaxAd ad) {
        }
        @Override
        public void onAdLoadFailed(String adUnitId, MaxError error) {
//            Log.e("Flip activity - ", "aL REWARDED ad Failed to load = " + error.getMessage());
        }
        @Override
        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        }
        @Override
        public void onRewardedVideoStarted(MaxAd ad) {
        }
        @Override
        public void onRewardedVideoCompleted(MaxAd ad) {
        }
        @Override
        public void onUserRewarded(MaxAd ad, MaxReward reward) {
            showDialogAfterInterstitial(true, false, 0);
        }
    }

    /**
     * Appodeal Rewarded
     **/
    private void loadAppodealRewarded() {
        Appodeal.cache(this, Appodeal.REWARDED_VIDEO);
    }
    private void showAppodealRewarded(boolean rewardForChance, boolean giveDoubleReward, long coinsEarned) {
        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded(boolean isPrecache) {
            }
            @Override
            public void onRewardedVideoFailedToLoad() {
            }
            @Override
            public void onRewardedVideoShown() {
            }
            @Override
            public void onRewardedVideoShowFailed() {
            }
            @Override
            public void onRewardedVideoClicked() {
            }
            @Override
            public void onRewardedVideoFinished(double amount, String name) {
                // Called when rewarded video is viewed until the end
                if (rewardForChance) {
                    disableEarnChanceBtn();
                    showDialogAfterInterstitial(true, false, 0);
                }
                if (giveDoubleReward) {
                    showDialogAfterDoubleReward(coinsEarned);
                }
            }
            @Override
            public void onRewardedVideoClosed(boolean finished) {
                loadAppodealRewarded();
            }
            @Override
            public void onRewardedVideoExpired() {
            }
        });

        Appodeal.show(this, Appodeal.REWARDED_VIDEO);
    }

//    /**
//     * Unity Rewarded
//     **/
//    private void loadUnityRewarded() {
//        unityRewarded = new com.unity3d.mediation.RewardedAd(this, UNITY_REWARDED_ID);
//        IRewardedAdLoadListener unityRewardedLoadListener = new IRewardedAdLoadListener() {
//            @Override
//            public void onRewardedLoaded(com.unity3d.mediation.RewardedAd rewardedAd) {
//                isUnityRewardedReady = true;
//            }
//            @Override
//            public void onRewardedFailedLoad(com.unity3d.mediation.RewardedAd rewardedAd, LoadError loadError, String s) {
//                isUnityRewardedReady = false;
//            }
//        };
//        unityRewarded.load(unityRewardedLoadListener);
//    }
//
//    private void show_UnityRewarded(boolean rewardForChance, boolean giveDoubleReward, long coinsEarned) {
//        IRewardedAdShowListener unityRewardedShowListener = new IRewardedAdShowListener() {
//            @Override
//            public void onRewardedShowed(com.unity3d.mediation.RewardedAd rewardedAd) {
//                isUnityRewardedReady = false;
//            }
//            @Override
//            public void onRewardedClicked(com.unity3d.mediation.RewardedAd rewardedAd) {
//            }
//            @Override
//            public void onRewardedClosed(com.unity3d.mediation.RewardedAd rewardedAd) {
//                isUnityRewardedReady = false;
//                loadUnityRewarded();
//            }
//            @Override
//            public void onRewardedFailedShow(com.unity3d.mediation.RewardedAd rewardedAd, ShowError showError, String s) {
//                isUnityRewardedReady = false;
//            }
//            @Override
//            public void onUserRewarded(com.unity3d.mediation.RewardedAd rewardedAd, IReward iReward) {
//                isUnityRewardedReady = false;
//                if (rewardForChance) {
//                    disableEarnChanceBtn();
//                    showDialogAfterInterstitial(true, false, 0);
//                }
//                if (giveDoubleReward) {
//                    showDialogAfterDoubleReward(coinsEarned);
//                }
//            }
//        };
//        unityRewarded.show(unityRewardedShowListener);
//    }



    /**
     * * * * * Interstitial
     ***/
    private void loadInterstitialFrom() {
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

    private void showInterstitialFrom() {
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
//        showProgressDialog();
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
//            progressDialog.dismiss();
//        }
//
//        @Override
//        public void didFailToLoad(TMAdError error) {
//            super.didFailToLoad(error);
//            progressDialog.dismiss();
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
//        showProgressDialog();
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(this, ADMOB_INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                admobInterstitial = interstitialAd;
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                admobInterstitial = null;
//                progressDialog.dismiss();
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
        showProgressDialog();
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
            progressDialog.dismiss();
        }
        @Override
        public void onAdDisplayed(MaxAd ad) {
        }
        @Override
        public void onAdHidden(MaxAd ad) {
            // Ad closed, pre-load next ad
            loadAppLovinInterstitial();
        }
        @Override
        public void onAdClicked(MaxAd ad) {
        }
        @Override
        public void onAdLoadFailed(String adUnitId, MaxError error) {
            progressDialog.dismiss();
        }
        @Override
        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        }
    }

//    /**
//     * Unity Intertitial
//     **/
//    private void loadUnityInterstitial() {
//        showProgressDialog();
//        unityInterstitial = new com.unity3d.mediation.InterstitialAd(this, UNITY_INTERSTITIAL_ID);
//        isUnityInterstitialReady = false;
//        IInterstitialAdLoadListener unityIntersLoadListener = new IInterstitialAdLoadListener() {
//            @Override
//            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
//                progressDialog.dismiss();
//                isUnityInterstitialReady = true;
//            }
//            @Override
//            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
//                progressDialog.dismiss();
//                isUnityInterstitialReady = false;
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
//            }
//        };
//        unityInterstitial.show(unityIntersShowListener);
//    }






//    @Override
//    public void onBackPressed() {
////        startActivity(new Intent(ScratchActivity.this, MainActivity.class));
//    }



    /*****Checking Internet Connectivity****/
    private boolean internetConnected(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog_rootView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_no_internet,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_no_internet));
        builder.setView(dialog_rootView);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        Button reloadActivityButton = dialog_rootView.findViewById(R.id.reload_activity_button);
        Button exitActivityButton = dialog_rootView.findViewById(R.id.exit_button);

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
                finishAffinity();
                System.exit(0);
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public void sendFreeChanceNotif() {
        String title = getString(R.string.notif_free_chance_title);
        String message = getString(R.string.notif_free_chance_message);

        Intent notificationIntent = new Intent(this, ScratchActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_FREE_CHANCE)
                .setSmallIcon(R.drawable.ic_app_notif_icon_cartoon_g1_svg)
                .setColor(getResources().getColor(R.color.system_accent3_700))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVibrate(new long[] {1000, 1000})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }

    private void setNotifAlarm(long btnEnable_futureTime) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AllGameAlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Constants.FREE_CHANCE_NOR_SCRATCH_REQUEST_CODE, intent, 0);

//        if (btnEnable_futureTime.before(Calendar.getInstance())) {
//            btnEnable_futureTime.add(Calendar.DATE, 1);
//        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, btnEnable_futureTime, pendingIntent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Appodeal.show(this, Appodeal.BANNER_VIEW);
//        IronSource.onResume(this);
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
        Tapjoy.onActivityStart(ScratchActivity.this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(ScratchActivity.this);
        super.onStop();
    }

}