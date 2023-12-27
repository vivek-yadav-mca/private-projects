package com.o2games.playwin.android.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.applovin.sdk.AppLovinPrivacySettings;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkSettings;
import com.appodeal.ads.Appodeal;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.databinding.ActivitySplashScreenBinding;
import com.o2games.playwin.android.model.AllGameInOne;
import com.o2games.playwin.android.model.SpUserModel;
import com.o2games.playwin.android.model.User;
import com.o2games.playwin.android.spinWheel.WheelItem;
import com.o2games.playwin.android.sqlUserGameData.DBHelper;
import com.o2games.playwin.android.userData.UserContext;
import com.tapjoy.TJConnectListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompat {

    SplashScreenActivity splashScreenActivity = SplashScreenActivity.this;
    private static final String TAG = "water_water";
    private FirebaseDataService firebaseDataService;
    private static final String sqlTotal_CashCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    String googleAdverId;
    boolean connectedToAdNetwork;

    FirebaseRemoteConfig remoteConfig;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseRef;
    GoogleSignInAccount lastSignedGoogleAccount;

    ActivitySplashScreenBinding binding;
    DBHelper dbHelper = new DBHelper(this);
    Handler handler = new Handler(Looper.getMainLooper());
    Timer progressTimer;
    int progressInt = 0;

    List<WheelItem> data;

    ConnectivityManager connectivityManager;
    AlertDialog underMaintenanceDialog;
    AlertDialog progressDialog;

    SharedPreferences writeSPref;
    SharedPreferences.Editor editorSPref;
    SharedPreferences readSPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDataService = new FirebaseDataService(this);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        lastSignedGoogleAccount = GoogleSignIn.getLastSignedInAccount(this);
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        writeSPref = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
        editorSPref = writeSPref.edit();
        readSPref = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);

        progressTimer = new Timer();

        gettingAdvertisingId();

        if (!internetConnected(SplashScreenActivity.this)) {
            showNoInternetDialog();
        } else {
            remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Boolean> task) {
                    if (task.isSuccessful()) {
                        boolean is_app_under_maintenance = remoteConfig.getBoolean(Constants.IS_APP_UNDER_MAINTENANCE);
                        if (is_app_under_maintenance) {
                            long under_maintenance_closing_time = remoteConfig.getLong(Constants.UNDER_MAINTENANCE_CLOSING_TIME);
                            String under_maintenance_dialog_title = remoteConfig.getString(Constants.UNDER_MAINTENANCE_TITLE);
                            String under_maintenance_dialog_msg = remoteConfig.getString(Constants.UNDER_MAINTENANCE_MSG);

                            showUnderMaintenanceDialog(under_maintenance_closing_time, under_maintenance_dialog_title, under_maintenance_dialog_msg);
                        } else {
                            // under maintenance is false
                            progressTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressInt = progressInt + 1;
                                    binding.splashProgressBar.setProgress(progressInt);

                                    splashScreenActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.splashProgressPercent.setText(progressInt + "%");

                                            if (binding.splashProgressBar.getProgress() >= 100) {
                                                progressTimer.cancel();
                                                if (lastSignedGoogleAccount != null) {
                                                    showCustomProgressDialog(getString(R.string.signup_verifying_account), false);
                                                    registerUserAndNavigate(lastSignedGoogleAccount);
                                                } else {
//                                                    checkAndInitializeSDK();
                                                    binding.splashContinueButtonFrameL.setVisibility(View.VISIBLE);
//                                                    binding.splashContinueButton.setVisibility(View.VISIBLE);
                                                    binding.splashProgressCircle.setVisibility(View.GONE);
                                                    binding.splashProgressPercent.setVisibility(View.GONE);
                                                    binding.splashProgressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    });
                                }
                            }, 800, 20);
                        }
                    }
                }
            });
        }

        binding.splashContinueButtonFrameL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this, SignupActivity.class));
            }
        });


    }

    private void showCustomProgressDialog(String dialog_msg, boolean showCloseBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View getLayout_rootView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_progress_bar_white_long,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_progress_bar_black));
        builder.setView(getLayout_rootView);
        builder.setCancelable(false);

        progressDialog = builder.create();
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();

        TextView progressDialog_msg = getLayout_rootView.findViewById(R.id.dialog_progressBar_black_msgText);
        progressDialog_msg.setText(dialog_msg);

        ProgressBar progressDialog_progressCircle = getLayout_rootView.findViewById(R.id.dialog_progressBar_black_progressCircle);
        ImageView progressDialog_closeBtn = getLayout_rootView.findViewById(R.id.dialog_progressBar_black_closeBtn);
        if (showCloseBtn) {
            progressDialog_closeBtn.setVisibility(View.VISIBLE);
            progressDialog_progressCircle.setVisibility(View.GONE);
        } else {
            progressDialog_closeBtn.setVisibility(View.GONE);
            progressDialog_progressCircle.setVisibility(View.VISIBLE);
        }

        progressDialog_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showMainAct_interstitialFrom();
            }
        });
    }

    private void gettingAdvertisingId() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String advertId = null;
                try {
                    advertId = idInfo.getId();
                    googleAdverId = idInfo.getId();
                    UserContext.setUserAdverId(googleAdverId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
//                Toast.makeText(getApplicationContext(), advertId, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        View view = LayoutInflater.from(SplashScreenActivity.this).inflate(R.layout.layout_dialog_no_internet,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_no_internet));
        builder.setView(view);

        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        Button reloadActivityButton = view.findViewById(R.id.reload_activity_button);
        Button exitActivityButton = view.findViewById(R.id.exit_button);

        reloadActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        exitActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    /*****Checking Internet Connectivity****/
    private boolean internetConnected(SplashScreenActivity mainActivity) {
        connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    /***** Under Maintenance Code Starts *****/
    private void showUnderMaintenanceDialog(long maintenanceClosingTime, String under_maintenance_dialog_title, String under_maintenance_dialog_msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootView1 = LayoutInflater.from(this).inflate(R.layout.layout_dialog_maintenance,
                (ConstraintLayout) findViewById(R.id.constraint_dialog_maintenance));
        builder.setView(rootView1);
        builder.setCancelable(false);

        underMaintenanceDialog = builder.create();

        underMaintenanceDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        underMaintenanceDialog.show();

        TextView dialogTitle = rootView1.findViewById(R.id.dialog_maintenance_title);
        TextView dialogMessage = rootView1.findViewById(R.id.dialog_maintenance_message);
        TextView dialogClosingTimer_text = rootView1.findViewById(R.id.dialog_maintenance_closing_timer);
        Button dialogExitButton = rootView1.findViewById(R.id.dialog_maintenance_exit_button);
        Button dialogRefreshButton = rootView1.findViewById(R.id.dialog_maintenance_refresh_button);

        dialogTitle.setText(under_maintenance_dialog_title);
        dialogMessage.setText(under_maintenance_dialog_msg);

        maintenanceClosingTimer(dialogMessage, dialogClosingTimer_text, maintenanceClosingTime, dialogExitButton, dialogRefreshButton);

        dialogExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                underMaintenanceDialog.dismiss();
                finishAffinity();
                System.exit(0);
            }
        });
        dialogRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        if (!SplashScreenActivity.this.isFinishing()) {
            underMaintenanceDialog.show();
        }
    }

    private void maintenanceClosingTimer(TextView dialogMessage, TextView dialogClosingTimer, long maintenanceClosingTime, Button dialogExitButton, Button dialogRefreshButton) {
        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        long leftTime = (maintenanceClosingTime - currentTime);

        if (maintenanceClosingTime <= currentTime) {
            dialogMessage.setText(getString(R.string.dialog_maintenance_msg_2));
            dialogExitButton.setVisibility(View.GONE);
            dialogRefreshButton.setVisibility(View.VISIBLE);
            dialogClosingTimer.setText("0d : 00h : 00m : 00s");
        } else {
            dialogExitButton.setVisibility(View.VISIBLE);
            dialogRefreshButton.setVisibility(View.GONE);

            new CountDownTimer(leftTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    int seconds = (int) millisUntilFinished / 1000 % 60;
                    int minutes = (int) millisUntilFinished / 1000 / 60 % 60;
                    int hours = (int) millisUntilFinished / 1000 / 60 / 60 % 24;
                    int days = (int) millisUntilFinished / 1000 / 60 / 60 / 24 % 30;

                    String formattedTime = String.format(Locale.getDefault(), "%02dd  %02dh  %02dm  %02ds", days, hours, minutes, seconds);
                    dialogClosingTimer.setText(formattedTime);
                }

                @Override
                public void onFinish() {
                    dialogMessage.setText(getString(R.string.dialog_maintenance_msg_2));
                    dialogExitButton.setVisibility(View.GONE);
                    dialogRefreshButton.setVisibility(View.VISIBLE);
                    dialogClosingTimer.setText("0d : 00h : 00m : 00s");
                }
            }.start();
        }
    }

    private void dismissUnderMaintenanceDialog() {
        if (underMaintenanceDialog != null && underMaintenanceDialog.isShowing()) {
            underMaintenanceDialog.dismiss();
        }
    }

    /***** Under Maintenance Code Ends *****/

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

    public void registerUserAndNavigate(GoogleSignInAccount account) {
        String userId = account.getEmail().replaceAll("[.#$\\[\\]]", "");
        String userName = account.getDisplayName();
        String userEmail = account.getEmail();
        String userGoogleId = account.getId();
        String userPhotoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;
        String authUid = firebaseAuth.getCurrentUser().getUid();

        User loggedInUser = new User(userId, userName, userEmail, userGoogleId, userPhotoUrl, googleAdverId, authUid);
        UserContext.setLoggedInUser(loggedInUser);

//        UserContext.setDbHelper(dbHelper);
//        SharedPreferences readSPref_forUserData = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
//        UserContext.setReadSPref_userData(readSPref_forUserData);
//   **** UserContext start conflicting after obfuscation.

        setUserData_sPref(loggedInUser);
        createSQLDatabaseTable(loggedInUser);

        checkAndInitializeSDK(loggedInUser);
    }

    private void setUserData_sPref(User loggedInUser) {
        editorSPref.putString(Constants.SP_USER_ID, loggedInUser.getId());
        editorSPref.putString(Constants.SP_USER_NAME, loggedInUser.getUserName());
        editorSPref.putString(Constants.SP_USER_EMAIL, loggedInUser.getUserEmail());
        editorSPref.putString(Constants.SP_USER_GOOGLE_ID, loggedInUser.getUserGoogleAccountId());
        editorSPref.putString(Constants.SP_USER_PHOTO_URL, loggedInUser.getUserPhotoUrl());
        editorSPref.putString(Constants.SP_USER_ADVER_ID, googleAdverId);
        editorSPref.putString(Constants.SP_USER_AUTH_UID, loggedInUser.getAuthUid());
        editorSPref.commit();
    }

    private SpUserModel getUserData_sPref() {
        SharedPreferences readSPref_forUserData = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);

        String userId = readSPref_forUserData.getString(Constants.SP_USER_ID, UserContext.getLoggedInUser().getId());
        String userName = readSPref_forUserData.getString(Constants.SP_USER_NAME, UserContext.getLoggedInUser().getUserName());
        String userEmail = readSPref_forUserData.getString(Constants.SP_USER_EMAIL, UserContext.getLoggedInUser().getUserEmail());
        String userGoogleId = readSPref_forUserData.getString(Constants.SP_USER_GOOGLE_ID, UserContext.getLoggedInUser().getUserGoogleAccountId());
        String userPhotoUrl = readSPref_forUserData.getString(Constants.SP_USER_PHOTO_URL, UserContext.getLoggedInUser().getUserPhotoUrl());
        String userAdverId = readSPref_forUserData.getString(Constants.SP_USER_ADVER_ID, UserContext.getUserAdverId());
        String userAuthId = readSPref_forUserData.getString(Constants.SP_USER_AUTH_UID, UserContext.getLoggedInUser().getAuthUid());

        User loggedInUser = new User(userId, userName, userEmail, userGoogleId, userPhotoUrl, userAdverId, userAuthId);
        UserContext.setLoggedInUser(loggedInUser);

        SpUserModel spUserModel = new SpUserModel(userId, userName, userEmail, userGoogleId, userPhotoUrl, userAuthId, userAdverId);
        return spUserModel;
    }

    private void createSQLDatabaseTable(User user) {
        DBHelper dbHelper = new DBHelper(this);

        Map<String, AllGameInOne> allGameMap = dbHelper.getUserAllGameData(user.getId());
        AllGameInOne gameData = null;
        for (Game game : Game.values()) {
            if (allGameMap == null || !allGameMap.containsKey(game.getId())) {
                gameData = new AllGameInOne(user.getId(), game.getId(),
                        Constants.DEFAULT_CHANCE_LEFT, Constants.DEFAULT_COINS, Constants.DEFAULT_CASH);
                dbHelper.insertFreeAdGameData(gameData);
            }
//            if (gameData != null) {
//                /** Set UserContext for new row inserted in the table **/
//                UserContext.setAllGameInOneMapByGameIdAndData(game.getId(), gameData);
//            } else {
//                /** Set UserContext for existing game in the table **/
//                UserContext.setAllGameInOneMapByGameIdAndData(game.getId(), allGameMap.get(game.getId()));
//            }
        }
    }

    private void checkAndInitializeSDK(User loggedInUser) {
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean googleForceUpdate = remoteConfig.getBoolean(Constants.GOOGLE_FORCE_UPDATE);
                    UserContext.setGoogleForceUpdate(googleForceUpdate);

                    boolean autoClean_leaderboard = remoteConfig.getBoolean(Constants.AUTO_CLEAN_LEADERBOARD);
                    UserContext.setAutoCleanLeaderboard(autoClean_leaderboard);
                    if (autoClean_leaderboard) {
                        long cleanLeaderboard_interval = remoteConfig.getLong(Constants.CLEAN_LEADERBOARD_INTERVAL);
                        UserContext.setClean_leaderboard_interval(cleanLeaderboard_interval);
                    }
//                    boolean isDailyWinner = remoteConfig.getBoolean(Constants.IS_DAILY_WINNER_LEADERBOARD);
//                    boolean isWeeklyWinner = remoteConfig.getBoolean(Constants.IS_WEEKLY_WINNER_LEADERBOARD);
//                    UserContext.setIsDailyWinnerLeaderboard(isDailyWinner);
//                    UserContext.setIsWeeklyWinnerLeaderboard(isWeeklyWinner);

                    boolean checkFraudUser = remoteConfig.getBoolean(Constants.CHECK_FRAUD_USER);
                    UserContext.setCheckFraudUser(checkFraudUser);
                    if (checkFraudUser) {
                        long userCoinBalance = remoteConfig.getLong(Constants.USER_COIN_BALANCE);
                        UserContext.setUserCoinBalance(userCoinBalance);
                    }

                    boolean isPaypalEnabled = remoteConfig.getBoolean(Constants.IS_PAYPAL_PAYMENT_ENABLED);
                    UserContext.setIsPaypalPaymentEnabled(isPaypalEnabled);
                    boolean isAmazonEnabled = remoteConfig.getBoolean(Constants.IS_AMAZON_PAYMENT_ENABLED);
                    UserContext.setIsAmazonPaymentEnabled(isAmazonEnabled);
                    boolean isUSD_1_Enabled = remoteConfig.getBoolean(Constants.USD_1_PAYMENT_ENABLED);
                    UserContext.setIsUSD_1_Disable(isUSD_1_Enabled);

                    long maxInGameCoins = remoteConfig.getLong(Constants.MAX_IN_GAME_COINS);
                    UserContext.setMaxInGameCoins((int)maxInGameCoins);
                    long minInGameCoins = remoteConfig.getLong(Constants.MIN_IN_GAME_COINS);
                    UserContext.setMinInGameCoins((int)minInGameCoins);
                    long maxGameQuizCoins = remoteConfig.getLong(Constants.MAX_GAME_QUIZ_COINS);
                    UserContext.setMaxGameQuizCoins((int)maxGameQuizCoins);
                    long minGameQuizCoins = remoteConfig.getLong(Constants.MIN_GAME_QUIZ_COINS);
                    UserContext.setMinGameQuizCoins((int)minGameQuizCoins);

//                    String getUpstoxReferralLink = remoteConfig.getString(Constants.GET_UPSTOX_REFERRAL_LINK);
//                    UserContext.setUpstoxReferralLink(getUpstoxReferralLink);
//                    String getQurekaLink = remoteConfig.getString(Constants.GET_QUREKA_LINK);
//                    UserContext.setQurekaLink(getQurekaLink);
//                    String getGamezopLink = remoteConfig.getString(Constants.GET_GAMEZOP_LINK);
//                    UserContext.setGamezopLink(getGamezopLink);
//
//                    boolean isCPALeadOfferwall_enabled = remoteConfig.getBoolean(Constants.IS_CPA_LEAD_OFFERWALL_enabled);
//                    UserContext.setIsCPALeadOfferwallEnabled(isCPALeadOfferwall_enabled);
//                    String getCPALeadOfferwallLink = remoteConfig.getString(Constants.GET_CPA_LEAD_OFFERWALL_LINK);
//                    UserContext.setCpaLeadOfferwallLink(getCPALeadOfferwallLink);
//                    String uniqueUserCPALeadURL = getCPALeadOfferwallLink + "&subid=" + googleAdverId;
//                    UserContext.setUniqueUserCpaLeadOfferwallLink(uniqueUserCPALeadURL);
//
//                    boolean isAdmobEnabled = remoteConfig.getBoolean(Constants.IS_ADMOB_ENABLED);
////                    boolean isAdmobEnabled = false;
//                    UserContext.setIsAdmobEnabled(isAdmobEnabled);
//
//                    boolean isTapdaqEnabled = remoteConfig.getBoolean(Constants.IS_TAPDAQ_ENABLED);
////                    boolean isTapdaqEnabled = false;
//                    UserContext.setIsTapdaqEnabled(isTapdaqEnabled);

                    boolean isTapjoyEnabled = remoteConfig.getBoolean(Constants.IS_TAPJOY_ENABLED);
//                    boolean isTapjoyEnabled = false;
                    UserContext.setIsTapjoy_enabled(isTapjoyEnabled);

                    boolean isAppLovinEnabled = remoteConfig.getBoolean(Constants.IS_APPLOVIN_ENABLED);
//                    boolean isAppLovinEnabled = false;
                    UserContext.setIsAppLovin_enabled(isAppLovinEnabled);

                    boolean isAppodealEnabled = remoteConfig.getBoolean(Constants.IS_APPODEAL_ENABLED);
//                    boolean isAppodealEnabled = true;
                    UserContext.setIsAppodeal_enabled(isAppodealEnabled);

//                    boolean isUnityEnabled = remoteConfig.getBoolean(Constants.IS_UNITY_ENABLED);
////                    boolean isUnityEnabled = false;
//                    UserContext.setIsUnity_enabled(isUnityEnabled);
//
//                    boolean isIronSourceEnabled = remoteConfig.getBoolean(Constants.IS_IRONSOURCE_ENABLED);
////                    boolean isIronSourceEnabled = true;
//                    UserContext.setIsIronSourceEnabled(isIronSourceEnabled);

//                    if (isAdmobEnabled || isTapdaqEnabled || isTapjoyEnabled || isAppLovinEnabled || isUnityEnabled || isIronSourceEnabled) {
                    if (isTapjoyEnabled || isAppLovinEnabled || isAppodealEnabled) {

//                        if (isAdmobEnabled) {
//                            boolean isAdmob_banner = remoteConfig.getBoolean(Constants.IS_ADMOB_BANNER);
//                            boolean isAdmob_interstitial = remoteConfig.getBoolean(Constants.IS_ADMOB_INTERSTITIAL);
//                            boolean isAdmob_rewarded = remoteConfig.getBoolean(Constants.IS_ADMOB_REWARDED);
//
//                            UserContext.setIsAdmobBanner(isAdmob_banner);
//                            UserContext.setIsAdmobInterstitial(isAdmob_interstitial);
//                            UserContext.setIsAdmobRewarded(isAdmob_rewarded);
//
////                            /** Chartboost **/
////                            Chartboost.startWithAppId(getApplicationContext(), getString(R.string.cB_app_id), getString(R.string.cB_app_sign));
//                            MobileAds.initialize(SplashScreenActivity.this, new OnInitializationCompleteListener() {
//                                @Override
//                                public void onInitializationComplete(InitializationStatus initializationStatus) {
//                                    Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
//                                    for (String adapterClass : statusMap.keySet()) {
//                                        AdapterStatus status = statusMap.get(adapterClass);
//                                        Log.d("App", String.format(
//                                                "Adapter name: %s, Description: %s, Latency: %d",
//                                                adapterClass, status.getDescription(), status.getLatency()));
//                                    }
//                                    // Start loading ads here...
//                                    connectedToAdNetwork = true;
////                                    startActivity();
//                                }
//                            });
//                        }
//
//                        if (isTapdaqEnabled) {
//                            boolean isTapdaqBanner = remoteConfig.getBoolean(Constants.IS_TAPDAQ_BANNER);
//                            boolean isTapdaqInterstitial = remoteConfig.getBoolean(Constants.IS_TAPDAQ_INTERSTITIAL);
//                            boolean isTapdaqRewarded = remoteConfig.getBoolean(Constants.IS_TAPDAQ_REWARDED);
//                            boolean isTapdaqNative = remoteConfig.getBoolean(Constants.IS_TAPDAQ_NATIVE);
//
//                            UserContext.setIsTapdaqBanner(isTapdaqBanner);
//                            UserContext.setIsTapdaqInterstitial(isTapdaqInterstitial);
//                            UserContext.setIsTapdaqRewarded(isTapdaqRewarded);
//                            UserContext.setIsTapdaqNative(isTapdaqNative);
//
//                            TapdaqConfig config = Tapdaq.getInstance().config();
//                            config.setUserSubjectToGdprStatus(STATUS.FALSE); //GDPR declare if user is in EU
//                            config.setConsentStatus(STATUS.TRUE); //GDPR consent must be obtained from the user
//                            config.setAgeRestrictedUserStatus(STATUS.FALSE); //Is user subject to COPPA or GDPR age restrictions
//
////                            /** Chartboost **/
////                            Chartboost.startWithAppId(getApplicationContext(), getString(R.string.cB_app_id), getString(R.string.cB_app_sign));
//                            Tapdaq.getInstance().initialize(SplashScreenActivity.this,
//                                    getString(R.string.tad_app_id), getString(R.string.tad_client_key),
//                                    config, new TMInitListenerBase() {
//                                        @Override
//                                        public void didInitialise() {
//                                            connectedToAdNetwork = true;
////                                            startActivity();
////                                            Log.e(TAG, "Tapdaq SDK initialized successfully");
//                                        }
//
//                                        @Override
//                                        public void didFailToInitialise(TMAdError tmAdError) {
//                                            connectedToAdNetwork = false;
////                                            startActivity();
////                                            Log.e(TAG, "Tapdaq SDK failed to initialize");
//                                        }
//                                    });
//                        }

                        if (isTapjoyEnabled) {
                            boolean isTapjoy_offerwall = remoteConfig.getBoolean(Constants.IS_TAPJOY_OFFERWALL);
                            UserContext.setIsTapjoy_offerwall(isTapjoy_offerwall);

                            Hashtable<String, Object> connectFlags = new Hashtable<String, Object>();
//                            connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING, true);
                            connectFlags.put(TapjoyConnectFlag.USER_ID, UserContext.getLoggedInUser().getId());

                            Tapjoy.connect(getApplicationContext(), getString(R.string.tJ_sdk_key),
                                    connectFlags, new TJConnectListener() {
                                        @Override
                                        public void onConnectSuccess() {
                                            connectedToAdNetwork = true;
                                            Tapjoy.setUserID(UserContext.getLoggedInUser().getId());
                                            Tapjoy.setGcmSender(getString(R.string.firebase_push_sender_id));
//                                            Tapjoy.setDebugEnabled(true);

                                            Tapjoy.onActivityStart(SplashScreenActivity.this);
//                                            Log.e(TAG, "Tapjoy SDK initialized successfully");
                                        }

                                        @Override
                                        public void onConnectFailure() {
                                            connectedToAdNetwork = false;
//                                            Log.e(TAG, "Tapjoy SDK FAILED initialize");
                                        }
                                    });
                        }

                        if (isAppLovinEnabled) {
                            boolean isAppLovinBanner = remoteConfig.getBoolean(Constants.IS_APPLOVIN_BANNER);
                            boolean isAppLovinInterstitial = remoteConfig.getBoolean(Constants.IS_APPLOVIN_INTERSTITIAL);
                            boolean isAppLovinRewarded = remoteConfig.getBoolean(Constants.IS_APPLOVIN_REWARDED);
                            boolean isAppLovin_mediumNative = remoteConfig.getBoolean(Constants.IS_APPLOVIN_MEDIUM_NATIVE);
//                            boolean isAppLovin_manualNative = remoteConfig.getBoolean(Constants.IS_APPLOVIN_MANUAL_NATIVE);

                            UserContext.setIsAppLovin_banner(isAppLovinBanner);
                            UserContext.setIsAppLovin_interstitial(isAppLovinInterstitial);
                            UserContext.setIsAppLovin_rewarded(isAppLovinRewarded);
                            UserContext.setIsAppLovin_mediumNative(isAppLovin_mediumNative);
//                            UserContext.setIsAppLovin_manualNative(isAppLovin_manualNative);

//                            /** Chartboost **/
//                            Chartboost.startWithAppId(getApplicationContext(), getString(R.string.cB_app_id), getString(R.string.cB_app_sign));

                            AppLovinSdkSettings appLovin_adUnit_settings = new AppLovinSdkSettings(SplashScreenActivity.this);
                            List<String> adUnitIds = new ArrayList<>();
                            adUnitIds.add(getString(R.string.aL_banner_default)); // Banner Ad Unit ID
                            adUnitIds.add(getString(R.string.aL_interstitial_default)); // Interstitial Ad Unit ID
                            adUnitIds.add(getString(R.string.aL_rewarded_default)); // Rewarded Ad Unit ID
                            adUnitIds.add(getString(R.string.aL_native_default_medium)); // Native Ad Unit ID
                            appLovin_adUnit_settings.setInitializationAdUnitIds(adUnitIds);

                            // Make sure to set the mediation provider value to "max" to ensure proper functionality
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

                        if (isAppodealEnabled) {
                            boolean isAppodealBanner = remoteConfig.getBoolean(Constants.IS_APPODEAL_BANNER);
                            boolean isAppodealInterstitial = remoteConfig.getBoolean(Constants.IS_APPODEAL_INTERSTITIAL);
                            boolean isAppodealRewarded = remoteConfig.getBoolean(Constants.IS_APPODEAL_REWARDED);
                            UserContext.setIsAppodeal_banner(isAppodealBanner);
                            UserContext.setIsAppodeal_interstitial(isAppodealInterstitial);
                            UserContext.setIsAppodeal_rewarded(isAppodealRewarded);

//                            /** Chartboost **/
//                            Chartboost.startWithAppId(getApplicationContext(), getString(R.string.cB_app_id), getString(R.string.cB_app_sign));

                            Appodeal.initialize(SplashScreenActivity.this, getString(R.string.apd_app_key),
                                    Appodeal.BANNER | Appodeal.INTERSTITIAL | Appodeal.REWARDED_VIDEO,
                                    true);
//                            Appodeal.setAutoCache(Appodeal.INTERSTITIAL, false);
                            Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, false);
//                            Appodeal.setSharedAdsInstanceAcrossActivities(true); // Default - false
                        }

//                        if (isUnityEnabled) {
//                            boolean isUnity_interstitial = remoteConfig.getBoolean(Constants.IS_UNITY_INTERSTITIAL);
//                            boolean isUnity_rewarded = remoteConfig.getBoolean(Constants.IS_UNITY_REWARDED);
//
//                            UserContext.setIsUnity_interstitial(isUnity_interstitial);
//                            UserContext.setIsUnity_rewarded(isUnity_rewarded);
//
//                            InitializationConfiguration unityConfig = InitializationConfiguration.builder()
//                                    .setGameId(getString(R.string.uni_ga_id))
//                                    .setInitializationListener(new IInitializationListener() {
//                                        @Override
//                                        public void onInitializationComplete() {
//                                            connectedToAdNetwork = true;
//                                            MetaData userMetaData = new MetaData(SplashScreenActivity.this);
//                                            // You must commit the changes to the MetaData object for each value
//                                            // before trying to set another value.
//                                            // If the user opts in to personalized ads:
//                                            userMetaData.set("user.nonbehavioral", false);
//                                            userMetaData.commit();
//                                            // If the user opts in to targeted advertising:
//                                            userMetaData.set("gdpr.consent", true);
//                                            userMetaData.commit();
//
////                                            Log.e("Splash UNITY Mediation", " - Initialized Successfully");
//                                        }
//
//                                        @Override
//                                        public void onInitializationFailed(SdkInitializationError sdkInitializationError, String s) {
//                                            connectedToAdNetwork = false;
////                                            Log.e("Splash UNITY Mediation", " - FAILED to Initialize");
//                                        }
//                                    }).build();
//
//                            UnityMediation.initialize(unityConfig);
//                        }
//
//                        if (isIronSourceEnabled) {
//                            boolean isIronsourceOfferwall = remoteConfig.getBoolean(Constants.IS_IRONSOURCE_OFFERWALL);
//                            UserContext.setIsIronSourceOfferwall(isIronsourceOfferwall);
//
//                            /**
//                             Ad Units should be in the type of IronSource.Ad_Unit.AdUnitName, example */
////                            IronSource.init(SplashScreenActivity.this, getString(R.string.iS_app_id),
////                                    IronSource.AD_UNIT.OFFERWALL);
////                            IronSource.init(SplashScreenActivity.this,
////                                    getString(R.string.iS_app_id), new IronSourceInitListener());
//
//                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                getUserDataAndNavigate(loggedInUser);
                                startActivity();
                            }
                        }, 3000);
                    } else {
                        connectedToAdNetwork = false;
//                        getUserDataAndNavigate(loggedInUser);
                        startActivity();
                    }
                } else {
//                    Log.e(TAG, "Exception while remote config is fetched.");
                }
            }
        });
    }

    private void startActivity() {
        boolean showLanguageSelection = readSPref.getBoolean(Constants.SP_SHOW_LANGUAGE_ACTIVITY, true);

        if (connectedToAdNetwork) {
            showCustomToast(new StringBuffer()
                    .append(getString(R.string.welcome_text)).append(" ")
                    .append(firebaseAuth.getCurrentUser().getDisplayName()).toString());
            progressDialog.dismiss();
//            if (showLanguageSelection) {
//                startActivity(new Intent(SplashScreenActivity.this, LanguageSelectionActivity.class));
//            } else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//            }
        } else {
            showCustomToast(new StringBuffer()
                    .append(getString(R.string.welcome_text)).append(" ")
                    .append(firebaseAuth.getCurrentUser().getDisplayName()).toString());
            progressDialog.dismiss();
//            if (showLanguageSelection) {
//                startActivity(new Intent(SplashScreenActivity.this, LanguageSelectionActivity.class));
//            } else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(SplashScreenActivity.this);
        super.onStop();
    }

}
