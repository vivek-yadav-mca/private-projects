package com.o2games.playwin.android.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.o2games.playwin.android.AlarmService.DailyRewardAlarmReciever;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.TapdaqNativeLargeLayout;
import com.o2games.playwin.android.activity.FlipActivity;
import com.o2games.playwin.android.activity.MainActivity;
import com.o2games.playwin.android.activity.ScratchActivity;
import com.o2games.playwin.android.activity.SpinningActivity;
import com.o2games.playwin.android.databinding.FragmentHomeBinding;
import com.o2games.playwin.android.model.User;
import com.o2games.playwin.android.sqlUserGameData.DBHelper;
import com.o2games.playwin.android.userData.UserContext;
import com.tapjoy.TJGetCurrencyBalanceListener;
import com.tapjoy.TJSpendCurrencyListener;
import com.tapjoy.Tapjoy;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    View rootView;
    private static HomeFragment instance;
    private MainActivity mainActivity;
    private static final String TAG = HomeFragment.class.getName();
    private FirebaseDataService firebaseDataService;
    private static final String sqlTotalCashCoinCOL = Game.TOTAL_CASH_COINS.getId();
    private String sql_totalCash;
    private String sql_totalCoins;
    public static int TAPJOY_CURRENCY_BALANCE;
    private User loggedInUser;

    private Context context;
    FragmentHomeBinding binding;
    private ConnectivityManager connectivityManager;
    private FirebaseRemoteConfig remoteConfig;
    private DatabaseReference databaseRef;
    private DBHelper dbHelper;
    private Handler handler;
    private SharedPreferences writeSPref;
    private SharedPreferences readSPref;
    private SharedPreferences.Editor editorSPref;
    private CountDownTimer timer;
    private AlertDialog underMaintenanceDialog;
    private AlertDialog progressDialog;
    private Animation scaleDown;
    private Animation rotate_clockwise;
    private Animation blinking;
    private Random random;

//    public static String UPSTOX_REFERRAL_LINK;
//    public static String GAMEZOP_COMMON_URL; // = "https://www.gamezop.com/?id=4114";
    public static String GZOP_MGL_DEDICATED_URL; // = "https://www.gamezop.com/g/<Game URL>?id=4114";
//    public static String QUREKA_COMMON_URL; // = "https://633.live.qureka.com"; / "https://633.live.predchamps.com"
    public static String QUREKA_PREDCHAMP_DEDICATED_URL; // = "https://633.live.qureka.com/freeentry/?id=<Quiz ID>";
    int cctToolbarColor;

    public static long QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS;
    public static final int MAX_QUIZ_PRED_COUNTDOWN_TIME = 6; // exclusive 6 will not be counted
    public static final int MIN_QUIZ_PRED_COUNTDOWN_TIME = 3;
    public static long QUIZ_PRED_AMOUNT_OF_REWARD;
    //    public static final String QUIZ_AMOUNT_OF_REWARD_TEXT = "+ 75";
    public static String QUIZ_PRED_AMOUNT_OF_REWARD_TEXT; // Also change bsDialog text

    private boolean gamezopClicked = false;
    private int MAX_GAME_QUIZ_REWARD_AMOUNT = 76;
    private int MIN_GAME_QUIZ_REWARD_AMOUNT = 50;

    public static  long GAME_COUNTDOWN_TIME_IN_MILLIS;
    public static final int MAX_GAME_COUNTDOWN_TIME = 7;
    public static final int MIN_GAME_COUNTDOWN_TIME = 4;
    public static long GAME_AMOUNT_OF_REWARD;
    //    public static final String GAME_AMOUNT_OF_REWARD_TEXT = "+ 100";
    public static String GAME_AMOUNT_OF_REWARD_TEXT; // Also change bsDialog text

    public static int COUNTDOWN_TIME_IN_MINUTES;
    private long mTimeLeft_in_millis;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private boolean updateScoreAutomatically_OnResume = true;

//    boolean isAdmobRewardedEnabled;
//    private String ADMOB_REWARDED_AD_UNIT_ID;
//    private RewardedAd admobRewarded;
//
//    boolean isTapdaqRewardedEnabled;
//    public static String TAPDAQ_REWARDED_ID;

    private boolean isAppLovinRewardedEnabled;
    private static String APPLOVIN_REWARDED_ID;
    private MaxRewardedAd applovinRewarded;

    private boolean isAppodealRewardedEnabled;

//    private boolean isUnityRewardedEnabled;
//    private boolean isUnityRewardedReady = false;
//    private static String UNITY_REWARDED_ID;
//    private com.unity3d.mediation.RewardedAd unityRewarded;

    public HomeFragment() {
    }

    public static HomeFragment GetInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        instance = this;
        loggedInUser = UserContext.getLoggedInUser();
        mainActivity = MainActivity.GetInstance();
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        context = getActivity();
        ViewPager2 viewPager_main_activity = getActivity().findViewById(R.id.viewPager_main_activity);

        firebaseDataService = new FirebaseDataService(getActivity());
        dbHelper = new DBHelper(getContext());
        handler = new Handler(Looper.getMainLooper());
        databaseRef = FirebaseDatabase.getInstance().getReference();
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.anim_scale_down);
        rotate_clockwise = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_clockwise);
        blinking = AnimationUtils.loadAnimation(context, R.anim.anim_blinking_repeat);
        random = new Random();

        updateHomeUI_CashCoinWallet();

        MAX_GAME_QUIZ_REWARD_AMOUNT = UserContext.getMaxGameQuizCoins();
        MIN_GAME_QUIZ_REWARD_AMOUNT = UserContext.getMinGameQuizCoins();

//        isAdmobRewardedEnabled = UserContext.getIsAdmobRewarded();
//        isTapdaqRewardedEnabled = UserContext.getIsTapdaqRewarded();
        isAppLovinRewardedEnabled = UserContext.getIsAppLovin_rewarded();
//        isUnityRewardedEnabled = UserContext.getIsUnity_rewarded();
        isAppodealRewardedEnabled = UserContext.getIsAppodeal_rewarded();

        // Banner disabled in fragment.
//        ADMOB_REWARDED_AD_UNIT_ID = getString(R.string.aM_rewarded_default);
//        TAPDAQ_REWARDED_ID = getString(R.string.tad_rewarded_default);
        APPLOVIN_REWARDED_ID = getString(R.string.aL_rewarded_default);
        applovinRewarded = MaxRewardedAd.getInstance(APPLOVIN_REWARDED_ID, getActivity());
//        UNITY_REWARDED_ID = getString(R.string.uni_rewarded_default);
        isAppodealRewardedEnabled = UserContext.getIsAppodeal_rewarded();

        if (!internetConnected(context)) {
            showNoInternetDialog(rootView);
        }

        try {
            String spUserId = loggedInUser.getId() + "_";
            writeSPref = getActivity().getSharedPreferences("o2_" + loggedInUser.getId() + Constants.USER_SPECIFIC, Context.MODE_PRIVATE);
            editorSPref = writeSPref.edit();
            readSPref = getActivity().getSharedPreferences("o2_" + loggedInUser.getId() + Constants.USER_SPECIFIC, Context.MODE_PRIVATE);

//            writeSPref = getActivity().getSharedPreferences("o2_" + spUserId + Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
//            readSPref = getActivity().getSharedPreferences("o2_" + spUserId + Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
        } catch (Exception e) {
//            Log.e(TAG, "Error while creating shared preference:", e);
            throw new RuntimeException(e);
        }

/**** Home Daily Reward Timer Setting ****/
        updateUIrewardTimer();
/**** Home Daily Reward Timer Setting ****/


        binding.refreshHomeScoreButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.refreshHomeScoreButtonCV.startAnimation(rotate_clockwise);
//                updateUICoinsWallet();
                updateHomeUI_CashCoinWallet();
            }
        });

        if (!UserContext.getIsTapjoy_offerwall()) {
//        if (!UserContext.getIsTapjoy_offerwall() && !UserContext.getIsIronSourceOfferwall()) {
            binding.homeFragOfferwallCardView.setVisibility(View.GONE);
        }
        binding.homeFragOfferwallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.showOfferwallFrom();
//                IronSource.showOfferwall();
//                show_free_addCoinsDialog(rootView, 100);
            }
        });

        RequestOptions loadImageConfig = new RequestOptions()
                .centerCrop()
                .circleCrop()  //to crop image in circle view
                .placeholder(R.drawable.user_color)
                .error(R.drawable.user_color);

        Glide.with(getActivity())
                .load(loggedInUser.getUserPhotoUrl())
                .apply(loadImageConfig)
                .into(binding.homeUserPhotoBox);

        binding.homeUserPhotoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager_main_activity.setCurrentItem(4);
            }
        });

        binding.homeFragCashBalanceFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager_main_activity.setCurrentItem(3);
            }
        });
        binding.homeFragCoinBalanceFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager_main_activity.setCurrentItem(3);
            }
        });

        binding.dailyRewardCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!internetConnected(context)) {
                    showNoInternetDialog(rootView);
                } else {
                    binding.dailyRewardCardView.startAnimation(scaleDown);
                    showDailyRewardDialog(rootView);
                }
            }
        });

        binding.spinWheelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!internetConnected(context)) {
                    showNoInternetDialog(rootView);
                } else {
                    binding.spinCvFrame.startAnimation(scaleDown);
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(((ViewGroup)getView().getParent()).getId(), new SpinFragment());
//                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    transaction.commit();
                    startActivity(new Intent(getActivity(), SpinningActivity.class));
                }
            }
        });

        binding.scratchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!internetConnected(context)) {
                    showNoInternetDialog(rootView);
                } else {
                    binding.scratchCvFrame.startAnimation(scaleDown);
                    startActivity(new Intent(getActivity(), ScratchActivity.class));
                }
            }
        });

        binding.flipWinCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!internetConnected(context)) {
                    showNoInternetDialog(rootView);
                } else {
                    binding.flipCvFrame.startAnimation(scaleDown);
                    startActivity(new Intent(getActivity(), FlipActivity.class));
                }
            }
        });

        /**
         * * Qureka
         * **/
        binding.qurekaQuizCricketQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizCricketQuiz.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.qureka_cricket_quiz_url, R.string.qureka_cricket_quiz,
                        R.drawable.qureka_circle_cricket_quiz, R.color.qureka_cricket_quiz);
            }
        });
        binding.qurekaQuizTechQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizTechQuiz.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.qureka_tech_quiz_url, R.string.qureka_tech_quiz,
                        R.drawable.qureka_circle_tech_quiz, R.color.qureka_tech_quiz);
            }
        });
        binding.qurekaQuizGkQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizGkQuiz.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.qureka_gk_basic_quiz_url, R.string.qureka_gk_basic_quiz,
                        R.drawable.qureka_circle_gk_quiz, R.color.qureka_gk_basic_quiz);
            }
        });
        binding.qurekaQuizIplQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizIplQuiz.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.qureka_ipl_quiz_url, R.string.qureka_ipl_quiz,
                        R.drawable.qureka_circle_ipl_quiz, R.color.qureka_ipl_quiz);
            }
        });

        binding.qurekaHomeIplQuizBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaHomeIplQuizBanner.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.qureka_rec_ipl_quiz_50k_url, R.string.qureka_rec_ipl_quiz_50k,
                        R.drawable.qureka_rec_ipl_quiz_50k, R.color.qureka_rec_ipl_quiz_50k);
            }
        });

        /**
         * * Predchamp
         * **/
        binding.predchampCricketPrediction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.predchampCricketPrediction1.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_cricket_result_url_1,
                        R.string.predchamp_predict_cricket_result_1, R.drawable.predchamp_predict_cricket_result_png_1,
                        R.color.predchamp_predict_cricket_result_1);
            }
        });
        binding.predchampPredictResultWinCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.predchampPredictResultWinCoin.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_result_win_coin_url, R.string.predchamp_predict_result_win_coin,
                        R.drawable.predchamp_predict_result_win_coin_png, R.color.predchamp_predict_result_win_coin);
            }
        });
        binding.predchampMoviePrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.predchampMoviePrediction.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_movie_url, R.string.predchamp_predict_movie,
                        R.drawable.predchamp_predict_movie_png, R.color.predchamp_predict_movie);
            }
        });
        binding.predchampCricketPrediction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.predchampCricketPrediction2.startAnimation(scaleDown);
                gamezopClicked = false;
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_cricket_result_url_2, R.string.predchamp_predict_cricket_result_2,
                        R.drawable.predchamp_predict_cricket_result_png_2, R.color.predchamp_predict_cricket_result_2);
            }
        });


        /**
         * * Dedicated Gamezop URL
         * **/
        binding.homeGamezopBottleShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopBottleShoot.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_bottle_shoot_url, R.string.gamezop_bottle_shoot,
                        R.drawable.gamezop_action_bottle_shoot_webp, R.color.gamezop_bottle_shoot);
            }
        });
        binding.homeGamezopSolitaireGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopSolitaireGold.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_solitaire_gold_url, R.string.gamezop_solitaire_gold,
                        R.drawable.gamezop_strategy_solitaire_gold_webp, R.color.gamezop_solitaire_gold);
            }
        });
        binding.homeGamezopCityCricket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopCityCricket.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_city_cricket_url, R.string.gamezop_city_cricket,
                        R.drawable.gamezop_sports_racing_city_cricket_webp, R.color.gamezop_city_cricket);
            }
        });

        binding.homeGamezopBubbleWipeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopBubbleWipeout.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_bubble_wipeout_url, R.string.gamezop_bubble_wipeout,
                        R.drawable.gamezop_strategy_bubble_wipeout_webp, R.color.gamezop_bubble_wipeout);
            }
        });
        binding.homeGamezopStickyGoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopStickyGoo.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_sticky_goo_url, R.string.gamezop_sticky_goo,
                        R.drawable.gamezop_adventure_sticky_goo_webp, R.color.gamezop_sticky_goo);
            }
        });
        binding.homeGamezopSaloonRobbery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopSaloonRobbery.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_saloon_robbery_url, R.string.gamezop_saloon_robbery,
                        R.drawable.gamezop_action_saloon_robbery_webp, R.color.gamezop_saloon_robbery);
            }
        });

        binding.homeGamezopJellySlice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopJellySlice.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_jelly_slice_url, R.string.gamezop_jelly_slice,
                        R.drawable.gamezop_puzzle_logic_jelly_slice_webp, R.color.gamezop_jelly_slice);
            }
        });
        binding.homeGamezopBouncy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopBouncy.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_bouncy_url, R.string.gamezop_bouncy,
                        R.drawable.gamezop_arcade_bouncy_webp, R.color.gamezop_bouncy);
            }
        });
        binding.homeGamezopJimboJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeGamezopJimboJump.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_jimbo_jump_url, R.string.gamezop_jimbo_jump,
                        R.drawable.gamezop_adventure_jimbo_jump_webp, R.color.gamezop_jimbo_jump);
            }
        });

        return binding.getRoot();
    }

//    private void showUpstoxOfferBsDialog(View v) {
//        BottomSheetDialog upstoxBsDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialog);
//        upstoxBsDialog.setContentView(R.layout.layout_bsdialog_upstox_referral);
//        upstoxBsDialog.getDismissWithAnimation();
////        upstoxBsDialog.setCancelable(false);
//        upstoxBsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        upstoxBsDialog.show();
//
//        FrameLayout upstox_dialog_ac_open_btn = upstoxBsDialog.findViewById(R.id.upstox_dialog_ac_open_btn);
//        TextView upstox_bs_dialog_title_2 = upstoxBsDialog.findViewById(R.id.upstox_bs_dialog_title_2);
//        TextView upstox_bs_dialog_title_4 = upstoxBsDialog.findViewById(R.id.upstox_bs_dialog_title_4);
//        TextView upstox_bs_dialog_title_6 = upstoxBsDialog.findViewById(R.id.upstox_bs_dialog_title_6);
//
//        upstox_bs_dialog_title_2.startAnimation(blinking);
//        upstox_bs_dialog_title_4.startAnimation(blinking);
//        upstox_bs_dialog_title_6.startAnimation(blinking);
//
//        upstox_dialog_ac_open_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UPSTOX_REFERRAL_LINK));
//                startActivity(browserIntent);
//            }
//        });
//    }

    private void showCustomToast(View rootView1, String toastMessage) {
        LayoutInflater inflater = getLayoutInflater();
        rootView1 = inflater.inflate(R.layout.layout_toast_custom,
                (ConstraintLayout) rootView1.findViewById(R.id.custom_toast_constraint));

        TextView toastText = rootView1.findViewById(R.id.custom_toast_text);
        toastText.setText(toastMessage);

        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(rootView1);
        toast.show();
    }

    private void updateUIrewardTimer() {
        long homePrevious10Time = readSPref.getLong(Constants.SP_10_BUTTON_TIME, 0);
        Date homeDateTime = new Date(System.currentTimeMillis());
        long homeCurrentTime = homeDateTime.getTime();
        //    TextView home_timer_reward_10 = rootView.findViewById(R.id.home_daily_reward_timer);
        if (homePrevious10Time == 0) {
            binding.homeDailyRewardTimer.setText(context.getString(R.string.home_frag_cV_reward_available));
        }
        homeTimer1Hours(homePrevious10Time, homeCurrentTime);
    }

    /*private AllGameInOne getAllGameInOneMapByGameId(String gameId) {
        AllGameInOne allGameInOne = UserContext.getAllGameInOneMapByGameId(gameId);
        if(allGameInOne == null) {
            // returned NULL, get userData from shared preferences
            return allGameInOne=  UserContext.getAllGameInOneMapByUserIdGameId(getUserData_sPref().getUserId(), gameId);
        } else {
            return allGameInOne;
        }
    }*/

    public void updateHomeUI_CashCoinWallet() {
        sql_totalCash = dbHelper.getFreeAdGameDataByUserIdAndGameId(loggedInUser.getId(), sqlTotalCashCoinCOL).getCash();
//        sql_totalCash = firebaseDataService.getUserWalletBalance();
//        sql_totalCash = UserContext.getAllGameInOneMapByGameId(sqlTotalCashCoinCOL).getCash();
        binding.homeUserTotalCashBalance.setText(new StringBuilder().append("\u0024 ").append(sql_totalCash).toString());

        sql_totalCoins = dbHelper.getFreeAdGameDataByUserIdAndGameId(loggedInUser.getId(), sqlTotalCashCoinCOL).getCoins();
//        sql_totalCoins = firebaseDataService.getCoinBalance();
//        sql_totalCoins = UserContext.getAllGameInOneMapByGameId(sqlTotalCashCoinCOL).getCoins();
        binding.homeUserTotalCoinBalance.setText(sql_totalCoins);

        updateScoreAutomatically_OnResume = true;
    }

    private long getRandomQuizPredict_CountdownTime() {
        int randomQuizCountdownTime = random.nextInt(MAX_QUIZ_PRED_COUNTDOWN_TIME - MIN_QUIZ_PRED_COUNTDOWN_TIME) + MIN_QUIZ_PRED_COUNTDOWN_TIME;
        long convertRandomTime_to_millis = (randomQuizCountdownTime * 1000) * 60;
        return convertRandomTime_to_millis;
    }
    private long getRandomQuizPredict_RewardAmount() {
        int randomQuizReward = random.nextInt(MAX_GAME_QUIZ_REWARD_AMOUNT - MIN_GAME_QUIZ_REWARD_AMOUNT) + MIN_GAME_QUIZ_REWARD_AMOUNT;
//        long convertRandomToLong = randomQuizReward * ((7 - 5) * (4 + 1));
        return randomQuizReward;
    }

    private long getRandomGame_CountdownTime() {
        int randomGameCountdownTime = random.nextInt(MAX_GAME_COUNTDOWN_TIME - MIN_GAME_COUNTDOWN_TIME) + MIN_GAME_COUNTDOWN_TIME;
        long convertRandomTime_to_millis = (randomGameCountdownTime * 1000) * 60;
        return convertRandomTime_to_millis;
    }
    private long getRandomGame_RewardAmount() {
        int randomGameReward = random.nextInt(MAX_GAME_QUIZ_REWARD_AMOUNT - MIN_GAME_QUIZ_REWARD_AMOUNT) + MIN_GAME_QUIZ_REWARD_AMOUNT;
        return randomGameReward;
    }

    private void sendQuizPredData_to_timerDialog(int stringURL, int stringName,
                                                 int timerDialog_drawableIcon, int timerDialog_colorSpecific) {
        QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS = getRandomQuizPredict_CountdownTime();
        COUNTDOWN_TIME_IN_MINUTES = (int) (QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
        QUIZ_PRED_AMOUNT_OF_REWARD = getRandomQuizPredict_RewardAmount();
        QUIZ_PRED_AMOUNT_OF_REWARD_TEXT = "+ " + QUIZ_PRED_AMOUNT_OF_REWARD;
        QUREKA_PREDCHAMP_DEDICATED_URL = context.getString(stringURL);

        String bsDialogTitle = new StringBuilder()
                .append(context.getString(R.string.dedicated_timer_bsdialog_qureka_title_play))
                .append(" ").append(context.getString(stringName))
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_qureka_title_for))
                .append(" ").append(COUNTDOWN_TIME_IN_MINUTES)
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_qureka_title_X_min_and_get))
                .append(" ").append(QUIZ_PRED_AMOUNT_OF_REWARD)
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_qureka_title_coins)).toString();

        showTimerBsDialog(1, timerDialog_drawableIcon,
                bsDialogTitle, getResources().getColor(R.color.white));
    }

    private void sendGzopMglData_to_timerDialog(int stringURL, int stringName,
                                                int timerDialog_drawableIcon, int timerDialog_colorSpecific) {
        GAME_COUNTDOWN_TIME_IN_MILLIS = getRandomGame_CountdownTime();
        COUNTDOWN_TIME_IN_MINUTES = (int) (GAME_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
        GAME_AMOUNT_OF_REWARD = getRandomGame_RewardAmount();
        GAME_AMOUNT_OF_REWARD_TEXT = new StringBuilder().append("+ ").append(GAME_AMOUNT_OF_REWARD).toString();
        if (gamezopClicked) {
            String userSpecificGameUrl = new StringBuffer()
                    .append(context.getString(stringURL))
                    .append("&sub=")
                    .append(loggedInUser.getAuthUid()).toString();
            GZOP_MGL_DEDICATED_URL = userSpecificGameUrl;
        } else {
            GZOP_MGL_DEDICATED_URL = context.getString(stringURL);
        }

        String bsDialogTitle = new StringBuilder()
                .append(context.getString(R.string.dedicated_timer_bsdialog_game_title_play))
                .append(" ").append(context.getString(stringName))
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_game_title_for))
                .append(" ").append(COUNTDOWN_TIME_IN_MINUTES)
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_game_title_X_min_and_get))
                .append(" ").append(GAME_AMOUNT_OF_REWARD)
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_game_title_coins)).toString();

        showTimerBsDialog(3, timerDialog_drawableIcon,
                bsDialogTitle, getResources().getColor(R.color.white));
    }

    private void openCCT_URL(int QU1_PC1_GZ3_MGL3) {
        String cct_URL = null;
        if (QU1_PC1_GZ3_MGL3 == 1) {
            cct_URL = QUREKA_PREDCHAMP_DEDICATED_URL;
            cctToolbarColor = context.getResources().getColor(R.color.darker_blue_app_theme);
        }

        if (QU1_PC1_GZ3_MGL3 == 3) {
            cct_URL = GZOP_MGL_DEDICATED_URL;
            cctToolbarColor = context.getResources().getColor(R.color.darker_blue_app_theme);
        }

        CustomTabColorSchemeParams setCCTBarColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(cctToolbarColor)
                .build();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(setCCTBarColors)
                .build();
        customTabsIntent.launchUrl(context, Uri.parse(cct_URL));
    }

    private void showTimerBsDialog(int QU1_PC1_GZ3_MGL3, int timerDialog_drawable_int,
                                   String timerDialog_dedi_title, int timerDialog_dedi_title_color) {
        BottomSheetDialog timerBsDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialog);
        timerBsDialog.setContentView(R.layout.layout_bsdialog_game_quiz_url_timer);
        timerBsDialog.getDismissWithAnimation();
        timerBsDialog.setCancelable(false);
        timerBsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timerBsDialog.show();

        FrameLayout dedURL_design_frame = timerBsDialog.findViewById(R.id.dedicated_url_design_frame);

        FrameLayout timer_stop_btn = timerBsDialog.findViewById(R.id.common_url_timer_stop_btn);
        TextView timer_stop_text = timerBsDialog.findViewById(R.id.common_url_timer_stop_text);
        FrameLayout timer_continue_btn = timerBsDialog.findViewById(R.id.common_url_timer_continue_btn);
        TextView timer_continue_text = timerBsDialog.findViewById(R.id.common_url_timer_continue_text);

        ImageView timer_bdDialog_icon = timerBsDialog.findViewById(R.id.timer_bsDialog_icon);
        TextView timer_bdDialog_title = timerBsDialog.findViewById(R.id.timer_bsDialog_title);
        TextView timer_text = timerBsDialog.findViewById(R.id.dedicated_url_timer_tv);

        /** Common for all **/
        timer_text.startAnimation(blinking);
        timer_text.setText(new StringBuffer()
                .append(COUNTDOWN_TIME_IN_MINUTES)
                .append(" ")
                .append(context.getString(R.string.common_timer_bsdialog_timer_min_text))
                .append(" 00 ")
                .append(context.getString(R.string.common_timer_bsdialog_timer_sec_text)).toString());

        if (QU1_PC1_GZ3_MGL3 == 1) {
            mTimeLeft_in_millis = QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS;
            timer_bdDialog_icon.setImageResource(timerDialog_drawable_int);
            timer_bdDialog_title.setText(timerDialog_dedi_title);
            timer_bdDialog_title.setTextColor(timerDialog_dedi_title_color);
            timer_text.setTextColor(timerDialog_dedi_title_color);
        }

        if (QU1_PC1_GZ3_MGL3 == 3) {
            mTimeLeft_in_millis = GAME_COUNTDOWN_TIME_IN_MILLIS;
            timer_bdDialog_icon.setImageResource(timerDialog_drawable_int);
            timer_bdDialog_title.setText(timerDialog_dedi_title);
            timer_bdDialog_title.setTextColor(timerDialog_dedi_title_color);
            timer_text.setTextColor(timerDialog_dedi_title_color);
        }

        timer_stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_stop_text.startAnimation(scaleDown);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timerBsDialog.dismiss();
                        resetCommonTimer(QU1_PC1_GZ3_MGL3);
                    }
                }, 100);
            }
        });

        timer_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerRunning = true;
                timer_continue_text.startAnimation(scaleDown);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openCCT_URL(QU1_PC1_GZ3_MGL3);

                        startCommonTimer(v, QU1_PC1_GZ3_MGL3, timerBsDialog, timer_text);
                    }
                }, 100);
            }
        });
    }

    private void startCommonTimer(View v, int comQU1_comGZ2_dedGZ3, BottomSheetDialog gamezopBsDialog, TextView gz_timer_text) {
        mCountDownTimer = new CountDownTimer(mTimeLeft_in_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft_in_millis = millisUntilFinished;
                int minutes = (int) (mTimeLeft_in_millis / 1000) / 60;
                int seconds = (int) (mTimeLeft_in_millis / 1000) % 60;

//                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                gz_timer_text.setText(timeLeftFormatted);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gz_timer_text.setText(new StringBuffer()
                                .append(minutes).append(" ")
                                .append(context.getString(R.string.common_timer_bsdialog_timer_min_text))
                                .append(" ")
                                .append(seconds).append(" ")
                                .append(context.getString(R.string.common_timer_bsdialog_timer_sec_text)).toString());
                    }
                }, 2000);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                resetCommonTimer(comQU1_comGZ2_dedGZ3);
                gamezopBsDialog.dismiss();
                updateScoreAutomatically_OnResume = false;
                showAddRewardDialog(v, comQU1_comGZ2_dedGZ3);
            }
        }.start();
    }

    private void pauseCommonTimer() {
        if (mTimerRunning) {
            mCountDownTimer.cancel();
            mTimerRunning = false;
        }
    }

    private void resetCommonTimer(int QU1_PC1_GZ3_MGL3) {
        if (QU1_PC1_GZ3_MGL3 == 1) {
            mTimeLeft_in_millis = QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS;
        }
        if (QU1_PC1_GZ3_MGL3 == 3) {
            mTimeLeft_in_millis = GAME_COUNTDOWN_TIME_IN_MILLIS;
        }
    }

    private void showAddRewardDialog(View rootView1, int QU1_PC1_GZ3_MGL3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_add_reward_game_home,
                (ConstraintLayout) getActivity().findViewById(R.id.constraint_dialog_add_reward_game_home));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog dialogAddReward = builder.create();
        dialogAddReward.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAddReward.show();

        ImageView dialogRewardIcon = rootView1.findViewById(R.id.dialog_add_reward_icon);
        TextView dialogRewardMsg = rootView1.findViewById(R.id.dialog_add_reward_msg);
//        FrameLayout dialogX2Btn = rootView1.findViewById(R.id.dialog_add_reward_btn);
        ImageView dialogCloseBtnIV = rootView1.findViewById(R.id.dialog_add_reward_close_btn);
//        TextView dialogCloseBtnTimer = rootView1.findViewById(R.id.dialog_add_reward_close_timer_tv);

//        TapdaqNativeLargeLayout nativeAdLayout = dialogAddReward.findViewById(R.id.native_ad_container);
//        nativeAdLayout.setVisibility(View.GONE);

        dialogRewardIcon.setImageResource(R.drawable.ic_coin_dollar);
        if (QU1_PC1_GZ3_MGL3 == 1) {
            dialogRewardMsg.setText(QUIZ_PRED_AMOUNT_OF_REWARD_TEXT);
        }
        if (QU1_PC1_GZ3_MGL3 == 3) {
            dialogRewardMsg.setText(GAME_AMOUNT_OF_REWARD_TEXT);
        }
//        dialogX2Btn.setVisibility(View.GONE);
        dialogCloseBtnIV.setVisibility(View.VISIBLE);
//        dialogCloseBtnTimer.setVisibility(View.GONE);

        dialogCloseBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTokenInSQL(QU1_PC1_GZ3_MGL3);
//                updateUI_CashCoinWallet();
                ((MainActivity)getActivity()).showMainAct_interstitialFrom();
                dialogAddReward.dismiss();
            }
        });
    }

    private void addTokenInSQL(int QU1_PC1_GZ3_MGL3) {
        if (QU1_PC1_GZ3_MGL3 == 1) {
            firebaseDataService.updateUserCoin(true, binding.homeUserTotalCoinBalance, QUIZ_PRED_AMOUNT_OF_REWARD);
        }
        if (QU1_PC1_GZ3_MGL3 == 3) {
            firebaseDataService.updateUserCoin(true, binding.homeUserTotalCoinBalance, GAME_AMOUNT_OF_REWARD);
        }

//        AllGameInOne allGameInOne = UserContext.getAllGameInOneMapByGameId(sqlTotalCashCoinCOL);
//
//        long coinsBeforeReward = Long.parseLong(allGameInOne.getCoins());
//        long coinsAfterReward = coinsBeforeReward;
//        if (QU1_PC1_GZ3_MGL3 == 1) {
//            coinsAfterReward = coinsBeforeReward + QUIZ_PRED_AMOUNT_OF_REWARD;
//        }
//        if (QU1_PC1_GZ3_MGL3 == 3) {
//            coinsAfterReward = coinsBeforeReward + GAME_AMOUNT_OF_REWARD;
//        }
//
//        allGameInOne.setCoins(String.valueOf(coinsAfterReward));
//        dbHelper.updateFreeAdGameChanceAndCoinsData(allGameInOne);
    }

//    /**
//     * Intertitial Ad Code
//     **/
//
//    private void loadInterstitialFrom() {
//        if (isTapdaqInterstitialEnabled || isAdmobInterstitialEnabled) {
//            if (isTapdaqIntersVideoReady() || admobInterstitial != null) {
//                // Do nothing
//                // Do not load ad again
//            } else {
//                if (isTapdaqInterstitialEnabled) {
//                    loadTapdaqIntersVideo();
//                }
//
//                if (isAdmobInterstitialEnabled) {
//                    loadAdmobInterstitial();
//                }
//            }
//        }
//    }
//
//    private void showInterstitialFrom() {
//        if (isTapdaqInterstitialEnabled || isAdmobInterstitialEnabled) {
//            if (isTapdaqIntersVideoReady()) {
//                showTapdaqIntersVideo();
//            }
//
//            if (admobInterstitial != null) {
//                showAdmobInterstitial();
//            }
//        }
//    }
//
//    /**
//     * Tapdaq Intertitial
//     **/
//
//    private void loadTapdaqIntersVideo() {
////        showProgressDialog();
//        Tapdaq.getInstance().loadVideo(getActivity(), TAPDAQ_INTERSTITIAL_AD_UNIT_ID, new TapdaqInterstitialVideoListener());
//    }
//
//    private boolean isTapdaqIntersVideoReady() {
//        boolean isReady = Tapdaq.getInstance().isVideoReady(getActivity(), TAPDAQ_INTERSTITIAL_AD_UNIT_ID);
//        return isReady;
//    }
//
//    private class TapdaqInterstitialVideoListener extends TMAdListener {
//        @Override
//        public void didLoad() {
//            super.didLoad();
////            progressDialog.dismiss();
//        }
//
//        @Override
//        public void didFailToLoad(TMAdError error) {
//            super.didFailToLoad(error);
////            progressDialog.dismiss();
//        }
//
//        @Override
//        public void didClose() {
//            loadTapdaqIntersVideo();
//            super.didClose();
//        }
//    }
//
//    private void showTapdaqIntersVideo() {
//        Tapdaq.getInstance().showVideo(getActivity(), TAPDAQ_INTERSTITIAL_AD_UNIT_ID, new TapdaqInterstitialVideoListener());
//    }
//
//    /**
//     * Admob Interstitial
//     **/
//
//    private void loadAdmobInterstitial() {
////        showProgressDialog();
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(context, ADMOB_INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                admobInterstitial = interstitialAd;
////                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                admobInterstitial = null;
////                Log.e(TAG, "Admob Ad Failed to LOAD" + loadAdError);
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
//                }
//            });
//            admobInterstitial.show(getActivity());
//        }
//    }




    /**
     * * Daily Free Reward Code
     **/




    private void showDailyRewardDialog(View rootView1) {
        BottomSheetDialog rewardDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialog);
        rewardDialog.setContentView(R.layout.layout_dialog_daily_rewards);
        rewardDialog.getDismissWithAnimation();
        rewardDialog.setCancelable(true);
        rewardDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        rewardDialog.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        rootView1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_daily_rewards,
//                rootView1.findViewById(R.id.constraint_dialog_daily_rewards));
//        builder.setView(rootView1);
//        builder.setCancelable(false);
//
//        AlertDialog rewardDialog = builder.create();

        loadRewardedAdFrom(rootView1);

//        ImageView closeButton = rootView1.findViewById(R.id.dialy_reward_close_button);
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rewardDialog.dismiss();
//            }
//        });

        TextView timer_reward_10 = rewardDialog.findViewById(R.id.timer_reward_10);
        TextView timer_reward_25 = rewardDialog.findViewById(R.id.timer_reward_25);
        TextView timer_reward_50 = rewardDialog.findViewById(R.id.timer_reward_50);
        TextView timer_reward_75 = rewardDialog.findViewById(R.id.timer_reward_75);
        TextView timer_reward_100 = rewardDialog.findViewById(R.id.timer_reward_100);

        FrameLayout free_10_coin_button = rewardDialog.findViewById(R.id.free_10_coin_button);
        FrameLayout free_25_coin_button = rewardDialog.findViewById(R.id.free_25_coin_button);
        FrameLayout free_50_coin_button = rewardDialog.findViewById(R.id.free_50_coin_button);
        FrameLayout free_75_coin_button = rewardDialog.findViewById(R.id.free_75_coin_button);
        FrameLayout free_100_coin_button = rewardDialog.findViewById(R.id.free_100_coin_button);


        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        long previous10Time = readSPref.getLong(Constants.SP_10_BUTTON_TIME, 0);
        long previous25Time = readSPref.getLong(Constants.SP_25_BUTTON_TIME, 0);
        long previous50Time = readSPref.getLong(Constants.SP_50_BUTTON_TIME, 0);
        long previous75Time = readSPref.getLong(Constants.SP_75_BUTTON_TIME, 0);
        long previous100Time = readSPref.getLong(Constants.SP_100_BUTTON_TIME, 0);

/***** All Buttons timer Pre-Check *****/
        if (previous10Time != 0) {
            timerRewardButton(1, free_10_coin_button, timer_reward_10, previous10Time, currentTime);
        } else {
            timer_reward_10.setText(context.getString(R.string.home_frag_cV_reward_available));
        }

        if (previous25Time != 0) {
            timerRewardButton(2, free_25_coin_button, timer_reward_25, previous25Time, currentTime);
        } else {
            timer_reward_25.setText(context.getString(R.string.home_frag_cV_reward_available));
        }

        if (previous50Time != 0) {
            timerRewardButton(4, free_50_coin_button, timer_reward_50, previous50Time, currentTime);
        } else {
            timer_reward_50.setText(context.getString(R.string.home_frag_cV_reward_available));
        }

        if (previous75Time != 0) {
            timerRewardButton(8, free_75_coin_button, timer_reward_75, previous75Time, currentTime);
        } else {
            timer_reward_75.setText(context.getString(R.string.home_frag_cV_reward_available));
        }

        if (previous100Time != 0) {
            timerRewardButton(12, free_100_coin_button, timer_reward_100, previous100Time, currentTime);
        } else {
            timer_reward_100.setText(context.getString(R.string.home_frag_cV_reward_available));
        }

/***** 10 Coins Button *****/
        long diffTimeLess1Hours = currentTime - previous10Time;
        if (diffTimeLess1Hours >= 3600000) {
            free_10_coin_button.setEnabled(true);
        } else {
            free_10_coin_button.setEnabled(false);
        }

        free_10_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateTime = new Date(System.currentTimeMillis());
                long savedReward10dateTime = dateTime.getTime();
                editorSPref.putLong(Constants.SP_10_BUTTON_TIME, savedReward10dateTime);
                editorSPref.commit();
                free_10_coin_button.setEnabled(false);

                long freeRewardAmount = 10;  // 25
                freeRewardUpdateToSQL(freeRewardAmount);
//                updateUICoinsWallet();
//                updateHomeUI_CashCoinWallet();

                updateUIrewardTimer();

                timerRewardButton(1, free_10_coin_button, timer_reward_10, savedReward10dateTime, currentTime);

            }
        });

/***** 25 Coins Button *****/
        long diffTimeLess2Hours = currentTime - previous25Time;
        if (diffTimeLess2Hours >= 7200000) {
            free_25_coin_button.setEnabled(true);
        } else {
            free_25_coin_button.setEnabled(false);
        }

        free_25_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPrefName = Constants.SP_25_BUTTON_TIME;
                long freeRewardAmount = 25;  //125

                showRewardedAdFrom(free_25_coin_button, sPrefName, freeRewardAmount, v, timer_reward_25, currentTime);

            }
        });

/***** 50 Coins Button *****/
        long diffTimeLess4Hours = currentTime - previous50Time;
        if (diffTimeLess4Hours >= 14400000) {
            free_50_coin_button.setEnabled(true);
        } else {
            free_50_coin_button.setEnabled(false);
        }

        free_50_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPrefName = Constants.SP_50_BUTTON_TIME;
                long freeRewardAmount = 50;  // 250

                showRewardedAdFrom(free_50_coin_button, sPrefName, freeRewardAmount, v, timer_reward_50, currentTime);
            }
        });

/***** 75 Coins Button *****/
        long diffTimeLess8Hours = currentTime - previous75Time;
        if (diffTimeLess8Hours >= 28800000) {
            free_75_coin_button.setEnabled(true);
        } else {
            free_75_coin_button.setEnabled(false);
        }

        free_75_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPrefName = Constants.SP_75_BUTTON_TIME;
                long freeRewardAmount = 75;  // 375

                showRewardedAdFrom(free_75_coin_button, sPrefName, freeRewardAmount, v, timer_reward_75, currentTime);
            }
        });

/***** 100 Coins Button *****/
        long diffTimeLess12Hours = currentTime - previous100Time;
        if (diffTimeLess12Hours >= 43200000) {
            free_100_coin_button.setEnabled(true);
        } else {
            free_100_coin_button.setEnabled(false);
        }

        free_100_coin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPrefName = Constants.SP_100_BUTTON_TIME;
                long freeRewardAmount = 100;  // 500

                showRewardedAdFrom(free_100_coin_button, sPrefName, freeRewardAmount, v, timer_reward_100, currentTime);
            }
        });

    }


    /***** TIMER - 10 Coins Button *****/
    private void timerRewardButton(int increaseInTime, FrameLayout free_coin_button, TextView timer_reward_tv, long savedRewardTime, long currentTime) {
        Calendar calFutureTime = Calendar.getInstance();
        calFutureTime.setTimeInMillis(savedRewardTime);
        calFutureTime.add(Calendar.HOUR_OF_DAY, increaseInTime);
        calFutureTime.set(Calendar.SECOND, 0);

        long futureTime = calFutureTime.getTimeInMillis();
        long leftTime = (futureTime - currentTime);

        if (currentTime <= futureTime) {
            timer = new CountDownTimer(leftTime, 1000) {
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

                    timer_reward_tv.setText(new StringBuilder()
                            .append(elapsedHours).append("h : ")
                            .append(elapsedMinutes).append("m : ")
                            .append(elapsedSeconds).append("s").toString());
                }

                @Override
                public void onFinish() {
                    timer_reward_tv.setText(context.getString(R.string.home_frag_cV_reward_available));
                    free_coin_button.setEnabled(true);
                }
            }.start();
        } else {
            timer_reward_tv.setText(context.getString(R.string.home_frag_cV_reward_available));
            free_coin_button.setEnabled(true);
        }
    }


    /***** HOME TIMER - 10 Coins Button *****/
    private void homeTimer1Hours(long homePrevious10Time, long homeCurrentTime) {
        Calendar futureTime = Calendar.getInstance();
        futureTime.setTimeInMillis(homePrevious10Time);
        futureTime.add(Calendar.HOUR_OF_DAY, 1);
        futureTime.set(Calendar.SECOND, 0);
        long leftTime = (futureTime.getTimeInMillis() - homeCurrentTime);

        timer = new CountDownTimer(leftTime, 1000) {
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

                binding.homeDailyRewardTimer.setText(new StringBuilder()
                        .append(elapsedHours).append("h : ")
                        .append(elapsedMinutes).append("m : ")
                        .append(elapsedSeconds).append("s").toString());
            }

            @Override
            public void onFinish() {
                binding.homeDailyRewardTimer.setText(context.getString(R.string.home_frag_cV_reward_available));
            }
        }.start();
    }


    /**** FREE Reward update to SQL ****/
    private void freeRewardUpdateToSQL(long freeRewardAmount) {
        firebaseDataService.updateUserCoin(true, binding.homeUserTotalCoinBalance, freeRewardAmount);

//        AllGameInOne totalCoinsModel = UserContext.getAllGameInOneMapByGameId(sqlTotalCashCoinCOL);
//
//        long existingTotalCoins = Long.parseLong(totalCoinsModel.getCoins());
//        long finalTotalCoins = existingTotalCoins + freeRewardAmount;
//        totalCoinsModel.setCoins(String.valueOf(finalTotalCoins));
//
//        dbHelper.updateFreeAdGameChanceAndCoinsData(totalCoinsModel);
////        updateUI_CashCoinWallet();
    }
    /**** FREE Reward update to SQL ****/


    private void show_free_addCoinsDialog(View rootView1, long rewardAmount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        rootView1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_add_reward_game_home,
                (ConstraintLayout) rootView1.findViewById(R.id.constraint_dialog_add_reward_game_home));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog addCoinsDialog = builder.create();

        addCoinsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addCoinsDialog.show();

        TextView youWonMessage = rootView1.findViewById(R.id.dialog_add_reward_msg);
        ImageView closeBtnIV = rootView1.findViewById(R.id.dialog_add_reward_close_btn);

        youWonMessage.setText(new StringBuilder().append("+ ").append(rewardAmount).toString());

        closeBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeRewardUpdateToSQL(rewardAmount);
//                updateHomeUI_CashCoinWallet();
                addCoinsDialog.dismiss();
            }
        });
    }

    private void showProgressDialog(View rootView1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        rootView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_progress_bar_white_short,
                (ConstraintLayout) rootView1.findViewById(R.id.constraint_dialog_progress));
        builder.setView(rootView1);
        builder.setCancelable(false);

        progressDialog = builder.create();

        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }

    private void loadRewardedAdFrom(View rootView1) {
        if (isAppLovinRewardedEnabled || isAppodealRewardedEnabled) {
//        if (isAdmobRewardedEnabled || isTapdaqRewardedEnabled || isAppLovinRewardedEnabled || isUnityRewardedEnabled) {
            if (isAppLovinRewardedReady() || Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
//            if (admobRewarded != null || isTapdaqRewardedReady() || isAppLovinRewardedReady() || isUnityRewardedReady) {
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

    private void showRewardedAdFrom(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
        if (isAppLovinRewardedEnabled || isAppodealRewardedEnabled) {
//        if (isAdmobRewardedEnabled || isTapdaqRewardedEnabled || isAppLovinRewardedEnabled || isUnityRewardedEnabled) {
            if (isAppLovinRewardedReady() || Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
//            if (admobRewarded != null || isTapdaqRewardedReady() || isAppLovinRewardedReady() || isUnityRewardedReady) {

//                if (isTapdaqRewardedReady()) {
//                    show_TapdaqRewarded(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
//                }
//                if (admobRewarded != null) {
//                    show_AdmobRewarded(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
//                }
                if (isAppLovinRewardedReady()) {
                    show_AppLovinRewarded(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
                }
                if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
                    showAppodealRewarded(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
                }
//                if (isUnityRewardedReady) {
//                    show_UnityRewarded(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
//                }
            } else {
                showCustomToast(v, context.getString(R.string.game_no_ad_available));
            }
        } else {
            showProgressDialog(v);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCustomToast(v, context.getString(R.string.game_no_ad_available));
                    ifRewardedAdNotEnabled(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
                    progressDialog.dismiss();
                }
            }, 2000);
        }
    }

    /**
     * Tapdaq Rewarded
     **/
//    private void loadTapdaqRewarded() {
//        showProgressDialog(rootView);
//        Tapdaq.getInstance().loadRewardedVideo(getActivity(), TAPDAQ_REWARDED_ID, new TMAdListener() {
//            @Override
//            public void didLoad() {
//                super.didLoad();
//                progressDialog.dismiss();
////                Log.e("Home Frag", "Tapdaq Rewarded Ad loaded");
//            }
//            @Override
//            public void didFailToLoad(TMAdError error) {
//                super.didFailToLoad(error);
//                progressDialog.dismiss();
////                Log.e("Home Frag", "Tapdaq Rewarded FAILED - " + error);
////                Toast.makeText(context, "Home Frag - " + "Tapdaq Rewarded FAILED - " + error, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//    private boolean isTapdaqRewardedReady() {
//        boolean isReady = Tapdaq.getInstance().isRewardedVideoReady(getActivity(), TAPDAQ_REWARDED_ID);
//        return isReady;
//    }
//
//    private void show_TapdaqRewarded(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
//        Tapdaq.getInstance().showRewardedVideo(getActivity(), TAPDAQ_REWARDED_ID, new TMAdListener() {
//            @Override
//            public void didClose() {
//                super.didClose();
//                loadTapdaqRewarded();
//            }
//            @Override
//            public void didVerify(TDReward reward) {
//                super.didVerify(reward);
//                rewardUserAfterAd(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
//            }
//        });
//    }
//
//    private void loadingAdmobRewarded() {
//        showProgressDialog(rootView);
//        if (admobRewarded == null) {
//            AdRequest adRequest = new AdRequest.Builder().build();
//            RewardedAd.load(getContext(), ADMOB_REWARDED_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
//                @Override
//                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                    admobRewarded = null;
//                    progressDialog.dismiss();
//                }
//                @Override
//                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                    admobRewarded = rewardedAd;
//                    progressDialog.dismiss();
//                }
//            });
//        } else {
//            progressDialog.dismiss();
//        }
//    }
//
//    private void show_AdmobRewarded(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
//        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
//            @Override
//            public void onAdShowedFullScreenContent() {
//            }
//            @Override
//            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
////                Log.d(TAG, "onAdFailedToShowFullScreenContent");
//                admobRewarded = null;
//            }
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                admobRewarded = null;
//                loadingAdmobRewarded();
//            }
//        };
//        admobRewarded.setFullScreenContentCallback(fullScreenContentCallback);
//
//        Activity activityContext = getActivity();
//        admobRewarded.show(activityContext, new OnUserEarnedRewardListener() {
//            @Override
//            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                int rewardAmount = rewardItem.getAmount();
//                String rewardType = rewardItem.getType();
//                rewardUserAfterAd(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
//            }
//        });
//    }

    /**
     * AppLovin Rewarded
     **/
    private void loadAppLovinRewarded() {
//         Initialized in onCreate() method of this class
//        applovinRewarded.setListener(new AppLovinRewardedAdListener());
        applovinRewarded.loadAd();
    }

    private boolean isAppLovinRewardedReady() {
        // Initialize MaxInterstitialAd in onCreate() method before checking .isReady()
        return applovinRewarded.isReady();
    }

    private void show_AppLovinRewarded(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
        // listener already attached in load() method
        applovinRewarded.setListener(new MaxRewardedAdListener() {
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
                rewardUserAfterAd(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
            }
        });
        applovinRewarded.showAd();
    }

//    private class AppLovinRewardedAdListener implements MaxRewardedAdListener {
//        @Override
//        public void onAdLoaded(MaxAd ad) {
////            Log.e("Flip activity - ", "aL REWARDED ad loaded = ");
//        }
//        @Override
//        public void onAdDisplayed(MaxAd ad) {
//        }
//        @Override
//        public void onAdHidden(MaxAd ad) {
//            // Ad closed, pre-load next ad
//            loadAppLovinRewarded();
////            showDialogAfterInterstitial(true, false, 0);
//        }
//        @Override
//        public void onAdClicked(MaxAd ad) {
//        }
//        @Override
//        public void onAdLoadFailed(String adUnitId, MaxError error) {
////            Log.e("Flip activity - ", "aL REWARDED ad Failed to load = " + error.getMessage());
//        }
//        @Override
//        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
//        }
//        @Override
//        public void onRewardedVideoStarted(MaxAd ad) {
//        }
//        @Override
//        public void onRewardedVideoCompleted(MaxAd ad) {
//        }
//        @Override
//        public void onUserRewarded(MaxAd ad, MaxReward reward) {
//            rewardUserAfterAd(free_coin_button, sPrefName, freeRewardAmount, v, timer_reward_tv, currentTime);
//        }
//    }

    /**
     * Appodeal Rewarded
     **/
    private void loadAppodealRewarded() {
        Appodeal.cache(getActivity(), Appodeal.REWARDED_VIDEO);
    }
    private void showAppodealRewarded(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
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
                rewardUserAfterAd(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
            }
            @Override
            public void onRewardedVideoClosed(boolean finished) {
                loadAppodealRewarded();
            }
            @Override
            public void onRewardedVideoExpired() {
            }
        });

        Appodeal.show(getActivity(), Appodeal.REWARDED_VIDEO);
    }

    /**
     * Unity Rewarded
     **/
//    private void loadUnityRewarded() {
//        showProgressDialog(rootView);
//        unityRewarded = new com.unity3d.mediation.RewardedAd(getActivity(), UNITY_REWARDED_ID);
//        IRewardedAdLoadListener unityRewardedLoadListener = new IRewardedAdLoadListener() {
//            @Override
//            public void onRewardedLoaded(com.unity3d.mediation.RewardedAd rewardedAd) {
//                progressDialog.dismiss();
//                isUnityRewardedReady = true;
//            }
//            @Override
//            public void onRewardedFailedLoad(com.unity3d.mediation.RewardedAd rewardedAd, LoadError loadError, String s) {
//                progressDialog.dismiss();
//                isUnityRewardedReady = false;
//            }
//        };
//        unityRewarded.load(unityRewardedLoadListener);
//    }
//
//    private void show_UnityRewarded(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
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
//                rewardUserAfterAd(free_coin_button, sPrefName, dailyRewardAmount, v, timer_reward_tv, currentTime);
//            }
//        };
//        unityRewarded.show(unityRewardedShowListener);
//    }



    private void rewardUserAfterAd(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
        Date dateTime = new Date(System.currentTimeMillis());
        long savedReward_time = dateTime.getTime();
        editorSPref.putLong(sPrefName, savedReward_time);
        editorSPref.commit();
        free_coin_button.setEnabled(false);

        updateScoreAutomatically_OnResume = false;

        show_free_addCoinsDialog(v, dailyRewardAmount);

        // 125
        if (dailyRewardAmount == 25) {
            setNotifAlarm(savedReward_time);
            timerRewardButton(2, free_coin_button, timer_reward_tv, savedReward_time, currentTime);
        }
        // 250
        if (dailyRewardAmount == 50) {
            timerRewardButton(4, free_coin_button, timer_reward_tv, savedReward_time, currentTime);
        }
        // 375
        if (dailyRewardAmount == 75) {
            timerRewardButton(8, free_coin_button, timer_reward_tv, savedReward_time, currentTime);
        }
        // 500
        if (dailyRewardAmount == 100) {
            timerRewardButton(12, free_coin_button, timer_reward_tv, savedReward_time, currentTime);
        }
    }

    private void ifRewardedAdNotEnabled(FrameLayout free_coin_button, String sPrefName, long dailyRewardAmount, View v, TextView timer_reward_tv, long currentTime) {
        Date dateTime = new Date(System.currentTimeMillis());
        long savingReward_time = dateTime.getTime();
        editorSPref.putLong(sPrefName, savingReward_time);
        editorSPref.commit();
        free_coin_button.setEnabled(false);

        freeRewardUpdateToSQL(dailyRewardAmount);

        show_free_addCoinsDialog(v, dailyRewardAmount);

        if (dailyRewardAmount == 25) {
            timerRewardButton(2, free_coin_button, timer_reward_tv, savingReward_time, currentTime);
        }
        if (dailyRewardAmount == 50) {
            timerRewardButton(4, free_coin_button, timer_reward_tv, savingReward_time, currentTime);
        }
        if (dailyRewardAmount == 75) {
            timerRewardButton(8, free_coin_button, timer_reward_tv, savingReward_time, currentTime);
        }
        if (dailyRewardAmount == 100) {
            timerRewardButton(12, free_coin_button, timer_reward_tv, savingReward_time, currentTime);
        }
    }

    private void showNoInternetDialog(View rootView1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        rootView1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_no_internet,
                (ConstraintLayout) rootView1.findViewById(R.id.constraint_dialog_no_internet));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        Button reloadActivityButton = rootView1.findViewById(R.id.reload_activity_button);
        Button exitActivityButton = rootView1.findViewById(R.id.exit_button);

        reloadActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().recreate();
                alertDialog.dismiss();
            }
        });

        exitActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                System.exit(0);
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

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

    private void setNotifAlarm(long savedReward_time) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), DailyRewardAlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), Constants.DAILY_REWARD_DEFAULT_REQUEST_CODE, intent, 0);

        Calendar calFutureTime = Calendar.getInstance();
        calFutureTime.setTimeInMillis(savedReward_time);
        calFutureTime.add(Calendar.HOUR_OF_DAY, 2);
        calFutureTime.set(Calendar.SECOND, 0);

        long notifAlarmTime = calFutureTime.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notifAlarmTime, pendingIntent);
    }


    private void getAndSetChanceNotif() {
        int normalSpinChance = Integer.parseInt(firebaseDataService.getAllGameInOneMapByGameId(Game.NORMAL_SPIN.getId()).getChanceLeft());
        int normalScratchChance = Integer.parseInt(firebaseDataService.getAllGameInOneMapByGameId(Game.NORMAL_SCRATCH.getId()).getChanceLeft());
        int normalFlipChance = Integer.parseInt(firebaseDataService.getAllGameInOneMapByGameId(Game.NORMAL_FLIP.getId()).getChanceLeft());
        binding.normalSpinNotifTv.setText(String.valueOf(normalSpinChance));
        binding.normalScratchNotifTv.setText(String.valueOf(normalScratchChance));
        binding.normalFlipNotifTv.setText(String.valueOf(normalFlipChance));

//        int normalSpinChance = Integer.parseInt(UserContext.getAllGameInOneMapByGameId(Game.NORMAL_SPIN.getId()).getChanceLeft());
//        binding.normalSpinNotifTv.setText(String.valueOf(normalSpinChance));
//        int normalScratchChance = Integer.parseInt(UserContext.getAllGameInOneMapByGameId(Game.NORMAL_SCRATCH.getId()).getChanceLeft());
//        binding.normalScratchNotifTv.setText(String.valueOf(normalScratchChance));
//        int normalFlipChance = Integer.parseInt(UserContext.getAllGameInOneMapByGameId(Game.NORMAL_FLIP.getId()).getChanceLeft());
//        binding.normalFlipNotifTv.setText(String.valueOf(normalFlipChance));

        if (normalSpinChance >= 11) {
            binding.normalSpinNotifTv.setTextColor(getResources().getColor(R.color.white));
            binding.normalSpinNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_green));
        } else if (normalSpinChance <= 10 && normalSpinChance >= 4) {
            binding.normalSpinNotifTv.setTextColor(getResources().getColor(R.color.black));
            binding.normalSpinNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_yellow));
        } else if (normalSpinChance <= 3) {
            binding.normalSpinNotifTv.setTextColor(getResources().getColor(R.color.white));
            binding.normalSpinNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_red));
        } else if (normalSpinChance == 0) {

        }

        if (normalScratchChance >= 11) {
            binding.normalScratchNotifTv.setTextColor(getResources().getColor(R.color.white));
            binding.normalScratchNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_green));
        } else if (normalScratchChance <= 10 && normalScratchChance >= 4) {
            binding.normalScratchNotifTv.setTextColor(getResources().getColor(R.color.black));
            binding.normalScratchNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_yellow));
        } else if (normalScratchChance <= 3) {
            binding.normalScratchNotifTv.setTextColor(getResources().getColor(R.color.white));
            binding.normalScratchNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_red));
        } else if (normalScratchChance == 0) {

        }

        if (normalFlipChance >= 11) {
            binding.normalFlipNotifTv.setTextColor(getResources().getColor(R.color.white));
            binding.normalFlipNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_green));
        } else if (normalFlipChance <= 10 && normalFlipChance >= 4) {
            binding.normalFlipNotifTv.setTextColor(getResources().getColor(R.color.black));
            binding.normalFlipNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_yellow));
        } else if (normalFlipChance <= 3) {
            binding.normalFlipNotifTv.setTextColor(getResources().getColor(R.color.white));
            binding.normalFlipNotifFl.setBackground(getResources().getDrawable(R.drawable.bg_home_notif_game_chance_red));
        } else if (normalFlipChance == 0) {

        }
    }

    private void getAddClearTapjoyCurrencyBalance() {
        showProgressDialog(rootView);

        if (Tapjoy.isConnected()) {
            Tapjoy.getCurrencyBalance(new TJGetCurrencyBalanceListener() {
                @Override
                public void onGetCurrencyBalanceResponse(String currencyName, int tapjoyBalance) {
                //    Log.e(TAG, "getCurrencyBalance returned " + currencyName + " : " + tapjoyBalance);
                    TAPJOY_CURRENCY_BALANCE = tapjoyBalance;
                }
                @Override
                public void onGetCurrencyBalanceResponseFailure(String error) {
    //                Log.i("Tapjoy", "getCurrencyBalance error: " + error);
                }
            });
    //        return tapjoyCurrencyBalance;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                //    Log.e(TAG, "getCurrencyBalance returned " + "private int " + " : " + TAPJOY_CURRENCY_BALANCE);
                    if (TAPJOY_CURRENCY_BALANCE > 0) {
    //                    show_free_addCoinsDialog(rootView, TAPJOY_CURRENCY_BALANCE);
                        addOfferwallCoinDialog(rootView, TAPJOY_CURRENCY_BALANCE);
                    }
                }
            }, 3500);
        }
    }

    private void addOfferwallCoinDialog(View rootView1, long coinToBeAdded) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        rootView1 = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_add_reward_spin_flip_scratch,
                (ConstraintLayout) rootView1.findViewById(R.id.constraint_dialog_add_reward_game_home));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog addCoinsDialog = builder.create();
        addCoinsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        addCoinsDialog.show();

        TextView youWonMessage = rootView1.findViewById(R.id.dialog_after_interstitial_message);
        TextView timer = rootView1.findViewById(R.id.dialog_after_interstitial_timer_text);
        Button doubleCoinBtn = rootView1.findViewById(R.id.dialog_after_interstitial_double_reward_btn);
        FrameLayout claimCoins = rootView1.findViewById(R.id.dialog_after_interstitial_claim_reward_btn);
        TapdaqNativeLargeLayout nativeLargeLayout = rootView1.findViewById(R.id.native_ad_container);
        FrameLayout nativeAdFrameLayout = rootView1.findViewById(R.id.native_ad_frameLayout);

        youWonMessage.setText(new StringBuilder().append(" + ").append(coinToBeAdded).toString());
        youWonMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_dollar, 0, 0, 0);
        timer.setVisibility(View.GONE);
        doubleCoinBtn.setVisibility(View.GONE);
        claimCoins.setVisibility(View.VISIBLE);
        nativeLargeLayout.setVisibility(View.GONE);
        nativeAdFrameLayout.setVisibility(View.GONE);

        claimCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDataService.updateUserCoin(true, binding.homeUserTotalCoinBalance, coinToBeAdded);
                Tapjoy.spendCurrency(TAPJOY_CURRENCY_BALANCE, new TJSpendCurrencyListener() {
                    @Override
                    public void onSpendCurrencyResponse(String currencyName, int balance) {
//                        Log.e(TAG, "getCurrencyBalance deducted " + currencyName + " : " + balance);
                    }
                    @Override
                    public void onSpendCurrencyResponseFailure(String error) {
                    }
                });
                addCoinsDialog.dismiss();
            }
        });
    }

    private void runShinningEffect() {
        /** For Shining Effect on EARN CHANCE button **/
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startShineEffect();
                    }
                });
            }
        }, 2, 4, TimeUnit.SECONDS);
        /** For Shining Effect on EARN CHANCE button **/
    }

    private void startShineEffect() {
        Animation shiningAnimation = new TranslateAnimation(
                0,
                binding.homeFragOfferwallBtn.getWidth() + binding.homeFragShinningDrawImageView.getWidth(),
                0, 0);

        shiningAnimation.setDuration(800);
        shiningAnimation.setFillAfter(false);
        shiningAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        binding.homeFragShinningDrawImageView.startAnimation(shiningAnimation);
    }


//    public class ProgressTask extends AsyncTask<Void, Void, Boolean> {
//        /** application context. */
//        private Activity activity;
//        private ProgressDialog dialog;
//
//        public ProgressTask(Activity activity) {
//            this.activity = activity;
//            dialog = new ProgressDialog(context);
//        }
//
//        protected void onPreExecute() {
////            this.dialog.setMessage("Please wait");
////            this.dialog.show();
//            showProgressDialog(rootView);
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//        }
//
//        protected Boolean doInBackground(final Void... args) {
//            // HERE GOES YOUR BACKGROUND WORK
//
//        }
//    }


    private void showVirtualCurrencyEarnedDialog() {
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (admobBanner != null) {
//            admobBanner.pause();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (admobBanner != null) {
//            admobBanner.resume();
//        }
        pauseCommonTimer();
        if (updateScoreAutomatically_OnResume) {
            updateHomeUI_CashCoinWallet();
        } else {
            // This boolean will always be true,
            // it come false only when Game/Quiz timer finishes.
            // and become true as soon as score is updated.
        }
        getAndSetChanceNotif();
        getAddClearTapjoyCurrencyBalance();
        if (UserContext.getIsTapjoy_offerwall()) {
//        if (UserContext.getIsTapjoy_offerwall() || UserContext.getIsIronSourceOfferwall()) {
            runShinningEffect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (admobBanner != null) {
//            admobBanner.destroy();
//        }
//        if (tapdaqBanner1 != null) {
//            tapdaqBanner1.destroy(getActivity());
//        }
    }

}